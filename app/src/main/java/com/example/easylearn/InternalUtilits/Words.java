package com.example.easylearn.InternalUtilits;

public class Words {

    private int level;
    private String nativeLanguage;
    private String english;

    public Words(String nativeLanguage, String english, int level){
        this.nativeLanguage = nativeLanguage;
        this.english = english;
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getNativeLanguage() {
        return nativeLanguage;
    }

    public void setNativeLanguage(String nativeLanguage) {
        this.nativeLanguage = nativeLanguage;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }
}
