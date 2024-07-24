package fpoly.nhanhhph47395.weather.utils;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fpoly.nhanhhph47395.weather.models.Location;
import fpoly.nhanhhph47395.weather.models.WeatherResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class WeatherManager {
    private static WeatherManager instance;
    private WeatherAPIService apiService;
    private static final String API_KEY = "dc7e6eea1ece4db598e92310241407";
    public List<WeatherResponse> locationList;


    public WeatherManager() {
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        apiService = retrofit.create(WeatherAPIService.class);
        locationList = new ArrayList<>();
    }

    public static synchronized WeatherManager shared() {
        if (instance == null) {
            instance = new WeatherManager();
        }
        return instance;
    }

    public List<WeatherResponse> getLocationList() {
        return locationList;
    }

    public void getWeatherBySpecificLocation(String location, int days, String language, final WeatherCallback callback) {
        Call<WeatherResponse> call = apiService.getWeatherBySpecificLocation(API_KEY, location, days, language);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Throwable("Response not successful"));
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public void getSearchResults(String query, final SearchCallback callback) {
        Call<List<Location>> call = apiService.searchLocations(API_KEY, query);
        call.enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Throwable("Response not successful"));
                }
            }

            @Override
            public void onFailure(Call<List<Location>> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public void fetchAndStoreWeatherData(Context context) {
        List<String> locationListString = AppManager.shared(context).loadLocationList();

        for (String location : locationListString) {
            getWeatherBySpecificLocation(location, 10, "vi", new WeatherCallback() {
                @Override
                public void onSuccess(WeatherResponse weatherResponse) {
                    locationList.add(weatherResponse);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    Log.d("onFailure", "onFailure: " + location + "-" +throwable.getLocalizedMessage());
                }
            });
        }
    }

    public interface WeatherCallback {
        void onSuccess(WeatherResponse weatherResponse);
        void onFailure(Throwable throwable);
    }

    public interface SearchCallback {
        void onSuccess(List<Location> locations);
        void onFailure(Throwable throwable);
    }
}
