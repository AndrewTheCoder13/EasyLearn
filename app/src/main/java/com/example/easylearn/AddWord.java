package com.example.easylearn;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.easylearn.InternalUtilits.StaticUtils;

public class AddWord extends AppCompatActivity {

    private ImageView addLogo;
    private TextView addWordText, word, translate;
    private EditText getWord, getTranslate;
    private int screenWidth, screenHeight;
    private Button saveButton;
    private ConstraintLayout wordLayout, translateLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        findViews();
        getScreenSize();
        viewSettings();
        addActionListeners();
        setFonts();
        setSizes();
    }

    private void findViews(){
        addLogo = findViewById(R.id.addLogo);
        addWordText = findViewById(R.id.addWordText);
        saveButton = findViewById(R.id.saveButton);
        wordLayout = findViewById(R.id.wordLayout);
        translateLayout = findViewById(R.id.translateLayout);
        word = findViewById(R.id.word);
        getWord = findViewById(R.id.getWord);
        translate = findViewById(R.id.translate);
        getTranslate = findViewById(R.id.getTranslate);
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

    private void addActionListeners(){
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = getWord.getText().toString();
                if((!getWord.getText().toString().equals("")) && (!getTranslate.getText().toString().equals(""))) {
                    Dictionary.addWord(getWord.getText().toString().trim(), getTranslate.getText().toString().trim(), getApplicationContext());
                    transit();
                } else {
                    showAlertDialog("Неверные данные!", "Правильно введите слово и перевод!");
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

    private void setFonts(){
        addWordText.setTypeface(StaticUtils.getTypeFace(getApplicationContext()));
        addWordText.setTextSize((float) (screenWidth * 0.03));
        word.setTypeface(StaticUtils.getTypeFace(getApplicationContext()));
        word.setTextSize((float) (screenWidth * 0.02));
        translate.setTypeface(StaticUtils.getTypeFace(getApplicationContext()));
        translate.setTextSize((float) (screenWidth * 0.02));
        getWord.setTypeface(StaticUtils.getTypeFace(getApplicationContext()));
        getWord.setTextSize((float) (screenWidth * 0.02));
        getTranslate.setTypeface(StaticUtils.getTypeFace(getApplicationContext()));
        getTranslate.setTextSize((float) (screenWidth * 0.02));
    }

    private void setSizes(){
        android.view.ViewGroup.LayoutParams layoutParams = getWord.getLayoutParams();
        layoutParams.width = (int) (screenWidth * 0.55);
        layoutParams.height = (int) (screenHeight * 0.07);
        getWord.setLayoutParams(layoutParams);
        layoutParams = getTranslate.getLayoutParams();
        layoutParams.width = (int) (screenWidth * 0.55);
        layoutParams.height = (int) (screenHeight * 0.07);
        getTranslate.setLayoutParams(layoutParams);
    }

    private void transit(){
        // Check if we're running on Android 5.0 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent(this, HomePage.class);
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(this, addLogo, "Logo");
            Bundle bundle = options.toBundle();
            startActivity(intent, options.toBundle());
        } else {
            Intent intent = new Intent("android.intent.action.HOME_PAGE");
            startActivity(intent);
        }
    }


}