package fpoly.nhanhhph47395.weather.utils;

import fpoly.nhanhhph47395.weather.models.WeatherResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class WeatherManager {
    private WeatherAPIService apiService;
    private static final String API_KEY = "dc7e6eea1ece4db598e92310241407";

    public WeatherManager() {
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        apiService = retrofit.create(WeatherAPIService.class);
    }

    public void getWeather(String location, int days, String language, final WeatherCallback callback) {
        Call<WeatherResponse> call = apiService.getWeather(API_KEY, location, days, language);
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

    public interface WeatherCallback {
        void onSuccess(WeatherResponse weatherResponse);
        void onFailure(Throwable throwable);
    }
}
