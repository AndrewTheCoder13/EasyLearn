package com.example.easylearn;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easylearn.InternalUtilits.Dictionary;
import com.example.easylearn.InternalUtilits.StaticUtils;
import com.example.easylearn.InternalUtilits.Words;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.TreeMap;

public class HomePage extends AppCompatActivity {

    private ImageView homeLogo, set;
    private FloatingActionButton addWordButton;
    private TextView yourDictionary;
    private int screenWidth, screenHeight;
    private LinearLayout mainLayout;
    private Button learnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        findViews();
        getScreenSize();
        viewSettings();
        addActionListeners();
        setFonts();
        addWords();
        makeWhiteList();
    }

    private void findViews() {
        homeLogo = findViewById(R.id.homeLogo);
        addWordButton = findViewById(R.id.addWordButton);
        yourDictionary = findViewById(R.id.yourDictionary);
        mainLayout = findViewById(R.id.mainLayout);
        set = findViewById(R.id.set);
        learnButton = findViewById(R.id.learnButton);
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
        addWordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transit();
            }
        });
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSetting();
            }
        });
        learnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.TRAIN");
                startActivity(intent);
            }
        });
    }

    private void setFonts() {
        yourDictionary.setTypeface(StaticUtils.getTypeFace(getApplicationContext()));
        yourDictionary.setTextSize((float) (screenWidth * 0.03));
    }

    private void addWords() {
        TreeMap<String, Words> wordsTreeMap = Dictionary.getFullList();
        wordsTreeMap.forEach((s, w) -> {
            LinearLayout layout = new LinearLayout(mainLayout.getContext());
            layout.setBackgroundResource(R.drawable.layout_bg);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setGravity(Gravity.CENTER_VERTICAL);
            android.view.ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) (screenWidth * 0.9), 100);

            layout.setLayoutParams(layoutParams);

            TextView textView = new TextView(this);
            if (s.length() > 9) {
                textView.setText(s.substring(0, 9) + "...");
            } else {
                textView.setText(s);
            }
            textView.setTypeface(StaticUtils.getTypeFace(getApplicationContext()));
            textView.setTextSize((float) (screenWidth * 0.02));
            textView.setTextColor(Color.BLACK);

            Space space = new Space(layout.getContext());
            space.setMinimumWidth(100);
            layout.addView(space, 0);
            layout.addView(textView, 1);
            space = new Space(layout.getContext());
            if (s.length() > 9) {
                space.setMinimumWidth((int) (screenWidth * 0.12));
            } else {
                space.setMinimumWidth((int) (screenWidth * 0.25));
            }
            layout.addView(space, 2);

            ImageView edit = new ImageView(layout.getContext());
            edit.setImageResource(R.drawable.glaz);
            layoutParams = new LinearLayout.LayoutParams(100, 100);
            edit.setLayoutParams(layoutParams);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String word = textView.getText().toString();
                    word = word.replace(".", "");
                    if (w.getNativeLanguage().contains(word)) {
                        if (w.getEnglish().length() > 9) {
                            textView.setText(w.getEnglish().substring(0, 9) + "...");
                        } else {
                            textView.setText(w.getEnglish());
                        }
                    } else {
                        if (w.getNativeLanguage().length() > 9) {
                            textView.setText(w.getNativeLanguage().substring(0, 9) + "...");
                        } else {
                            textView.setText(w.getNativeLanguage());
                        }
                    }
                }
            });
            layout.addView(edit, 3);

            space = new Space(layout.getContext());
            space.setMinimumWidth(100);
            layout.addView(space, 4);

            ImageView delete = new ImageView(layout.getContext());
            delete.setImageResource(R.drawable.galochkaznak);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAlertDialog(w, layout);
                }
            });
            layoutParams = new LinearLayout.LayoutParams(100, 100);
            delete.setLayoutParams(layoutParams);
            layout.addView(delete, 5);
            mainLayout.addView(layout);
        });
    }

    private void showAlertDialog(Words w, LinearLayout layout) {
        int level = w.getLevel();
        String message = level >= 3 ? "Убрать слово из списка для изучения? По оценке программы, вы уже достаточно хорошо выучили слово." : level < -2 ? "Убрать слово из списка для изучения? По анализу программы, вы выучили данное слово еще недостаточно хорошо." :
                "Убрать слово из списка для изучения? Оно было добавленно недавно и недостаточно данных, для оценки уровня знания данного слово.";
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Слово выученно?")
                .setMessage(message)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Dictionary.delete(w.getNativeLanguage(), getApplicationContext());
                        mainLayout.removeView(layout);
                    }
                })
                .create();
        alertDialog.show();
    }

    private void transit() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent(this, AddWord.class);
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(this, homeLogo, "Logo");
            Bundle bundle = options.toBundle();
            startActivity(intent, options.toBundle());
        } else {
            Intent intent = new Intent("android.intent.action.ADD_PAGE");
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Выйти из приложения?")
                .setMessage("Вы действительно хотите выйти?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                })
                .create()
                .show();
    }

    public void startSetting() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent(this, Settings.class);
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(this, set, "Set");
            Bundle bundle = options.toBundle();
            startActivity(intent, options.toBundle());
        } else {
            Intent intent = new Intent("android.intent.action.SET");
            startActivity(intent);
        }
    }

    private void makeWhiteList() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent openIntent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                openIntent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                openIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                openIntent.setData(Uri.parse("package:" + packageName));
                getApplicationContext().startActivity(openIntent);
            }
        }
    }
}