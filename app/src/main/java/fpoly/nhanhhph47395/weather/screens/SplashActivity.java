package fpoly.nhanhhph47395.weather.screens;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import fpoly.nhanhhph47395.weather.R;
import fpoly.nhanhhph47395.weather.utils.AppManager;
import fpoly.nhanhhph47395.weather.utils.WeatherManager;

public class SplashActivity extends AppCompatActivity {
    private boolean notificationPromptShown = false;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        //set language
        if (AppManager.shared(this).isFirstLogin()) {
            AppManager.shared(this).setDarkModeStatusBasedOnDevice(this);

            String deviceLanguage = Locale.getDefault().getLanguage();

            if (deviceLanguage.equals("vi")) {
                AppManager.shared(this).setSelectedLanguageIndex(0);
            } else {
                AppManager.shared(this).setSelectedLanguageIndex(1);
            }
        }

        //set darkmode
        AppManager.applyTheme(AppManager.shared(this).getDarkModeStatus());


        //get location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        new Handler().postDelayed(() -> {
            if (AppManager.shared(this).isFirstLogin()) {
                showNotificationPrompt();
            } else {
                checkLocationPermission();
                if (AppManager.shared(SplashActivity.this).isLocationEnabled()) {
                    getLastKnownLocation()
                            .addOnCompleteListener(task -> {
                                if (!task.isSuccessful()) {
                                    Log.e("Initialization Error", "Failed to get last known location", task.getException());
                                }

                                WeatherManager.shared().fetchAndStoreWeatherData(this)
                                        .thenAccept(aVoid -> {
                                            goToMainActivity();
                                        })
                                        .exceptionally(throwable -> {
                                            Log.e("Initialization Error", "Failed to fetch weather data", throwable);
                                            goToMainActivity();
                                            return null;
                                        });
                            });
                } else {
                    WeatherManager.shared().fetchAndStoreWeatherData(this)
                            .thenAccept(aVoid -> {
                                goToMainActivity();
                            })
                            .exceptionally(throwable -> {
                                Log.e("Initialization Error", "Failed to fetch weather data", throwable);
                                goToMainActivity();
                                return null;
                            });
                }

            }
        }, 3000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (notificationPromptShown) {
            checkNotificationStatus();
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (!AppManager.shared(this).isFirstLogin()) {
            AppManager.applyTheme(AppManager.shared(this).getDarkModeStatus());
        }
    }

    private void showNotificationPrompt() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (!notificationManager.areNotificationsEnabled()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Bạn có muốn nhận thông báo không?")
                    .setMessage("Vui lòng bật thông báo để nhận những thông tin mới nhất.")
                    .setPositiveButton("Bật", (dialog, which) -> {
                        Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                                .putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                        startActivity(intent);
                        notificationPromptShown = true;
                    })
                    .setNegativeButton("Hủy", (dialog, which) -> {
                        AppManager.shared(this).setNotificationEnabled(false);
                        requestLocationPermission();
                    })
                    .show();
        } else {
            AppManager.shared(this).setNotificationEnabled(true);
            AppManager.shared(this).saveTime(6, 0, "dayTime");
            AppManager.shared(this).saveTime(18, 0, "nightTime");
            setupNoti(6, 0, 18, 0);
            requestLocationPermission();
        }
    }

    private void checkNotificationStatus() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (notificationManager.areNotificationsEnabled()) {
            AppManager.shared(this).setNotificationEnabled(true);
            requestLocationPermission();
        } else {
            AppManager.shared(this).setNotificationEnabled(false);
            showNotificationPrompt();
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            AppManager.shared(this).setLocationEnabled(true);
        } else {
            AppManager.shared(this).setLocationEnabled(false);
        }
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    AppManager.shared(this).REQUEST_LOCATION_PERMISSION);
        } else {
            AppManager.shared(this).setLocationEnabled(true);
            goToMainActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == AppManager.shared(this).REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                AppManager.shared(this).setLocationEnabled(true);
                getLastKnownLocation()
                        .addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {
                                Log.e("Initialization Error", "Failed to get last known location", task.getException());
                            }

                            WeatherManager.shared().fetchAndStoreWeatherData(this)
                                    .thenAccept(aVoid -> {
                                        goToMainActivity();
                                    })
                                    .exceptionally(throwable -> {
                                        goToMainActivity();
                                        Log.e("Initialization Error", "Failed to fetch weather data", throwable);
                                        return null;
                                    });
                        });
            } else {
                AppManager.shared(this).setLocationEnabled(false);
                goToMainActivity();
            }

        }
    }

    private void goToMainActivity() {
        AppManager.shared(this).setFirstLogin(false);
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private Task<Void> getLastKnownLocation() {
        final TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            taskCompletionSource.setException(new SecurityException("Location permissions not granted"));
            return taskCompletionSource.getTask();
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            List<String> locationList = AppManager.shared(SplashActivity.this).loadLocationList();
                            String latAndLong = location.getLatitude()+","+location.getLongitude();

                            if (AppManager.shared(SplashActivity.this).isFirstLogin()) {
                                if (locationList.isEmpty()) {
                                    locationList.add(latAndLong);
                                }
                            } else {
                                if (locationList.isEmpty()) {
                                    locationList.add(latAndLong);
                                } else {
                                    if (AppManager.shared(SplashActivity.this).hasLatAndLong()) {
                                        locationList.set(0, latAndLong);
                                    } else {
                                        locationList.add(0, latAndLong);
                                    }
                                }
                            }
                            AppManager.shared(SplashActivity.this).saveLocationList(locationList);
                            AppManager.shared(SplashActivity.this).setHasLatAndLong(true);

                            taskCompletionSource.setResult(null);
                        } else {
                            taskCompletionSource.setException(new Exception("Location is null"));
                        }
                    }
                });
        return taskCompletionSource.getTask();
    }

    private void setDailyNotification(int hourOfDay, int minute, int requestCode) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("notification_id", requestCode);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_MUTABLE); // hoặc PendingIntent.FLAG_IMMUTABLE

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void setupNoti(int dayHour, int dayMinute, int nightHour, int nightMinute) {
        AppManager.shared(this).saveTime(dayHour, dayMinute, "dayTime");
        setDailyNotification(dayHour, dayMinute, 0);

        AppManager.shared(this).saveTime(nightHour, nightMinute, "nightTime");
        setDailyNotification(nightHour, nightMinute, 1);
    }
}