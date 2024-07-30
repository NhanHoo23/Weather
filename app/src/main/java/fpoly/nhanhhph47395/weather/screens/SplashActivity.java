package fpoly.nhanhhph47395.weather.screens;

import android.Manifest;
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

import java.util.ArrayList;
import java.util.List;

import fpoly.nhanhhph47395.weather.R;
import fpoly.nhanhhph47395.weather.fragments.HomeFragment;
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

        if (AppManager.shared(this).isFirstLogin()) {
            AppManager.shared(this).setDarkModeStatusBasedOnDevice(this);
        }
        AppManager.applyTheme(AppManager.shared(this).getDarkModeStatus());

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        new Handler().postDelayed(() -> {
            if (!AppManager.shared(this).isFirstLogin()) {
                showNotificationPrompt();
            } else {
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
                                        return null;
                                    });
                        });
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
                getLastKnownLocation()
                        .addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {
                                Log.e("Initialization Error", "Failed to get last known location", task.getException());
                            }

                            WeatherManager.shared().fetchAndStoreWeatherData(this)
                                    .thenAccept(aVoid -> {
                                        AppManager.shared(this).setLocationEnabled(true);
                                        goToMainActivity();
                                    })
                                    .exceptionally(throwable -> {
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
                            AppManager.shared(SplashActivity.this).setDefaultLongitude((float) location.getLongitude());
                            AppManager.shared(SplashActivity.this).setDefaultLatitude((float) location.getLatitude());

                            List<String> locationList = AppManager.shared(SplashActivity.this).loadLocationList();
                            String latAndLong = location.getLatitude()+","+location.getLongitude();
                            if (locationList.isEmpty()) {
                                locationList.add(latAndLong);
                            } else {
                                if (AppManager.shared(SplashActivity.this).isLocationEnabled()) {
                                    locationList.set(0, latAndLong);
                                } else {
                                    locationList.add(0, latAndLong);
                                }
                            }
                            AppManager.shared(SplashActivity.this).saveLocationList(locationList);

                            taskCompletionSource.setResult(null);
                        } else {
                            taskCompletionSource.setException(new Exception("Location is null"));
                        }
                    }
                });
        return taskCompletionSource.getTask();
    }


}