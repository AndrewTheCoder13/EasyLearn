package com.example.easylearn.InternalUtilits;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {
    private static boolean notificationsStatus;
    private static String interval;
    private static int count;
    public static final String APP_PREFERENCES = "mysettings";


    public static int getCount() {
        return count;
    }

    public static void setCount(int count, Context applicationContext) {
        Settings.count = count;
        SharedPreferences mSettings = applicationContext.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt("count", count);
        editor.apply();
    }

    public static boolean isNotificationsStatus() {
        return notificationsStatus;
    }

    public static void setNotificationsStatus(boolean notificationsStatus2, Context applicationContext) {
        notificationsStatus = notificationsStatus2;
        SharedPreferences mSettings = applicationContext.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean("nStatus", notificationsStatus);
        editor.apply();
    }
    public static String getTime() {
        return interval;
    }

    public static void setTime(String time2, Context applicationContext) {
        interval = time2;
        SharedPreferences mSettings = applicationContext.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString("nTime", interval);
        editor.apply();
    }

    public static void getSettings(Context applicationContext) {
        SharedPreferences mSettings = applicationContext.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        notificationsStatus = mSettings.getBoolean("nStatus", false);
        interval = mSettings.getString("nTime", "15");
        count = mSettings.getInt("count", 1);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean("nStatus", notificationsStatus);
        editor.putString("nTime", interval);
        editor.apply();
    }

    public static void saveSettings(Context applicationContext){
        SharedPreferences mSettings = applicationContext.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean("nStatus", notificationsStatus);
        editor.putString("nTime", interval);
        editor.putInt("count", count);
        editor.apply();
    }
}
