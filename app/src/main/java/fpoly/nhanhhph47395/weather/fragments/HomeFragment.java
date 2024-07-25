package fpoly.nhanhhph47395.weather.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fpoly.nhanhhph47395.weather.R;
import fpoly.nhanhhph47395.weather.adapter.DayForecastAdapter;
import fpoly.nhanhhph47395.weather.adapter.HourForecastAdapter;
import fpoly.nhanhhph47395.weather.adapter.LocationAdapter;
import fpoly.nhanhhph47395.weather.databinding.FragmentHomeBinding;
import fpoly.nhanhhph47395.weather.models.Forecast;;
import fpoly.nhanhhph47395.weather.models.Location;
import fpoly.nhanhhph47395.weather.models.WeatherEvaluate;
import fpoly.nhanhhph47395.weather.models.WeatherResponse;
import fpoly.nhanhhph47395.weather.subviews.WindView;
import fpoly.nhanhhph47395.weather.utils.AppManager;
import fpoly.nhanhhph47395.weather.utils.WeatherManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private HourForecastAdapter hourForecastAdapter;
    private DayForecastAdapter dayForecastAdapter;
    private PopupMenu popupMenu;

    private List<Forecast.ForecastDay.Hour> hoursList;
    private List<Forecast.ForecastDay> daysList;

    public HomeFragment() {}

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        setupView();

        Bundle args = getArguments();
        if (args != null) {
            int locationIndex = args.getInt("locationIndex");

            callAPI(locationIndex);
        } else {
            callAPI(0);
        }

        return view;
    }

    private void setupView() {
        binding.dropdownMenu.setVisibility(View.VISIBLE);
        binding.tvLocation.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.nestedScrollView.setVisibility(View.GONE);

        popupMenu = new PopupMenu(getContext(), binding.dropdownMenu);

        binding.dropdownMenu.setOnClickListener(v1 -> {
            showPopupMenu(binding.dropdownMenu);
        });

    }

    private void showPopupMenu(View view) {
        popupMenu.getMenu().clear();

        List<WeatherResponse> arr = WeatherManager.shared().getLocationList();
        for (int i = 0; i < arr.size(); i++) {
            if(i == 0) {
                popupMenu.getMenu().add(0, i, i, "Vị trí của tôi");
            } else {
                popupMenu.getMenu().add(0, i, i, arr.get(i).location.name);
            }
        }

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            int index = menuItem.getItemId();
            callAPI(index);

            return true;
        });

        popupMenu.show();
    }

    private void callAPI(int index) {
        WeatherManager.shared().getWeatherBySpecificLocation(AppManager.shared(getContext()).loadLocationList().get(index), 10, "vi", new WeatherManager.WeatherCallback() {
            @Override
            public void onSuccess(WeatherResponse weatherResponse) {
                hoursList = get24HourForecast(weatherResponse);
                daysList = weatherResponse.forecast.forecastday;
                updateUI(weatherResponse);
            }

            @Override
            public void onFailure(Throwable throwable) {
                //In ra thông báo lỗi data hoặc ko có mạng
                binding.progressBar.setVisibility(View.GONE);
                Log.d("Huhuhu", "Không lấy được data. Lỗi: " + throwable.getLocalizedMessage());
            }
        });
    }

    private void updateUI(WeatherResponse weatherResponse) {
        binding.progressBar.setVisibility(View.GONE);
        binding.nestedScrollView.setVisibility(View.VISIBLE);

        binding.dropdownMenu.setText(weatherResponse.location.name + " ▼");
        binding.tvLocation.setText(weatherResponse.location.name);
        binding.tvTemp.setText(String.valueOf((int) weatherResponse.current.temp_c) + "°");
        binding.tvStatus.setText(weatherResponse.current.condition.text);
        binding.tvHighestTemp.setText("C:" + String.valueOf((int)weatherResponse.forecast.forecastday.get(0).day.maxtemp_c) + "°");
        binding.tvLowestTemp.setText("T:" + String.valueOf((int)weatherResponse.forecast.forecastday.get(0).day.mintemp_c) + "°");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rcHourForecast.setLayoutManager(linearLayoutManager);
        hourForecastAdapter = new HourForecastAdapter(getContext(), hoursList);
        binding.rcHourForecast.setAdapter(hourForecastAdapter);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.rcDayForecast.setLayoutManager(linearLayoutManager2);
        dayForecastAdapter = new DayForecastAdapter(getContext(), daysList);
        binding.rcDayForecast.setAdapter(dayForecastAdapter);

        binding.windView.setWindDirection(weatherResponse.current.wind_degree, String.valueOf((int) weatherResponse.current.wind_kph), "km/h");

        binding.tvPrecip.setText(String.valueOf((int)weatherResponse.current.precip_mm) + " mm");
        binding.tvPrecipForecast.setText("Dự báo: " + (int)weatherResponse.forecast.forecastday.get(1).day.totalprecip_mm + "mm trong 24h tiếp theo" );
        binding.tvUV.setText(String.valueOf(weatherResponse.current.uv));
        WeatherEvaluate.UVLevel uvLevel = WeatherEvaluate.UVLevel.getUVLevel(weatherResponse.current.uv);
        binding.tvUVWarning.setText(uvLevel.getDisplayName());
        binding.tvUVAdvice.setText(uvLevel.getDetailedDescription());
        binding.tvHumidity.setText(String.valueOf(weatherResponse.current.humidity)+"%");
        binding.tvHumidityEvaluate.setText("Điểm sương là " + (int)weatherResponse.current.dewpoint_c + "° ngay lúc này");
        binding.tvVision.setText(String.valueOf(weatherResponse.current.vis_km + " km"));
        WeatherEvaluate.VisibilityLevel visibilityLevel = WeatherEvaluate.VisibilityLevel.getVisibilityLevel(weatherResponse.current.vis_km);
        binding.tvVisionEvaluate.setText(visibilityLevel.getDisplayName());
        binding.tvFeelLike.setText(String.valueOf((int)weatherResponse.current.feelslike_c) + "°");
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
}