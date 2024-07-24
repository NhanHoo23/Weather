package fpoly.nhanhhph47395.weather.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fpoly.nhanhhph47395.weather.R;
import fpoly.nhanhhph47395.weather.adapter.DayForecastAdapter;
import fpoly.nhanhhph47395.weather.adapter.HourForecastAdapter;
import fpoly.nhanhhph47395.weather.models.Forecast;
import fpoly.nhanhhph47395.weather.models.Location;
import fpoly.nhanhhph47395.weather.models.WeatherEvaluate;
import fpoly.nhanhhph47395.weather.models.WeatherResponse;
import fpoly.nhanhhph47395.weather.subviews.WindView;
import fpoly.nhanhhph47395.weather.utils.AppManager;
import fpoly.nhanhhph47395.weather.utils.WeatherManager;

public class LocationForecastFragment extends BottomSheetDialogFragment {
    private Location selectedLocation;
    private NestedScrollView nestedScrollView;
    private ProgressBar progressBar;
    private RelativeLayout header;
    private TextView tvMenu, tvCancel, tvAdd, tvLocation, tvTemp, tvStatus, tvHighestTemp, tvLowestTemp, tvPrecip, tvPrecipForecast, tvUV, tvUVWarning, tvUVAdvice, tvHumidity, tvHumidityEvaluate, tvVision, tvVisionEvaluate, tvFeelLike;
    private RecyclerView rcHourForecast, rcDayForecast;
    private HourForecastAdapter hourForecastAdapter;
    private DayForecastAdapter dayForecastAdapter;
    private WindView windView;

    private List<Forecast.ForecastDay.Hour> hoursList;
    private List<Forecast.ForecastDay> daysList;
    private WeatherResponse weatherResponseLocation;

    private LocationForecastFragmentListener listener;

    public LocationForecastFragment(Location selectedLocation, LocationForecastFragmentListener listener) {
        this.selectedLocation = selectedLocation;
        this.listener = listener;
    }

    public static LocationForecastFragment newInstance(Location location, LocationForecastFragmentListener listener) {
        LocationForecastFragment fragment = new LocationForecastFragment(location, listener);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        setupView(view);

        callAPI();

        return view;
    }

    private void setupView(View v) {
        v.setBackgroundResource(R.drawable.rounded_corner);
        tvMenu = v.findViewById(R.id.dropdown_menu);
        header = v.findViewById(R.id.header);
        tvCancel = v.findViewById(R.id.tvCancel);
        tvAdd = v.findViewById(R.id.tvAdd);
        nestedScrollView = v.findViewById(R.id.nestedScrollView);
        progressBar = v.findViewById(R.id.progressBar);
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

        tvMenu.setVisibility(View.INVISIBLE);
        tvLocation.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        nestedScrollView.setVisibility(View.INVISIBLE);
        header.setVisibility(View.VISIBLE);

        tvCancel.setOnClickListener(v1 -> {dismiss();});
        tvAdd.setOnClickListener(v1 -> {
            addLocation();
        });
    }

    private void callAPI() {
        WeatherManager.shared().getWeatherBySpecificLocation(selectedLocation.name, 10, "vi", new WeatherManager.WeatherCallback() {
            @Override
            public void onSuccess(WeatherResponse weatherResponse) {
                weatherResponseLocation = weatherResponse;
                hoursList = get24HourForecast(weatherResponse);
                daysList = weatherResponse.forecast.forecastday;
                updateUI(weatherResponse);
            }

            @Override
            public void onFailure(Throwable throwable) {
                //In ra thông báo lỗi data hoặc ko có mạng
                progressBar.setVisibility(View.GONE);
                Log.d("Huhuhu", "Không lấy được data. Lỗi: " + throwable.getLocalizedMessage());
            }
        });
    }

    private void updateUI(WeatherResponse weatherResponse) {
        progressBar.setVisibility(View.GONE);
        nestedScrollView.setVisibility(View.VISIBLE);

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

        windView.setWindDirection(weatherResponse.current.wind_degree, String.valueOf((int) weatherResponse.current.wind_kph), "km/h");

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm");
        LocalDateTime dateTime = LocalDateTime.parse(weatherResponse.location.localtime, formatter);
        int currentHour = dateTime.getHour();

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

    private void addLocation() {
        List<String> locationListString = AppManager.shared(getContext()).loadLocationList();
        locationListString.add(selectedLocation.name);
        AppManager.shared(getContext()).saveLocationList(locationListString);

        WeatherManager.shared().locationList.add(weatherResponseLocation);

        if (listener != null) {
            listener.updateUI();
        }
        dismiss();
    }

    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {
            View parent = (View) view.getParent();
            BottomSheetBehavior<?> behavior = BottomSheetBehavior.from(parent);
            int height = getResources().getDisplayMetrics().heightPixels * 19 / 20;
            behavior.setPeekHeight(height);
            parent.setBackgroundResource(android.R.color.transparent);
        }
    }

    public interface LocationForecastFragmentListener {
        void updateUI();
    }
}
