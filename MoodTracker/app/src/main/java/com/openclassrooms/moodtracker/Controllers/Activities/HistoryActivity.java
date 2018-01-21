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
        ImageButton commentButton1 = (ImageButton) findViewById(R.id.activity_history_comment_btn1);

        FrameLayout frameLayout2 = (FrameLayout) findViewById(R.id.activity_history_framelayout2);
        TextView textView2 = (TextView) findViewById(R.id.activity_history_textview2);
        ImageButton commentButton2 = (ImageButton) findViewById(R.id.activity_history_comment_btn2);

        FrameLayout frameLayout3 = (FrameLayout) findViewById(R.id.activity_history_framelayout3);
        TextView textView3 = (TextView) findViewById(R.id.activity_history_textview3);
        ImageButton commentButton3 = (ImageButton) findViewById(R.id.activity_history_comment_btn3);

        FrameLayout frameLayout4 = (FrameLayout) findViewById(R.id.activity_history_framelayout4);
        TextView textView4 = (TextView) findViewById(R.id.activity_history_textview4);
        ImageButton commentButton4 = (ImageButton) findViewById(R.id.activity_history_comment_btn4);

        FrameLayout frameLayout5 = (FrameLayout) findViewById(R.id.activity_history_framelayout5);
        TextView textView5 = (TextView) findViewById(R.id.activity_history_textview5);
        ImageButton commentButton5 = (ImageButton) findViewById(R.id.activity_history_comment_btn5);

        FrameLayout frameLayout6 = (FrameLayout) findViewById(R.id.activity_history_framelayout6);
        TextView textView6 = (TextView) findViewById(R.id.activity_history_textview6);
        ImageButton commentButton6 = (ImageButton) findViewById(R.id.activity_history_comment_btn6);

        FrameLayout frameLayout7 = (FrameLayout) findViewById(R.id.activity_history_framelayout7);
        TextView textView7 = (TextView) findViewById(R.id.activity_history_textview7);
        ImageButton commentButton7 = (ImageButton) findViewById(R.id.activity_history_comment_btn7);

        FrameLayout [] frameLayouts = {frameLayout1, frameLayout2, frameLayout3, frameLayout4, frameLayout5, frameLayout6, frameLayout7};
        TextView [] textViews = {textView1, textView2, textView3, textView4, textView5, textView6, textView7};
        ImageButton [] commentButtons = {commentButton1, commentButton2, commentButton3, commentButton4, commentButton5, commentButton6, commentButton7};

        for (int i = 0; i < frameLayouts.length; i++){
            configureFrameLayout(frameLayouts[i], i+1);
            setTextView(textViews[i], i+1);
            configureCommentButton(commentButtons[i], i+1);
        }
    }

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
                    String comment = mWeeklyMoods.getDailyComment(mPreferences, numDay);
                    Toast.makeText(HistoryActivity.this, comment, Toast.LENGTH_LONG).show();
                }   });
        }else{
            imgbtn.setVisibility(ImageButton.INVISIBLE);
        }
    }

}