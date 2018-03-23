package com.openclassrooms.moodtracker;

import android.content.SharedPreferences;

/**
 * Created by berenger on 23/03/2018.
 */

public class MoodManager {
    private static final MoodManager ourInstance = new MoodManager();

    public static MoodManager getInstance() {
        return ourInstance;
    }

    private MoodManager() {}

    public void saveMoodToPrefs(SharedPreferences prefsFile, int dayIndex, int mood){
        prefsFile.edit().putInt("moodOfDay"+dayIndex, mood).apply();
    }

    public int getMoodFromPrefs(SharedPreferences prefsFile, int dayIndex){
        return prefsFile.getInt("moodOfDay"+dayIndex, 3);
    }

    public void saveCommentToPrefs(SharedPreferences prefsFile, int dayIndex, String comment){
        prefsFile.edit().putString("commentOfDay"+dayIndex, comment).apply();
    }

    public String getCommentFromPrefs(SharedPreferences prefsFile, int dayIndex){
        return prefsFile.getString("commentOfDay"+dayIndex, "");
    }

    public void updateWeeklyMoods(SharedPreferences prefsFile, int betweenDays){

        int [] weeklyMoods = new int [8];
        String [] weeklyComments = new String [8];

        //Get Last Saved DailyMoods and DailyComments
        for(int i = 0; i < weeklyMoods.length; i++){
            weeklyMoods[i] = getMoodFromPrefs(prefsFile, i);
            weeklyComments[i] = getCommentFromPrefs(prefsFile, i);
        }

        //Move every dailyMoods and dailyComments up one day
        for(int i = 7; i > 0; i--){
            if ((i-betweenDays) >= 0){
                weeklyMoods[i] = weeklyMoods[i-betweenDays];
                weeklyComments[i] = weeklyComments[i-betweenDays];
            }else{
                weeklyMoods[i] = 3;
                weeklyComments[i] = "";
            }
        }

        //Save updated weeklyMoods
        for(int i = 1; i < weeklyMoods.length; i++) {
            saveMoodToPrefs(prefsFile, i, weeklyMoods[i]);
            saveCommentToPrefs(prefsFile, i, weeklyComments[i]);
        }

        //Renew Today Mood to default value
        saveMoodToPrefs(prefsFile, 0, 3);
        saveCommentToPrefs(prefsFile, 0, "");
    }


}
