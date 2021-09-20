package com.example.easylearn.InternalUtilits;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;

public class Dictionary {

    private static final int DEFAULT_LEVEL = 0;
    private static TreeMap<String, Words> fullList;
    private static TreeMap<String, Words> newWords;
    private static TreeMap<String, Words> badlyKnownWords;
    private static TreeMap<String, Words> wellKnownWords;
    public static final String APP_PREFERENCES = "mysettings";

    public static void loadWords(String words) {
        fullList = new TreeMap<>();
        newWords = new TreeMap<>();
        badlyKnownWords = new TreeMap<>();
        wellKnownWords = new TreeMap<>();
        try {
            JSONParser jsonParser = new JSONParser();
            JSONArray jsonArray = (JSONArray) jsonParser.parse(words);
            jsonArray.forEach(wordLink -> {
                JSONObject needObject = (JSONObject) wordLink;
                String type = (String) needObject.get("type");
                String nativeWord = (String) needObject.get("native");
                String english = (String) needObject.get("english");
                long level = (long) needObject.get("level");
                Words word = new Words(nativeWord, english, (int) level);
                switch (type) {
                    case "new": {
                        fullList.put(nativeWord, word);
                        newWords.put(nativeWord, word);
                    }
                    break;
                    case "bad": {
                        fullList.put(nativeWord, word);
                        badlyKnownWords.put(nativeWord, word);
                    }
                    break;
                    case "well": {
                        fullList.put(nativeWord, word);
                        wellKnownWords.put(nativeWord, word);
                    }
                    break;
                }
            });
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public static void addWord(String nativeWord, String english, Context context) {
        Words word = new Words(nativeWord, english, DEFAULT_LEVEL);
        fullList.put(word.getNativeLanguage(), word);
        newWords.put(word.getNativeLanguage(), word);
        Dictionary.saveWords(context);
    }

    public static void delete(String word, Context context) {
        fullList.remove(word);
        newWords.remove(word);
        badlyKnownWords.remove(word);
        wellKnownWords.remove(word);
        Dictionary.saveWords(context);
    }

    public static void changeWordLevel(String word, boolean increase, Context context) {
        Words words = fullList.get(word);
        int level = words.getLevel();
        if (increase) {
            level++;
            words.setLevel(level);
            if (level > 3) {
                newWords.remove(word);
                wellKnownWords.put(word, words);
            } else if (level > -1) {
                badlyKnownWords.remove(word);
                newWords.put(word, words);
            }
        } else {
            level--;
            words.setLevel(level);
            if (level < -2) {
                newWords.remove(word);
                badlyKnownWords.put(word, words);
            } else if (level < 1) {
                wellKnownWords.remove(word);
                newWords.put(word, words);
            }
        }
        Dictionary.saveWords(context);
    }

    public static void saveWords(Context context) {
        JSONArray array = new JSONArray();
        for (String key : fullList.keySet()) {
            Words words = fullList.get(key);
            String type = words.getLevel() > 3 ? "well" : words.getLevel() <-2? "bad" : "new";
            JSONObject need = new JSONObject();
            need.put("native", words.getNativeLanguage());
            need.put("english", words.getEnglish());
            need.put("level", words.getLevel());
            need.put("type", type);
            array.add(need);
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement je = JsonParser.parseString(array.toJSONString());
        SharedPreferences mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString("Words", gson.toJson(je));
        editor.apply();
    }

    public static TreeMap<String, Words> getFullList() {
        return fullList;
    }

    public static TreeMap<String, Words> getNewWords() {
        return newWords;
    }

    public static TreeMap<String, Words> getBadlyKnownWords() {
        return badlyKnownWords;
    }

    public static TreeMap<String, Words> getWellKnownWords() {
        return wellKnownWords;
    }

}
