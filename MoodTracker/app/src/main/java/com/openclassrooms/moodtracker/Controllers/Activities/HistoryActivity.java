package com.openclassrooms.moodtracker.Controllers.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.openclassrooms.moodtracker.Models.WeeklyMoods;
import com.openclassrooms.moodtracker.R;

public class HistoryActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;

    private WeeklyMoods mWeeklyMoods = new WeeklyMoods();
    private int [] viewSize = {250, 500, 750, 1000, 1250};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mPreferences = getSharedPreferences("dailyMoods", MODE_PRIVATE);
        mWeeklyMoods.setDailyComment(mPreferences, 1, "Hier");

        this.configureTextViews();
    }

    private void configureTextViews(){
        //Serialize all TextViews
        TextView [] textViews = {(TextView) findViewById(R.id.activity_history_text_view_1),
                                 (TextView) findViewById(R.id.activity_history_text_view_2),
                                 (TextView) findViewById(R.id.activity_history_text_view_3),
                                 (TextView) findViewById(R.id.activity_history_text_view_4),
                                 (TextView) findViewById(R.id.activity_history_text_view_5),
                                 (TextView) findViewById(R.id.activity_history_text_view_6),
                                 (TextView) findViewById(R.id.activity_history_text_view_7)};

        ImageButton commentBtn = (ImageButton) findViewById(R.id.activity_history_comment_btn);

        //Set all TextViews
        for (int i = 1; i < textViews.length + 1; i++){
            setTextView(textViews[i-1], i);
        }
    }

    private void setTextView(TextView textView, int i){

        //set Text and Padding
        textView.setText(getResources().getStringArray(R.array.dayCount)[i-1]);
        textView.setTextSize(20);
        textView.setPadding(10,0,0,0);

        //Set BackgroundColor and width according to Day Mood
        int dailyMood = mWeeklyMoods.getDailyMood(mPreferences, i);

        textView.setBackgroundColor(getResources().getIntArray(R.array.colorPagesViewPager)[dailyMood]);
        textView.getLayoutParams().width = viewSize[dailyMood];

        //Set Comment icon if comment exists
        if(!mWeeklyMoods.getDailyComment(mPreferences, i).equals(""))
            //Show button + if clic on button, show Comment (Toast)
    }
}