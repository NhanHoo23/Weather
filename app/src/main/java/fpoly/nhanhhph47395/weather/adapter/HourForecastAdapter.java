package fpoly.nhanhhph47395.weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fpoly.nhanhhph47395.weather.R;
import fpoly.nhanhhph47395.weather.models.Forecast;


public class HourForecastAdapter extends RecyclerView.Adapter<HourForecastAdapter.HourForecastViewHolder> {

    private Context mContext;
    private List<Forecast.ForecastDay.Hour> list;
    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");

    public HourForecastAdapter(Context mContext, List<Forecast.ForecastDay.Hour> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public HourForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.layout_rc_hour_forecast_item, parent, false);

        return new HourForecastViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull HourForecastViewHolder holder, int position) {
        Forecast.ForecastDay.Hour hour = list.get(position);

        try {
            Date date = inputFormat.parse(hour.time);
            String formattedTime = outputFormat.format(date);

            Calendar calendar = Calendar.getInstance();
//            int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

            holder.tvHour.setText(formattedTime);
            Glide.with(mContext)
                    .load("https:" + hour.condition.icon) // Add "https:" if the URL is relative
                    .into(holder.imgWeather);
            holder.tvHumidity.setText("\uD83D\uDCA7" + hour.humidity + "%");
            holder.tvTemp.setText((int)hour.temp_c + "°");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        return 0;
    }

    public static class HourForecastViewHolder extends RecyclerView.ViewHolder {
        private TextView tvHour, tvHumidity, tvTemp;
        private ImageView imgWeather;

        public HourForecastViewHolder(@NonNull View itemView) {
            super(itemView);

            tvHour = itemView.findViewById(R.id.tvHour);
            tvHumidity = itemView.findViewById(R.id.tvHumidity);
            tvTemp = itemView.findViewById(R.id.tvTemp);
            imgWeather = itemView.findViewById(R.id.imgWeather);
        }
    }
}
