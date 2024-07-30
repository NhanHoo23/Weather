package fpoly.nhanhhph47395.weather.utils;

import android.app.UiModeManager;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class AppManager {
    private static final String PREFS_NAME = "app_prefs";
    private static final String KEY_LOCATION_ENABLED = "location_enabled";
    private static final String KEY_NOTIFICATION_ENABLED = "notification_enabled";

    private static AppManager instance;
    private SharedPreferences sharedPreferences;

    public final int REQUEST_LOCATION_PERMISSION = 1;
    public int selectedFragment = 0;


    private AppManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized AppManager shared(Context context) {
        if (instance == null) {
            instance = new AppManager(context.getApplicationContext());
        }
        return instance;
    }

    public void setFirstLogin(boolean enabled) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isFirstLogin", enabled);
        editor.apply();
    }

    public boolean isFirstLogin() {
        return sharedPreferences.getBoolean("isFirstLogin", true);
    }

    public void setDarkModeStatusBasedOnDevice(Context context) {
        UiModeManager uiModeManager = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
        int currentMode = uiModeManager.getNightMode();
        boolean isDarkMode = (currentMode == UiModeManager.MODE_NIGHT_YES);
        instance.setDarkModeStatus(isDarkMode);
    }



    //Setting
    public void setLocationEnabled(boolean enabled) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_LOCATION_ENABLED, enabled);
        editor.apply();
    }

    public boolean isLocationEnabled() {
        return sharedPreferences.getBoolean(KEY_LOCATION_ENABLED, false);
    }

    public void setNotificationEnabled(boolean enabled) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_NOTIFICATION_ENABLED, enabled);
        editor.apply();
    }

    public boolean isNotificationEnabled() {
        return sharedPreferences.getBoolean(KEY_NOTIFICATION_ENABLED, false);
    }

    public void setDefaultLongitude(float longitude) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("defaultLongitude", longitude);
        editor.apply();
    }

    public float defaultLongitude() {
        return sharedPreferences.getFloat("defaultLongitude", 0);
    }

    public void setDefaultLatitude(float latitude) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("defaultLatitude", latitude);
        editor.apply();
    }

    public float defaultLatitude() {
        return sharedPreferences.getFloat("defaultLatitude", 0);
    }

    public void saveLocationList(List<String> locationList) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("locationList", new Gson().toJson(locationList));
        editor.apply();
    }

    public List<String> loadLocationList() {
        String json = sharedPreferences.getString("locationList", "[]");
        Type type = new TypeToken<List<String>>() {}.getType();
        return new Gson().fromJson(json, type);
    }

    public void setSelectedTempIndex(int index) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("selectedTempIndex", index);
        editor.apply();
    }

    public int getSelectedTempIndex() {
        return sharedPreferences.getInt("selectedTempIndex", 0);
    }

    public void setSelectedWindSpeedIndex(int index) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("selectedWindSpeedIndex", index);
        editor.apply();
    }

    public int getSelectedWindSpeedIndex() {
        return sharedPreferences.getInt("selectedWindSpeedIndex", 0);
    }

    public void setSelectedPrecipitationIndex(int index) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("selectedPrecipitationIndex", index);
        editor.apply();
    }

    public int getSelectedPrecipitationIndex() {
        return sharedPreferences.getInt("selectedPrecipitationIndex", 0);
    }

    public void setSelectedDistanceIndex(int index) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("selectedDistanceIndex", index);
        editor.apply();
    }

    public int getSelectedDistanceIndex() {
        return sharedPreferences.getInt("selectedDistanceIndex", 0);
    }

    public void setSelectedLanguageIndex(int index) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("selectedLanguageIndex", index);
        editor.apply();
    }

    public int getSelectedLanguageIndex() {
        return sharedPreferences.getInt("selectedLanguageIndex", 0);
    }

    public void setSelectedDefaultLocationIndex(int index) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("selectedDefaultLocationIndex", index);
        editor.apply();
    }

    public int getSelectedDefaultLocationIndex() {
        return sharedPreferences.getInt("selectedDefaultLocationIndex", 0);
    }

    public void setDarkModeStatus(boolean isDarkMode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("darkModeStatus", isDarkMode);
        editor.apply();
    }

    public boolean getDarkModeStatus() {
        return sharedPreferences.getBoolean("darkModeStatus", false);
    }

    public static void applyTheme(boolean isDarkMode) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

}



