package com.example.easylearn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.PowerManager;
import android.os.SystemClock;
import android.transition.Explode;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.easylearn.InternalUtilits.LoadClass;
import com.example.easylearn.InternalUtilits.Settings;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    private ImageView logo;
    private ProgressBar progressBar;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        findViews();
        LoadClass.load(getApplicationContext());
        viewSettings();
        Settings.getSettings(getApplicationContext());
        progressBarInit();
    }

    private void findViews(){
        logo = findViewById(R.id.logo);
        progressBar = findViewById(R.id.progressBar);
    }

    private void progressBarInit(){
        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                counter++;
                progressBar.setProgress(counter);
                if(counter == 100){
                    timer.cancel();
                }
            }
        };
        timer.schedule(timerTask, 0, 10);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.nothingdoinganimation);
        logo.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                transit();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
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

    private void transit(){
        // Check if we're running on Android 5.0 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent(this, HomePage.class);
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(this, logo, "Logo");
            Bundle bundle = options.toBundle();
            startActivity(intent, options.toBundle());
        } else {
            Intent intent = new Intent("android.intent.action.HOME_PAGE");
            startActivity(intent);
        }
    }
}