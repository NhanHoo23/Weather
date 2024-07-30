package fpoly.nhanhhph47395.weather.utils;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import fpoly.nhanhhph47395.weather.models.Location;
import fpoly.nhanhhph47395.weather.models.WeatherResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class WeatherManager {
    private static WeatherManager instance;
    private WeatherAPIService apiService;
    private static final String API_KEY = "5d1ae5737ac1422c90b10706242907";
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

    public CompletableFuture<Void> fetchAndStoreWeatherData(Context context) {
        List<String> locationListString = AppManager.shared(context).loadLocationList();
        CompletableFuture<Void> future = CompletableFuture.completedFuture(null);

        for (String location : locationListString) {
            future = future.thenCompose(v -> { //thenCompose tạo chuỗi các tác vụ
                CompletableFuture<Void> apiCallFuture = new CompletableFuture<>();
                boolean isEn = AppManager.shared(context).getSelectedLanguageIndex() == 1;
                getWeatherBySpecificLocation(location, 10, isEn ? "en":"vi", new WeatherCallback() {
                    @Override
                    public void onSuccess(WeatherResponse weatherResponse) {
                        synchronized (locationList) {
                            locationList.add(weatherResponse);
                        }
                        apiCallFuture.complete(null);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.d("onFailure", "onFailure: " + location + "-" + throwable.getLocalizedMessage());
                        apiCallFuture.completeExceptionally(throwable);
                    }
                });
                return apiCallFuture;
            });
        }

        return future;
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
