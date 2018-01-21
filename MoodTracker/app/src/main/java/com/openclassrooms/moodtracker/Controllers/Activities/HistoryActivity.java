package com.openclassrooms.moodtracker.Controllers.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

        FrameLayout frameLayout1 = (FrameLayout) findViewById(R.id.activity_history_framelayout1);
        TextView textView1 = (TextView) findViewById(R.id.activity_history_textview1);
        ImageButton imageButton1 = (ImageButton) findViewById(R.id.activity_history_comment_btn1);

        configureFrameLayout(frameLayout1, 1);
        setTextView(textView1, 1);
        configureCommentButton(imageButton1, 1);

        FrameLayout frameLayout2 = (FrameLayout) findViewById(R.id.activity_history_framelayout2);
        TextView textView2 = (TextView) findViewById(R.id.activity_history_textview2);
        ImageButton imageButton2 = (ImageButton) findViewById(R.id.activity_history_comment_btn2);

        configureFrameLayout(frameLayout2, 2);
        setTextView(textView2, 2);
        configureCommentButton(imageButton2, 2);
    }

    /*
    private void configureTextViews(){
        //Set all TextViews
        for (int i = 0; i < frameLayouts.length + 1; i++){
            setFrameLayout(frameLayouts[i], i);
        }
    }*/

    private void configureFrameLayout(FrameLayout frameLayout, int numDay){
        int dailyMood = mWeeklyMoods.getDailyMood(mPreferences, numDay);
        frameLayout.setBackgroundColor(getResources().getIntArray(R.array.colorPagesViewPager)[dailyMood]);
        frameLayout.getLayoutParams().width = viewSize[dailyMood];
    }

    private void setTextView(TextView textView, int numDay){
        textView.setText(getResources().getStringArray(R.array.dayCount)[numDay-1]);
    }

    private void configureCommentButton(ImageButton imgbtn, final int numDay){
        //Set Comment button if comment exists
        if(!mWeeklyMoods.getDailyComment(mPreferences, numDay).equals("")){
            //Show button + if click on button, show Comment (Toast)
            imgbtn.setVisibility(ImageButton.VISIBLE);
            imgbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(HistoryActivity.this, "Le message du jour" + numDay + "était blabla", Toast.LENGTH_LONG).show();
                }   });
        }else{
            imgbtn.setVisibility(ImageButton.INVISIBLE);
        }
    }

}