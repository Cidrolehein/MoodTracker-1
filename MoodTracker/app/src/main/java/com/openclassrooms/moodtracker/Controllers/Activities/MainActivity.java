package com.openclassrooms.moodtracker.Controllers.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.openclassrooms.moodtracker.Adapters.PageAdapter;
import com.openclassrooms.moodtracker.Models.WeeklyMoods;
import com.openclassrooms.moodtracker.R;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private PopupWindow mPopupWindow;
    private FrameLayout mMainLayout;
    private ViewPager viewPager;

    private WeeklyMoods mTodayMood;
    private int intTodayMood;
    private SharedPreferences mPreferences;
    private int currentDay;

    int[] smileys = {R.drawable.smiley_sad, R.drawable.smiley_disappointed, R.drawable.smiley_normal, R.drawable.smiley_happy, R.drawable.smiley_super_happy};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mCommentButton = (Button) findViewById(R.id.activity_main_comment_button);
        Button mHistoryButton = (Button) findViewById(R.id.activity_main_history_button);
        mMainLayout = (FrameLayout) findViewById(R.id.activity_main_frame_layout);
        mContext = getApplicationContext();

        mPreferences = getSharedPreferences("dailyMoods", MODE_PRIVATE);
        mTodayMood = new WeeklyMoods();

        currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        intTodayMood = showMoodDependingOnDayIdentity(mPreferences, currentDay, mTodayMood);

        mHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent historyActivityIntent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(historyActivityIntent);
            }
        });

        mCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }
        });

        this.configureViewPager();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        int position = viewPager.getCurrentItem();
        mPreferences.edit().putInt("Today", currentDay).apply();
        mTodayMood.setDailyMood(mPreferences, 0, position);
    }

    private void configureViewPager() {
        viewPager = (ViewPager) findViewById(R.id.activity_main_viewpager);
        viewPager.setAdapter(new PageAdapter(
                getSupportFragmentManager(),
                getResources().getIntArray(R.array.colorPagesViewPager), smileys) {});
        viewPager.setCurrentItem(intTodayMood);
    }

    private int showMoodDependingOnDayIdentity (SharedPreferences prefsFile, int currentDay, WeeklyMoods wm){
        int savedDay = prefsFile.getInt("Today", currentDay);
        if(currentDay != savedDay)  wm.updateWeeklyMoods(prefsFile);
        return wm.getDailyMood(prefsFile, 0);
    }


    private void showPopupWindow(){
        // Initialize a new instance of LayoutInflater service
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        // Inflate the custom layout/view
        View commentPopupView = inflater.inflate(R.layout.activity_main_comment_popup,null);

        // Initialize a new instance of popup window
        mPopupWindow = new PopupWindow(commentPopupView, 800, 600, true);

        // Get a reference for the close and validate buttons
        Button cancelButton = (Button) commentPopupView.findViewById(R.id.activity_main_comment_popup_cancel_btn);
        Button validateButton = (Button) commentPopupView.findViewById(R.id.activity_main_comment_popup_validate_btn);

        // Set a click listener for the popup window close button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                mPopupWindow.dismiss();
            }
        });

        final EditText validateComments = (EditText) commentPopupView.findViewById((R.id.activity_main_comment_popup_validate_edittext));

        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getComment from EditText
                String dailyComment = validateComments.getText().toString();

                //Save the comment
                mTodayMood.setDailyComment(mPreferences, 0, dailyComment);
                mPopupWindow.dismiss();
                Toast.makeText(MainActivity.this, "Commentaire enregistré", Toast.LENGTH_SHORT).show();
            }
        });

        //Positions popup window
        mPopupWindow.showAtLocation(mMainLayout, Gravity.TOP,0,0);
    }
}