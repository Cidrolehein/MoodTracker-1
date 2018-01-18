package com.openclassrooms.moodtracker.Controllers.Activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import com.openclassrooms.moodtracker.R;

public class HistoryActivity extends AppCompatActivity {

    private int [] viewSize = {250, 500, 750, 1000, 1250};

    private String [] dayCount = {"Il y a une semaine", "Il y a 6 jours", "Il y a 5 jours",
            "Il y a 4 jours", "Il y a 3 jours", "Avant-hier", "Hier"};

    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mPreferences = getSharedPreferences("dailyMoods", MODE_PRIVATE);

        updateDailyMoods(mPreferences, 2);

        this.configureTextViews();
    }

    private void configureTextViews(){
        TextView [] textViews = {(TextView) findViewById(R.id.activity_history_text_view_1),
                                 (TextView) findViewById(R.id.activity_history_text_view_2),
                                 (TextView) findViewById(R.id.activity_history_text_view_3),
                                 (TextView) findViewById(R.id.activity_history_text_view_4),
                                 (TextView) findViewById(R.id.activity_history_text_view_5),
                                 (TextView) findViewById(R.id.activity_history_text_view_6),
                                 (TextView) findViewById(R.id.activity_history_text_view_7)};

        for (int i = 0; i < textViews.length; i++){
            setTextView(textViews[i], i);
        }
    }

    private void setTextView(TextView textView, int i){

        int dailyMood = getDailyMood(mPreferences, i);

        textView.setText(dayCount[i]);
        textView.setTextSize(20);
        textView.setPadding(10,0,0,0);

        textView.setBackgroundColor(getResources().getIntArray(R.array.colorPagesViewPager)[dailyMood]);
        textView.getLayoutParams().width = viewSize[dailyMood];
    }

    public int getDailyMood(SharedPreferences prefsFile, int numDay) {
        int dailyMood = prefsFile.getInt("day"+numDay, 4);
        return dailyMood;
    }

    public void setDailyMood(SharedPreferences prefsFile, int numDay, int dailyMood) {
        prefsFile.edit().putInt("day"+numDay, dailyMood).apply();
    }

    public void updateDailyMoods(SharedPreferences prefsFile, int yesterdayMood){

        int [] dailyMoods = new int [7];

        //Get Last Saved DailyMoods
        for(int i = 0; i < dailyMoods.length; i++){
            dailyMoods[i] = getDailyMood(prefsFile, i);
        }

        //Move every dailyMoods up one day  (3days ago's Mood -> 4days ago's Mood)
        for(int i = 6; i > 0; i--){
            dailyMoods[i] = dailyMoods[i-1];
        }
        dailyMoods[0] = yesterdayMood;

        //Save updated dailyMoods
        for(int i = 0; i < dailyMoods.length; i++) {
            prefsFile.edit().putInt("day" + i, dailyMoods[i]).apply();
        }
    }
}