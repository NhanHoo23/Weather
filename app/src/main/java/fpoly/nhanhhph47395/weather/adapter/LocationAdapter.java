package fpoly.nhanhhph47395.weather.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import fpoly.nhanhhph47395.weather.R;
import fpoly.nhanhhph47395.weather.models.Location;
import fpoly.nhanhhph47395.weather.models.WeatherResponse;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {
    private Context mContext;
    private List<WeatherResponse> list;
    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd H:mm");
    SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");

    public LocationAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void updateData(List<WeatherResponse> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.layout_rc_location_item, parent, false);

        return new LocationViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        WeatherResponse weatherResponse = list.get(position);

        if (position == 0) {
            holder.tvLocation.setText("Vị trí của tôi");
            holder.tvTime.setText(weatherResponse.location.name);
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
        holder.tvTemp.setText(String.valueOf((int)weatherResponse.current.temp_c) + "°C");
        holder.tvHighestTemp.setText("C:" + String.valueOf((int)weatherResponse.forecast.forecastday.get(0).day.maxtemp_c) + "°");
        holder.tvLowestTemp.setText("T:" + String.valueOf((int)weatherResponse.forecast.forecastday.get(0).day.mintemp_c) + "°");
    }

    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        return 0;
    }

    public static class LocationViewHolder extends RecyclerView.ViewHolder {
        private TextView tvLocation, tvTime, tvTemp, tvCondition, tvHighestTemp, tvLowestTemp;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);

            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvTemp = itemView.findViewById(R.id.tvTemp);
            tvCondition = itemView.findViewById(R.id.tvCondition);
            tvHighestTemp = itemView.findViewById(R.id.tvHighestTemp);
            tvLowestTemp = itemView.findViewById(R.id.tvLowestTemp);
        }
    }
}
