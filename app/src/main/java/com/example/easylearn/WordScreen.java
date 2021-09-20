package com.example.easylearn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.easylearn.InternalUtilits.Dictionary;
import com.example.easylearn.InternalUtilits.LoadClass;
import com.example.easylearn.InternalUtilits.Settings;
import com.example.easylearn.InternalUtilits.StaticUtils;
import com.example.easylearn.InternalUtilits.Words;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

public class WordScreen extends AppCompatActivity {

    private int screenWidth, screenHeight;
    private ImageView next2;
    private EditText translateText2;
    private TextView wordText2;
    private Button check2, finish;
    private String translate;
    private String wordName;
    private int currentCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("I'm working too!");
        setContentView(R.layout.activity_word_screen);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        LoadClass.load(getApplicationContext());
        findViews();
        getScreenSize();
        viewSettings();
        makeViewSettings();
        setFonts();
        addActionListeners();
    }

    private void findViews() {
        next2 = findViewById(R.id.next2);
        translateText2 = findViewById(R.id.translateText2);
        wordText2 = findViewById(R.id.wordText2);
        check2 = findViewById(R.id.check2);
        finish = findViewById(R.id.finish);
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

    private void makeViewSettings() {
        double random = Math.random();
        if (random < 0.5) {
            TreeMap<String, Words> words = Dictionary.getBadlyKnownWords();
            int size = words.size();
            if (size == 0) {
                words = Dictionary.getNewWords();
                size = words.size();
                if (size == 0) {
                    words = Dictionary.getWellKnownWords();
                    size = words.size();
                    if (size == 0) {
                        wordText2.setText("Чтобы использовать данный режим, нужно добавить хотя бы одно слово.");
                        check2.setVisibility(View.INVISIBLE);
                        next2.setVisibility(View.INVISIBLE);
                    } else {
                        fillMainPanel(words);
                    }
                } else {
                    fillMainPanel(words);
                }
            } else {
                fillMainPanel(words);
            }
        } else if (random < 0.8) {
            TreeMap<String, Words> words = Dictionary.getNewWords();
            int size = words.size();
            if (size == 0) {
                words = Dictionary.getBadlyKnownWords();
                size = words.size();
                if (size == 0) {
                    words = Dictionary.getWellKnownWords();
                    size = words.size();
                    if (size == 0) {
                        wordText2.setText("Чтобы использовать данный режим, нужно добавить хотя бы одно слово.");
                        check2.setVisibility(View.INVISIBLE);
                        next2.setVisibility(View.INVISIBLE);
                    } else {
                        fillMainPanel(words);
                    }
                } else {
                    fillMainPanel(words);
                }
            } else {
                fillMainPanel(words);
            }
        } else {
            TreeMap<String, Words> words = Dictionary.getWellKnownWords();
            int size = words.size();
            if (size == 0) {
                words = Dictionary.getBadlyKnownWords();
                size = words.size();
                if (size == 0) {
                    words = Dictionary.getNewWords();
                    size = words.size();
                    if (size == 0) {
                        wordText2.setText("Чтобы использовать данный режим, нужно добавить хотя бы одно слово.");
                        check2.setVisibility(View.INVISIBLE);
                        next2.setVisibility(View.INVISIBLE);
                    } else {
                        fillMainPanel(words);
                    }
                } else {
                    fillMainPanel(words);
                }
            } else {
                fillMainPanel(words);
            }
        }
    }

    private void fillMainPanel(TreeMap<String, Words> words){
        finish.setVisibility(View.INVISIBLE);
        next2.setVisibility(View.INVISIBLE);
        finish.setClickable(false);
        finish.setFocusable(false);
        finish.setFocusableInTouchMode(false);

        Random randomGenerator = new Random();
        List<String> keys = new ArrayList<String>(words.keySet());
        String randomKey = keys.get( randomGenerator.nextInt(keys.size()));
        Words word = words.get(randomKey);
        String one = word.getNativeLanguage();
        String two = word.getEnglish();
        keys = new ArrayList<String>();
        keys.add(one);
        keys.add(two);
        randomKey = keys.get( randomGenerator.nextInt(keys.size()));
        translate = randomKey;
        wordName = keys.get(0).equalsIgnoreCase(randomKey)? keys.get(1) : keys.get(0);
        wordText2.setText(keys.get(0).equalsIgnoreCase(randomKey)? keys.get(1) : keys.get(0));
    }

    private void addActionListeners() {
        check2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check2.setClickable(false);
                String written = translateText2.getText().toString();
                if(written.trim().equalsIgnoreCase(translate.trim())){
                    translateText2.setTextColor(Color.GREEN);
                    translateText2.setClickable(false);
                    translateText2.setCursorVisible(false);
                    translateText2.setFocusable(false);
                    translateText2.setFocusableInTouchMode(false);
                    check2.setClickable(false);
                    check2.setFocusable(false);
                    check2.setFocusableInTouchMode(false);
                    if(Dictionary.getFullList().containsKey(translate)) {
                        Dictionary.changeWordLevel(translate, true, getApplicationContext());
                    } else {
                        Dictionary.changeWordLevel(wordName, true, getApplicationContext());
                    }
                } else {
                    if(Dictionary.getFullList().containsKey(translate)) {
                        Dictionary.changeWordLevel(translate, false, getApplicationContext());
                    } else {
                        Dictionary.changeWordLevel(wordName, false, getApplicationContext());
                    }
                    translateText2.setTextColor(Color.RED);
                    translateText2.setText(translateText2.getText().append(" - " + translate));
                }
                Settings.getSettings(getApplicationContext());
                int count = Settings.getCount();
                if(currentCount < count){
                    currentCount++;
                    next2.setVisibility(View.VISIBLE);
                } else {
                    finish.setVisibility(View.VISIBLE);
                    next2.setVisibility(View.INVISIBLE);
                    check2.setVisibility(View.INVISIBLE);
                    next2.setClickable(false);
                    next2.setFocusable(false);
                    next2.setFocusableInTouchMode(false);
                    check2.setClickable(false);
                    check2.setFocusable(false);
                    check2.setFocusableInTouchMode(false);
                    finish.setClickable(true);
                    finish.setFocusable(true);
                    finish.setFocusableInTouchMode(true);
                }
            }
        });
        next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check2.setClickable(true);
                translateText2.setTextColor(Color.GREEN);
                translateText2.setClickable(true);
                translateText2.setCursorVisible(true);
                translateText2.setFocusable(true);
                translateText2.setFocusableInTouchMode(true);
                next2.setClickable(true);
                next2.setFocusable(true);
                next2.setFocusableInTouchMode(true);
                check2.setClickable(true);
                check2.setFocusable(true);
                check2.setFocusableInTouchMode(true);
                next2.setVisibility(View.INVISIBLE);
                translateText2.setTextColor(getResources().getColor(R.color.main_color_red));
                makeViewSettings();
                translateText2.setText("");
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveTaskToBack(false);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
    }

    private void setFonts() {
        translateText2.setTypeface(StaticUtils.getTypeFace(getApplicationContext()));
        wordText2.setTextSize((float) (screenWidth * 0.03));
        wordText2.setTypeface(StaticUtils.getTypeFace(getApplicationContext()));
    }
}