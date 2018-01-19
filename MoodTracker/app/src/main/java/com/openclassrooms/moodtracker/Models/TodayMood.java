package com.openclassrooms.moodtracker.Models;

import android.content.SharedPreferences;

/**
 * Created by berenger on 19/01/2018.
 */

public class TodayMood {

    public TodayMood() {
    }

    public int getTodayMood(SharedPreferences prefsFile){
        int todayMood = prefsFile.getInt("day"+0, 3);
        return todayMood;
    }

    public void setTodayMood(){

    }

}
