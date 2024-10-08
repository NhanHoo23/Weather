package fpoly.nhanhhph47395.weather.screens;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;

import fpoly.nhanhhph47395.weather.R;
import fpoly.nhanhhph47395.weather.models.WeatherResponse;
import fpoly.nhanhhph47395.weather.screens.MainActivity;
import fpoly.nhanhhph47395.weather.screens.SplashActivity;
import fpoly.nhanhhph47395.weather.utils.AppManager;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        WeatherResponse weatherResponse = AppManager.shared(context).getWeatherResponse();
        int notificationId = intent.getIntExtra("notification_id", 0);

        if (weatherResponse != null) {
            String notiTitle = notificationId == 0 ? context.getString(R.string.notiTitleToday) : context.getString(R.string.notiTitleTomorrow);
            String title = notiTitle + " " + weatherResponse.location.name;
            int day = notificationId == 0 ? 0 : 1;
            String message = weatherResponse.forecast.forecastday.get(day).day.condition.text;

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            String channelId = "default_channel_id";
            String channelName = "Default Channel";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            Intent notificationIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification notification = new NotificationCompat.Builder(context, channelId)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_notification)
                    .build();

            notificationManager.notify(notificationId, notification);
        }
    }
}

