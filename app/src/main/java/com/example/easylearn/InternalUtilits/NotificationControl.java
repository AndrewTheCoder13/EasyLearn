package com.example.easylearn.InternalUtilits;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.WindowManager;

import com.example.easylearn.R;
import com.example.easylearn.WordScreen;

import java.util.Calendar;
import java.util.Random;

public class NotificationControl extends BroadcastReceiver {
    public static String NOTIFICATION_ID = "notification_id";
    public static String NOTIFICATION = "notification";

    @Override
    public void onReceive(Context context, Intent intent) {
        Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(VibrationEffect.createOneShot(
                2000,
                VibrationEffect.DEFAULT_AMPLITUDE
        ));
        Settings.getSettings(context);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int notificationId = intent.getIntExtra(NOTIFICATION_ID, 160);
        notificationManager.notify(notificationId, notification);
        if(Settings.isNotificationsStatus()) {
            System.out.println("IS WORKING!");
            scheduleNotification(160, context);
            Intent openIntent = new Intent(context.getApplicationContext(), WordScreen.class);
            openIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.getApplicationContext().startActivity(openIntent);
        }
    }

    public void scheduleNotification(int notificationId, Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        AlarmManager mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = getNotificationTime();
        createNotificationChannel(notificationManager);
        Notification notification = createNotification(context);
        setAlarm(notificationId, notification, mAlarmManager, calendar, context);
    }

    private Calendar getNotificationTime() {
        int time = Integer.valueOf(com.example.easylearn.InternalUtilits.Settings.getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, time);
        return calendar;
    }

    private void createNotificationChannel(NotificationManager notificationManager) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("ELA", "EasyLearnApp",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("EasyLearnApp");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private Notification createNotification(Context context){
        Notification.Builder builder = new Notification.Builder(context, "ELA");
        Intent intent = new Intent(context, WordScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.icon, 10)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon))
                .setTicker("Привет! Проверь уведомление!")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentText("Когда будет возможность, не забывай учить слова!")
                .setContentTitle("Напоминание");
        Notification notification = builder.build();
        return notification;
    }

    private void setAlarm(int notificationId, Notification notification, AlarmManager mAlarmManager, Calendar calendar, Context context){
        Intent nIntent = new Intent(context, NotificationControl.class);
        nIntent.putExtra(NotificationControl.NOTIFICATION_ID, notificationId);
        nIntent.putExtra(NotificationControl.NOTIFICATION, notification);
        nIntent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        PendingIntent pendingIntentSecond = PendingIntent.getBroadcast(context, 0, nIntent, 0);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            mAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntentSecond);
        else if (Build.VERSION.SDK_INT > 19) {
            mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntentSecond);
        } else
            mAlarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntentSecond);*/
        mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntentSecond);
    }
}
