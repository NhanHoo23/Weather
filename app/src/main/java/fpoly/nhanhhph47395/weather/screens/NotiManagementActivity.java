package fpoly.nhanhhph47395.weather.screens;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

import fpoly.nhanhhph47395.weather.R;
import fpoly.nhanhhph47395.weather.databinding.ActivityNotiManagementBinding;
import fpoly.nhanhhph47395.weather.utils.AppManager;

public class NotiManagementActivity extends AppCompatActivity {
    ActivityNotiManagementBinding binding;
    private boolean isEnable = false;
    private boolean gotoSetting = false;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNotiManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        isEnable = AppManager.shared(this).isNotificationEnabled();
        setupView();
        setupHandle();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (gotoSetting) {
            checkNotificationStatus();
        }
    }

    private void setupView() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.notiSetting));

        binding.timePickerDay.setIs24HourView(true);
        binding.timePickerNight.setIs24HourView(true);
        binding.btnSwitch.setChecked(isEnable);

        int[] dayTime = AppManager.shared(this).getTime("dayTime");
        binding.timePickerDay.setHour(dayTime[0]);
        binding.timePickerDay.setMinute(dayTime[1]);

        int[] nightTime = AppManager.shared(this).getTime("nightTime");
        binding.timePickerNight.setHour(nightTime[0]);
        binding.timePickerNight.setMinute(nightTime[1]);
    }

    private void setupHandle() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        binding.btnSwitch.setOnClickListener(v -> {
            if (notificationManager.areNotificationsEnabled()) {
                isEnable = !isEnable;
                AppManager.shared(this).setNotificationEnabled(isEnable);

                if (isEnable) {
                    int dayHour = binding.timePickerDay.getHour();
                    int dayMinute = binding.timePickerDay.getMinute();
                    int nightHour = binding.timePickerNight.getHour();
                    int nightMinute = binding.timePickerNight.getMinute();

                    setupNoti(dayHour, dayMinute, nightHour, nightMinute);
                } else {
                    cancelNoti();
                }
            } else {
                gotoSetting = true;
                Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                        .putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                startActivity(intent);
            }

        });

        binding.timePickerDay.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            if (isEnable) {
                int dayHour = binding.timePickerDay.getHour();
                int dayMinute = binding.timePickerDay.getMinute();
                int nightHour = binding.timePickerNight.getHour();
                int nightMinute = binding.timePickerNight.getMinute();

                AppManager.shared(this).saveTime(hourOfDay, minute, "dayTime");
                setupNoti(dayHour, dayMinute, nightHour, nightMinute);
            }
        });

        binding.timePickerNight.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            if (isEnable) {
                int dayHour = binding.timePickerDay.getHour();
                int dayMinute = binding.timePickerDay.getMinute();
                int nightHour = binding.timePickerNight.getHour();
                int nightMinute = binding.timePickerNight.getMinute();

                AppManager.shared(this).saveTime(hourOfDay, minute, "nightTime");
                setupNoti(dayHour, dayMinute, nightHour, nightMinute);
            }
        });
    }

    private void checkNotificationStatus() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (notificationManager.areNotificationsEnabled()) {
            AppManager.shared(this).setNotificationEnabled(true);
            binding.btnSwitch.setChecked(true);
            isEnable = true;
        } else {
            AppManager.shared(this).setNotificationEnabled(false);
            binding.btnSwitch.setChecked(false);
            isEnable = false;
        }
    }

    private void setDailyNotification(int hourOfDay, int minute, int requestCode) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("notification_id", requestCode);
        pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_MUTABLE);

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

    private void cancelNoti() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }
}