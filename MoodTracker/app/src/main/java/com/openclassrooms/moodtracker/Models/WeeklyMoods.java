package com.openclassrooms.moodtracker.Models;

import android.content.SharedPreferences;

/**
 * Created by berenger on 18/01/2018.
 */

public class WeeklyMoods {

    public int getDailyMood(SharedPreferences prefsFile, int numDay) {
        int dailyMood = prefsFile.getInt("day"+numDay, 3);
        return dailyMood;
    }

    public String getDailyComment(SharedPreferences prefsFile, int numDay){
        String dailyComment = prefsFile.getString("comment"+numDay, "");
        return dailyComment;
    }

    public void setDailyMood(SharedPreferences prefsFile, int numDay, int dailyMood) {
        prefsFile.edit().putInt("day"+numDay, dailyMood).apply();
    }

    public void setDailyComment(SharedPreferences prefsFile, int numDay, String dailyComment){
        prefsFile.edit().putString("comment"+numDay, dailyComment).apply();
    }

    public void updateWeeklyMoods(SharedPreferences prefsFile){

        int [] weeklyMoods = new int [8];
        String [] weeklyComments = new String [8];

        //Get Last Saved DailyMoods and DailyComments
        for(int i = 0; i < weeklyMoods.length; i++){
            weeklyMoods[i] = getDailyMood(prefsFile, i);
            weeklyComments[i] = getDailyComment(prefsFile, i);
        }

        //Move every dailyMoods and dailyComments up one day
        for(int i = 7; i > 0; i--){
            weeklyMoods[i] = weeklyMoods[i-1];
            weeklyComments[i] = weeklyComments[i-1];
        }

        //Save updated weeklyMoods
        for(int i = 1; i < (weeklyMoods.length - 1); i++) {
            setDailyMood(prefsFile, i, weeklyMoods[i]);
            setDailyComment(prefsFile, i, weeklyComments[i]);
        }

        //Renew Today Mood to default value
        setDailyMood(prefsFile, 0, 3);
        setDailyComment(prefsFile, 0, "");
    }
}
