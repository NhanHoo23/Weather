package fpoly.nhanhhph47395.weather.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AppManager {
    private static final String PREFS_NAME = "app_prefs";
    private static final String KEY_LOCATION_ENABLED = "location_enabled";
    private static final String KEY_NOTIFICATION_ENABLED = "notification_enabled";

    private static AppManager instance;
    private SharedPreferences sharedPreferences;

    public final int REQUEST_LOCATION_PERMISSION = 1;


    private AppManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized AppManager shared(Context context) {
        if (instance == null) {
            instance = new AppManager(context.getApplicationContext());
        }
        return instance;
    }

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

    public void setFirstLogin(boolean enabled) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isFirstLogin", enabled);
        editor.apply();
    }

    public boolean isFirstLogin() {
        return sharedPreferences.getBoolean("isFirstLogin", false);
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
}



