package com.example.easylearn.InternalUtilits;

import android.content.Context;
import android.content.SharedPreferences;

public class LoadClass {

    public static final String APP_PREFERENCES = "mysettings";

    public static void load(Context context){
        SharedPreferences mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        String words = mSettings.getString("Words", "");
        Dictionary.loadWords(words);
    }

}
