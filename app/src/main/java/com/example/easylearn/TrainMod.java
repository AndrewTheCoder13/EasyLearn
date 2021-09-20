package com.example.easylearn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
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
import com.example.easylearn.InternalUtilits.StaticUtils;
import com.example.easylearn.InternalUtilits.Words;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

public class TrainMod extends AppCompatActivity {

    private int screenWidth, screenHeight;
    private ImageView next;
    private EditText translateText;
    private TextView wordText;
    private Button check;
    private String translate;
    private String wordName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_mod);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        findViews();
        getScreenSize();
        viewSettings();
        makeViewSettings();
        setFonts();
        addActionListeners();
    }

    private void findViews() {
        next = findViewById(R.id.next);
        translateText = findViewById(R.id.translateText);
        wordText = findViewById(R.id.wordText);
        check = findViewById(R.id.check);
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
        if (Dictionary.getFullList() == null){
            LoadClass.load(getApplicationContext());
        }
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
                        wordText.setText("Чтобы использовать данный режим, нужно добавить хотя бы одно слово.");
                        check.setVisibility(View.INVISIBLE);
                        next.setVisibility(View.INVISIBLE);
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
                        wordText.setText("Чтобы использовать данный режим, нужно добавить хотя бы одно слово.");
                        check.setVisibility(View.INVISIBLE);
                        next.setVisibility(View.INVISIBLE);
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
                        wordText.setText("Чтобы использовать данный режим, нужно добавить хотя бы одно слово.");
                        check.setVisibility(View.INVISIBLE);
                        next.setVisibility(View.INVISIBLE);
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
        next.setVisibility(View.INVISIBLE);
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
        wordText.setText(keys.get(0).equalsIgnoreCase(randomKey)? keys.get(1) : keys.get(0));
    }

    private void addActionListeners() {
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check.setClickable(false);
                next.setVisibility(View.VISIBLE);
                String written = translateText.getText().toString();
                if(written.trim().equalsIgnoreCase(translate.trim())){
                    translateText.setTextColor(Color.GREEN);
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
                    translateText.setTextColor(Color.RED);
                    translateText.setText(translateText.getText().append(" - " + translate));
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check.setClickable(true);
                next.setVisibility(View.INVISIBLE);
                translateText.setTextColor(getResources().getColor(R.color.main_color_red));
                makeViewSettings();
                translateText.setText("");
            }
        });
    }

    private void setFonts() {
        translateText.setTypeface(StaticUtils.getTypeFace(getApplicationContext()));
        wordText.setTextSize((float) (screenWidth * 0.03));
        wordText.setTypeface(StaticUtils.getTypeFace(getApplicationContext()));
    }

}