package com.openclassrooms.moodtracker.Models;

import android.content.SharedPreferences;

import java.util.Date;

/**
 * Created by berenger on 18/01/2018.
 */

public class WeeklyMoods {

    public int getDailyMood(SharedPreferences prefsFile, int numDay) {
        int dailyMood = prefsFile.getInt("day"+numDay, 3);
        return dailyMood;
    }

    public void setDailyMood(SharedPreferences prefsFile, int numDay, int dailyMood) {
        prefsFile.edit().putInt("day"+numDay, dailyMood).apply();
    }

    public void updateWeeklyMoods(SharedPreferences prefsFile){

        int [] weeklyMoods = new int [8];

        //Get Last Saved DailyMoods
        for(int i = 0; i < weeklyMoods.length; i++){
            weeklyMoods[i] = getDailyMood(prefsFile, i);
        }

        //Move every dailyMoods up one day  (3days ago's Mood -> 4days ago's Mood)
        for(int i = 7; i > 0; i--){
            weeklyMoods[i] = weeklyMoods[i-1];
        }

        //Save updated weeklyMoods
        for(int i = 1; i < (weeklyMoods.length - 1); i++) {
            setDailyMood(prefsFile, i, weeklyMoods[i]);
        }

        //Renew Today Mood to default value
        setDailyMood(prefsFile, 0, 3);
    }
}
