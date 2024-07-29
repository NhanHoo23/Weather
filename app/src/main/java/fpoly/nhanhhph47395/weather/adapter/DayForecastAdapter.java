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

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import fpoly.nhanhhph47395.weather.R;
import fpoly.nhanhhph47395.weather.models.Forecast;
import fpoly.nhanhhph47395.weather.utils.AppManager;


public class DayForecastAdapter extends RecyclerView.Adapter<DayForecastAdapter.DayForecastViewHolder> {

    private Context mContext;
    private List<Forecast.ForecastDay> list;
    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat outputFormat = new SimpleDateFormat("DDD", new Locale("vi", "VN"));

    public DayForecastAdapter(Context mContext, List<Forecast.ForecastDay> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public DayForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.layout_rc_day_forecast_item, parent, false);

        return new DayForecastViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull DayForecastViewHolder holder, int position) {
        Forecast.ForecastDay day = list.get(position);

        try {
            Date date = inputFormat.parse(day.date);

            Calendar calendarToday = Calendar.getInstance();

            Calendar calendarProvided = Calendar.getInstance();
            calendarProvided.setTime(date);

            if (calendarToday.get(Calendar.YEAR) == calendarProvided.get(Calendar.YEAR) &&
                    calendarToday.get(Calendar.MONTH) == calendarProvided.get(Calendar.MONTH) &&
                    calendarToday.get(Calendar.DAY_OF_MONTH) == calendarProvided.get(Calendar.DAY_OF_MONTH)) {
                holder.tvDay.setText("Hôm nay");
            } else {
                int dayOfWeek = calendarProvided.get(Calendar.DAY_OF_WEEK);
                DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(Locale.getDefault());
                String[] weekdays = dateFormatSymbols.getWeekdays();
                String dayOfWeekString = weekdays[dayOfWeek];

                holder.tvDay.setText(dayOfWeekString);
            }

            Glide.with(mContext)
                    .load("https:" + day.day.condition.icon)
                    .into(holder.imgWeather);
            holder.tvHumidity.setText("\uD83D\uDCA7" + day.day.avghumidity + "%");
            boolean isTempC = AppManager.shared(mContext).getSelectedTempIndex() == 0;
            holder.tvTemp.setText((isTempC ? (int)day.day.mintemp_c : (int)day.day.mintemp_f) + "°/" + (isTempC ? (int)day.day.maxtemp_c : (int)day.day.maxtemp_f) + "°");
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

    public static class DayForecastViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDay, tvHumidity, tvTemp;
        private ImageView imgWeather;

        public DayForecastViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDay = itemView.findViewById(R.id.tvDay);
            tvHumidity = itemView.findViewById(R.id.tvHumidity);
            tvTemp = itemView.findViewById(R.id.tvTemp);
            imgWeather = itemView.findViewById(R.id.imgWeather);
        }
    }
}
