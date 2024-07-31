package fpoly.nhanhhph47395.weather.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import fpoly.nhanhhph47395.weather.R;
import fpoly.nhanhhph47395.weather.models.Forecast;
import fpoly.nhanhhph47395.weather.models.Location;


public class SearchTextAdapter extends RecyclerView.Adapter<SearchTextAdapter.SearchTextViewHolder> {
    private Context mContext;
    private List<Location> list;
    private SearchTextAdapterClickListener listener;

    public SearchTextAdapter(Context mContext, SearchTextAdapterClickListener listener) {
        this.mContext = mContext;
        this.listener = listener;
    }

    public void updateData(List<Location> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    public void clearData() {
        if (list != null) {
            list.clear();
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public SearchTextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1, parent, false);

        return new SearchTextViewHolder(mView, listener, mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchTextViewHolder holder, int position) {
        Location item = list.get(position);
        if (item.region.equals("")) {
            holder.bind(item.name + ", " +
                    item.country);
        } else {
            holder.bind(item.name + ", " +
                    item.region + ", " +
                    item.country);
        }
    }

    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        return 0;
    }

    public static class SearchTextViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSearch;

        public SearchTextViewHolder(@NonNull View itemView, final SearchTextAdapterClickListener listener, Context context) {
            super(itemView);

            tvSearch = itemView.findViewById(android.R.id.text1);
            tvSearch.setSingleLine();
            tvSearch.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
            tvSearch.setEllipsize(TextUtils.TruncateAt.END); // Tạo dấu 3 chấm
            tvSearch.setTextColor(context.getResources().getColor(R.color.mainTextColor));

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }

        public void bind(String result) {
            tvSearch.setText(result);
        }
    }

    public interface SearchTextAdapterClickListener {
        void onItemClick(int position);
    }
}


