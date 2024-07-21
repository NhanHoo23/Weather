package fpoly.nhanhhph47395.weather.utils;

import fpoly.nhanhhph47395.weather.models.WeatherResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPIService {
    @GET("forecast.json")
    Call<WeatherResponse> getWeatherBySpecificLocation(
            @Query("key") String apiKey,
            @Query("q") String location,
            @Query("days") int days,
            @Query("lang") String language
    );

    @GET("forecast.json")
    Call<WeatherResponse> getWeatherByLongAndLat(
            @Query("key") String apiKey,
            @Query("q") String latAndLong,
            @Query("days") int days,
            @Query("lang") String language
    );
}
