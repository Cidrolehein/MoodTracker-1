package com.openclassrooms.moodtracker.Models;

import android.content.SharedPreferences;

/**
 * Created by berenger on 18/01/2018.
 */

public class WeeklyMoods {

    public int getDailyMood(SharedPreferences prefsFile, int numDay) {
        int dailyMood = prefsFile.getInt("day"+numDay, 4);
        return dailyMood;
    }

    public void setDailyMood(SharedPreferences prefsFile, int numDay, int dailyMood) {
        prefsFile.edit().putInt("day"+numDay, dailyMood).apply();
    }

    public void updateWeeklyMoods(SharedPreferences prefsFile, int yesterdayMood){

        int [] weeklyMoods = new int [7];

        //Get Last Saved DailyMoods
        for(int i = 0; i < weeklyMoods.length; i++){
            weeklyMoods[i] = getDailyMood(prefsFile, i);
        }

        //Move every dailyMoods up one day  (3days ago's Mood -> 4days ago's Mood)
        for(int i = 6; i > 0; i--){
            weeklyMoods[i] = weeklyMoods[i-1];
        }
        weeklyMoods[0] = yesterdayMood;

        //Save updated weeklyMoods
        for(int i = 0; i < weeklyMoods.length; i++) {
            setDailyMood(prefsFile, i, weeklyMoods[i]);
        }
    }
}
