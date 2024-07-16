package fpoly.nhanhhph47395.weather.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fpoly.nhanhhph47395.weather.R;
import fpoly.nhanhhph47395.weather.adapter.DayForecastAdapter;
import fpoly.nhanhhph47395.weather.adapter.HourForecastAdapter;
import fpoly.nhanhhph47395.weather.models.Forecast;;
import fpoly.nhanhhph47395.weather.models.WeatherEvaluate;
import fpoly.nhanhhph47395.weather.models.WeatherResponse;
import fpoly.nhanhhph47395.weather.subviews.WindView;
import fpoly.nhanhhph47395.weather.utils.WeatherManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private TextView tvLocation, tvTemp, tvStatus, tvHighestTemp, tvLowestTemp, tvPrecip, tvPrecipForecast, tvUV, tvUVWarning, tvUVAdvice, tvHumidity, tvHumidityEvaluate, tvVision, tvVisionEvaluate, tvFeelLike;
    private RecyclerView rcHourForecast, rcDayForecast;
    private HourForecastAdapter hourForecastAdapter;
    private DayForecastAdapter dayForecastAdapter;
    private WindView windView;

    private WeatherManager weatherManager;
    private List<Forecast.ForecastDay.Hour> hoursList;
    private List<Forecast.ForecastDay> daysList;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        tvLocation = v.findViewById(R.id.tvLocation);
        tvTemp = v.findViewById(R.id.tvTemp);
        tvStatus = v.findViewById(R.id.tvStatus);
        tvHighestTemp = v.findViewById(R.id.tvHighestTemp);
        tvLowestTemp = v.findViewById(R.id.tvLowestTemp);
        rcHourForecast = v.findViewById(R.id.rcHourForecast);
        rcDayForecast = v.findViewById(R.id.rcDayForecast);
        windView = v.findViewById(R.id.windView);
        tvPrecip = v.findViewById(R.id.tvPrecip);
        tvPrecipForecast = v.findViewById(R.id.tvPrecipForecast);
        tvUV = v.findViewById(R.id.tvUV);
        tvUVWarning = v.findViewById(R.id.tvUVWarning);
        tvUVAdvice = v.findViewById(R.id.tvUVAdvice);
        tvHumidity = v.findViewById(R.id.tvHumidity);
        tvHumidityEvaluate = v.findViewById(R.id.tvHumidityEvaluate);
        tvVision = v.findViewById(R.id.tvVision);
        tvVisionEvaluate = v.findViewById(R.id.tvVisionEvaluate);
        tvFeelLike = v.findViewById(R.id.tvFeelLike);

//        float windSpeed = 10.8F;  // Đơn vị km/h
//        double windDirection = 76;
//
//
        weatherManager = new WeatherManager();
        weatherManager.getWeather("Hanoi", 10, "vi", new WeatherManager.WeatherCallback() {
            @Override
            public void onSuccess(WeatherResponse weatherResponse) {
                hoursList = get24HourForecast(weatherResponse);
                daysList = weatherResponse.forecast.forecastday;
                updateUI(weatherResponse);
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d("Huhuhu", "Không lấy được data. Lỗi: " + throwable.getLocalizedMessage());
            }
        });

        return v;
    }

    private void updateUI(WeatherResponse weatherResponse) {
        tvLocation.setText(weatherResponse.location.name);
        tvTemp.setText(String.valueOf((int) weatherResponse.current.temp_c) + "°");
        tvStatus.setText(weatherResponse.current.condition.text);
        tvHighestTemp.setText("C:" + String.valueOf((int)weatherResponse.forecast.forecastday.get(0).day.maxtemp_c) + "°");
        tvLowestTemp.setText("T:" + String.valueOf((int)weatherResponse.forecast.forecastday.get(0).day.mintemp_c) + "°");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rcHourForecast.setLayoutManager(linearLayoutManager);
        hourForecastAdapter = new HourForecastAdapter(getContext(), hoursList);
        rcHourForecast.setAdapter(hourForecastAdapter);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcDayForecast.setLayoutManager(linearLayoutManager2);
        dayForecastAdapter = new DayForecastAdapter(getContext(), daysList);
        rcDayForecast.setAdapter(dayForecastAdapter);

        windView.setWindDirection(weatherResponse.current.wind_degree, "10");

        tvPrecip.setText(String.valueOf((int)weatherResponse.current.precip_mm) + " mm");
        tvPrecipForecast.setText("Dự báo: " + (int)weatherResponse.forecast.forecastday.get(1).day.totalprecip_mm + "mm trong 24h tiếp theo" );
        tvUV.setText(String.valueOf(weatherResponse.current.uv));
        WeatherEvaluate.UVLevel uvLevel = WeatherEvaluate.UVLevel.getUVLevel(weatherResponse.current.uv);
        tvUVWarning.setText(uvLevel.getDisplayName());
        tvUVAdvice.setText(uvLevel.getDetailedDescription());
        tvHumidity.setText(String.valueOf(weatherResponse.current.humidity)+"%");
        tvHumidityEvaluate.setText("Điểm sương là " + (int)weatherResponse.current.dewpoint_c + "° ngay lúc này");
        tvVision.setText(String.valueOf(weatherResponse.current.vis_km + " km"));
        WeatherEvaluate.VisibilityLevel visibilityLevel = WeatherEvaluate.VisibilityLevel.getVisibilityLevel(weatherResponse.current.vis_km);
        tvVisionEvaluate.setText(visibilityLevel.getDisplayName());
        tvFeelLike.setText(String.valueOf((int)weatherResponse.current.feelslike_c) + "°");
    }

    private List<Forecast.ForecastDay.Hour> get24HourForecast(WeatherResponse weatherResponse) {
        List<Forecast.ForecastDay.Hour> hourlyForecasts = new ArrayList<>();

        // Get the current hour
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        // Get the hours from the current hour to the end of the first day
        List<Forecast.ForecastDay.Hour> firstDayHours = weatherResponse.forecast.forecastday.get(0).hour;
        for (int i = currentHour; i < firstDayHours.size(); i++) {
            hourlyForecasts.add(firstDayHours.get(i));
        }

        // Get the remaining hours from the next day to make up the 24 hours
        List<Forecast.ForecastDay.Hour> secondDayHours = weatherResponse.forecast.forecastday.get(1).hour;
        for (int i = 0; i < 24 - (firstDayHours.size() - currentHour); i++) {
            hourlyForecasts.add(secondDayHours.get(i));
        }

        return hourlyForecasts;
    }
}