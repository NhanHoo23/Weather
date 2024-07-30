package fpoly.nhanhhph47395.weather.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import fpoly.nhanhhph47395.weather.R;
import fpoly.nhanhhph47395.weather.fragments.SearchFragment;
import fpoly.nhanhhph47395.weather.models.Location;
import fpoly.nhanhhph47395.weather.models.WeatherResponse;
import fpoly.nhanhhph47395.weather.utils.AppManager;
import fpoly.nhanhhph47395.weather.utils.WeatherManager;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {
    private Context mContext;
    private List<WeatherResponse> list;
    private OnLocationSelectedListener listener;
    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd H:mm");
    SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");
    private SearchFragment fragment;

    private boolean showDeleteButton = false;

    public LocationAdapter(Context mContext, OnLocationSelectedListener listener, SearchFragment fragment) {
        this.mContext = mContext;
        this.listener = listener;
        this.fragment = fragment;
    }

    public void updateData(List<WeatherResponse> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.layout_rc_location_item, parent, false);

        return new LocationViewHolder(mView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        WeatherResponse weatherResponse = list.get(position);

        if (position == 0) {
            if (AppManager.shared(mContext).isLocationEnabled()) {
                holder.tvLocation.setText(mContext.getString(R.string.myLocation));
                holder.tvTime.setText(weatherResponse.location.name);
            } else {
                holder.tvLocation.setText(weatherResponse.location.name);
                holder.tvTime.setText(weatherResponse.location.country);
            }
        } else {
            holder.tvLocation.setText(weatherResponse.location.name);

            Date date = null;
            try {
                date = inputFormat.parse(weatherResponse.location.localtime);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            String formattedTime = outputFormat.format(date);
            holder.tvTime.setText(formattedTime);
        }

        holder.tvCondition.setText(weatherResponse.current.condition.text);
        boolean isTempC = AppManager.shared(mContext).getSelectedTempIndex() == 0;
        holder.tvTemp.setText((isTempC ? (int)weatherResponse.current.temp_c : (int)weatherResponse.current.temp_f) + (isTempC ? "°C" : "°F"));
        holder.tvHighestTemp.setText(mContext.getString(R.string.highestTemp) + (isTempC ? (int)weatherResponse.forecast.forecastday.get(0).day.maxtemp_c : (int)weatherResponse.forecast.forecastday.get(0).day.maxtemp_f) + "°");
        holder.tvLowestTemp.setText(mContext.getString(R.string.lowestTemp) + (isTempC ? (int)weatherResponse.forecast.forecastday.get(0).day.mintemp_c : (int)weatherResponse.forecast.forecastday.get(0).day.mintemp_f) + "°");

        if (position != 0) {
            if (showDeleteButton) {
                holder.btnDelete.setVisibility(View.VISIBLE);
                holder.btnDelete.setAlpha(0f);
                holder.btnDelete.animate()
                        .alpha(1f)
                        .setDuration(300)
                        .setListener(null);
            } else {
                holder.btnDelete.setVisibility(View.GONE);
            }
        }

        holder.btnDelete.setOnClickListener(v -> {
            // Xử lý sự kiện xóa item
            fragment.deleteItem(position);

        });
    }

    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        return 0;
    }

    public static class LocationViewHolder extends RecyclerView.ViewHolder {
        private TextView tvLocation, tvTime, tvTemp, tvCondition, tvHighestTemp, tvLowestTemp;
        public ImageView btnDelete;

        public LocationViewHolder(@NonNull View itemView, final OnLocationSelectedListener listener) {
            super(itemView);

            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvTemp = itemView.findViewById(R.id.tvTemp);
            tvCondition = itemView.findViewById(R.id.tvCondition);
            tvHighestTemp = itemView.findViewById(R.id.tvHighestTemp);
            tvLowestTemp = itemView.findViewById(R.id.tvLowestTemp);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onLocationSelected(position);
                        FragmentManager fragmentManager = ((FragmentActivity)itemView.getContext()).getSupportFragmentManager();
                        fragmentManager.popBackStack();
                    }
                }
            });
        }
    }

    public void toggleDeleteButton() {
        showDeleteButton = !showDeleteButton;
        notifyDataSetChanged();
    }



    public interface OnLocationSelectedListener {
        void onLocationSelected(int position);
    }
}
