package com.openclassrooms.moodtracker.Controllers.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.openclassrooms.moodtracker.Adapters.PageAdapter;
import com.openclassrooms.moodtracker.Models.WeeklyMoods;
import com.openclassrooms.moodtracker.R;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private Activity mActivity;
    private PopupWindow mPopupWindow;
    private FrameLayout mMainLayout;

    private WeeklyMoods mTodayMood;
    private int intTodayMood;
    private SharedPreferences mPreferences;

    private Button mCommentButton;
    private Button mHistoryButton;
    private View mCommentPopup;
    int[] smileys = {R.drawable.smiley_sad, R.drawable.smiley_disappointed, R.drawable.smiley_normal, R.drawable.smiley_happy, R.drawable.smiley_super_happy};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();
        mActivity = MainActivity.this;
        mMainLayout = (FrameLayout) findViewById(R.id.activity_main_frame_layout);
        mCommentButton = (Button) findViewById(R.id.activity_main_comment_button);
        mHistoryButton = (Button) findViewById(R.id.activity_main_history_button);

        mPreferences = getSharedPreferences("dailyMoods", MODE_PRIVATE);

        if(mTodayMood == null){
            mTodayMood = new WeeklyMoods();
            intTodayMood = 3;
            Toast.makeText(MainActivity.this, "New WeeklyMood", Toast.LENGTH_SHORT).show();
        }else{
            intTodayMood = mTodayMood.getDailyMood(mPreferences, 0);
            Toast.makeText(MainActivity.this, "Today Mood déjà enregistré", Toast.LENGTH_SHORT).show();
        }

        mTodayMood.setDailyMood(mPreferences, 4, 1);
        
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

    private void configureViewPager() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.activity_main_viewpager);
        viewPager.setAdapter(new PageAdapter(
                getSupportFragmentManager(),
                getResources().getIntArray(R.array.colorPagesViewPager), smileys) {});
        viewPager.setCurrentItem(intTodayMood);
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

        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Save the comment
                mPopupWindow.dismiss();
                Toast.makeText(MainActivity.this, "Commentaire enregistré", Toast.LENGTH_SHORT).show();
            }
        });

        //Positions popup window
        mPopupWindow.showAtLocation(mMainLayout, Gravity.TOP,0,0);
    }
}