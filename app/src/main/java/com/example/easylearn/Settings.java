package com.example.easylearn;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easylearn.InternalUtilits.NotificationControl;
import com.example.easylearn.InternalUtilits.StaticUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Random;

public class Settings extends AppCompatActivity {

    public static final String APP_PREFERENCES = "mysettings";
    private ImageView homeLogo2;
    private FloatingActionButton addWordButton2;
    private TextView settings, notificationSwitchText, notificationTimeText, countText;
    private int screenWidth, screenHeight;
    private LinearLayout mainLayout, notificationTimeLayout, count;
    private Button saveSetButton;
    private Switch notificationControl;
    private EditText editTextTime, getCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        findViews();
        getScreenSize();
        viewSettings();
        addActionListeners();
        setFonts();
        getSettings();
    }

    private void findViews() {
        homeLogo2 = findViewById(R.id.homeLogo2);
        addWordButton2 = findViewById(R.id.addWordButton2);
        settings = findViewById(R.id.settings);
        mainLayout = findViewById(R.id.mainLayout);
        saveSetButton = findViewById(R.id.saveSetButton);
        notificationSwitchText = findViewById(R.id.notificationSwitchText);
        notificationTimeText = findViewById(R.id.notificationTimeText);
        notificationControl = findViewById(R.id.notificationControl);
        editTextTime = findViewById(R.id.editTextTime);
        notificationTimeLayout = findViewById(R.id.notificationTimeLayout);
        count = findViewById(R.id.count);
        getCount = findViewById(R.id.getCount);
        countText = findViewById(R.id.countText);
    }

    private void getScreenSize() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenHeight = size.y;
        screenWidth = size.x;
    }

    private void viewSettings() {
        actionBarAndStatusBarSettings();
    }

    private void actionBarAndStatusBarSettings() {
        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_color_gold));
        }
    }

    private void addActionListeners() {
        addWordButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transit();
            }
        });
        saveSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        notificationControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.example.easylearn.InternalUtilits.Settings.setNotificationsStatus(notificationControl.isChecked(), getApplicationContext());
                if (!notificationControl.isChecked()) {
                    notificationTimeLayout.setAlpha((float) 0.5);
                    count.setAlpha((float) 0.5);
                    editTextTime.setClickable(false);
                    editTextTime.setCursorVisible(false);
                    editTextTime.setFocusable(false);
                    editTextTime.setFocusableInTouchMode(false);

                    getCount.setClickable(false);
                    getCount.setCursorVisible(false);
                    getCount.setFocusable(false);
                    getCount.setFocusableInTouchMode(false);
                } else {
                    notificationTimeLayout.setAlpha((float) 1);
                    count.setAlpha((float) 1);
                    editTextTime.setClickable(true);
                    editTextTime.setCursorVisible(true);
                    editTextTime.setFocusable(true);
                    editTextTime.setFocusableInTouchMode(true);

                    getCount.setClickable(true);
                    getCount.setCursorVisible(true);
                    getCount.setFocusable(true);
                    getCount.setFocusableInTouchMode(true);
                }
            }
        });
        saveSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.example.easylearn.InternalUtilits.Settings.setNotificationsStatus(notificationControl.isChecked(), getApplicationContext());
                if (notificationControl.isChecked()) {
                    int time = Integer.valueOf(editTextTime.getText().toString());
                    if ((time > 60) || (time < 0)) {
                        showAlertDialog("Неверные данные!", "Неправильно введено время!");
                    } else {
                        int count = Integer.parseInt(getCount.getText().toString());
                        if ((count > 60) || (count < 0)) {
                            showAlertDialog("Неверные данные!", "Неправильно введено время!");
                        } else {
                            com.example.easylearn.InternalUtilits.Settings.setTime(editTextTime.getText().toString(), getApplicationContext());
                            com.example.easylearn.InternalUtilits.Settings.setCount(count, getApplicationContext());
                            scheduleNotification(160);
                            Toast toast = Toast.makeText(getApplicationContext(), "Настройки сохранены", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Настройки сохранены", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void scheduleNotification(int notificationId) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        AlarmManager mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar = getNotificationTime();
        createNotificationChannel(notificationManager);
        Notification notification = createNotification();
        setAlarm(notificationId, notification, mAlarmManager, calendar);
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

    private Notification createNotification() {
        Notification.Builder builder = new Notification.Builder(this, "ELA");
        Intent intent = new Intent(this, WordScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.icon, 10)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.icon))
                .setTicker("Привет! Проверь уведомление!")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentText("Когда будет возможность, не забывай учить слова!")
                .setContentTitle("Напоминание");
        Notification notification = builder.build();
        return notification;
    }

    private void setAlarm(int notificationId, Notification notification, AlarmManager mAlarmManager, Calendar calendar) {

        Intent nIntent = new Intent(this, NotificationControl.class);
        nIntent.putExtra(NotificationControl.NOTIFICATION_ID, notificationId);
        nIntent.putExtra(NotificationControl.NOTIFICATION, notification);
        nIntent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        PendingIntent pendingIntentSecond = PendingIntent.getBroadcast(this, 0, nIntent, 0);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            mAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntentSecond);
        else if (Build.VERSION.SDK_INT > 19) {
            mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntentSecond);
        } else
            mAlarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntentSecond);*/
        mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntentSecond);
    }

    private void setFonts() {
        settings.setTypeface(StaticUtils.getTypeFace(getApplicationContext()));
        settings.setTextSize((float) (screenWidth * 0.03));
        notificationTimeText.setTypeface(StaticUtils.getTypeFace(getApplicationContext()));
        notificationTimeText.setTextSize((float) (screenWidth * 0.02));
        notificationSwitchText.setTypeface(StaticUtils.getTypeFace(getApplicationContext()));
        notificationSwitchText.setTextSize((float) (screenWidth * 0.02));
        countText.setTypeface(StaticUtils.getTypeFace(getApplicationContext()));
        countText.setTextSize((float) (screenWidth * 0.02));
    }

    private void transit() {
        // Check if we're running on Android 5.0 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent(this, AddWord.class);
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(this, homeLogo2, "Logo");
            Bundle bundle = options.toBundle();
            startActivity(intent, options.toBundle());
        } else {
            Intent intent = new Intent("android.intent.action.ADD_PAGE");
            startActivity(intent);
        }
    }

    private void getSettings() {
        boolean nStatus = com.example.easylearn.InternalUtilits.Settings.isNotificationsStatus();
        String time = com.example.easylearn.InternalUtilits.Settings.getTime();
        int countTime = com.example.easylearn.InternalUtilits.Settings.getCount();
        if (nStatus) {
            notificationControl.setChecked(true);
            editTextTime.setText(time);
            getCount.setText(Integer.toString(countTime));
        } else {
            editTextTime.setText(time);
            getCount.setText(Integer.toString(countTime));
            notificationControl.setChecked(false);
            notificationTimeLayout.setAlpha((float) 0.5);
            count.setAlpha((float) 0.5);
            getCount.setClickable(false);
            getCount.setCursorVisible(false);
            getCount.setFocusable(false);
            getCount.setFocusableInTouchMode(false);
            editTextTime.setClickable(false);
            editTextTime.setCursorVisible(false);
            editTextTime.setFocusable(false);
            editTextTime.setFocusableInTouchMode(false);
        }
    }
}