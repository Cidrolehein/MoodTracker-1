package com.openclassrooms.moodtracker.Controllers.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.openclassrooms.moodtracker.MoodManager;
import com.openclassrooms.moodtracker.R;

public class HistoryActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private MoodManager mMoodManager;

    private final double [] viewSizeMultiplier = {0.25, 0.4, 0.6, 0.8, 1};
    private double deviceWidth, deviceHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //Initialize Preferences
        mPreferences = getSharedPreferences("dailyMoods", MODE_PRIVATE);
        mMoodManager = MoodManager.getInstance();

        configureWeeklyMoods();
        configurePieChartButton();
    }

    //----------------------
    // CONFIGURATION
    //----------------------

    private void getDeviceMetrics(){
        //Get Device Width and Height
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        deviceWidth = displayMetrics.widthPixels;
        deviceHeight = displayMetrics.heightPixels;
    }

    private void configureWeeklyMoods(){
        //Inflate layout
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout container = (LinearLayout) findViewById(R.id.activity_history_linearLayout);

        //Create the 7 views of daily moods
        for(int i = 7; i > 0; i--) {

            View view;

            //Inflate framelayouts and create new textview and comment button
            view = inflater.inflate(R.layout.activity_history_frame, null);

            configureFrameLayout(view, i);

            //Add to the container (LinearLayout)
            container.addView(view);
        }

        //Add PieChart Button
        View piechartView = inflater.inflate(R.layout.activity_history_piechart_btn, null);
        container.addView(piechartView);
    }

    private void configurePieChartButton() {
        //Serialize the button
        Button mPieChartButton = (Button) findViewById(R.id.activity_history_piechart_button);

        //Add a clickListener to go to PieChart Activity
        mPieChartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pieChartActivityIntent = new Intent(HistoryActivity.this, HistoryPieChartActivity.class);
                startActivity(pieChartActivityIntent);
            }
        });
    }

    private void configureFrameLayout(View v, int numDay){

        //Get Mood of the day [i]
        int moodOfTheDay = mMoodManager.getMoodFromPrefs(mPreferences, (numDay));
        final String commentOfTheDay = mMoodManager.getCommentFromPrefs(mPreferences, (numDay));

        //Serialize FrameLayouts, TextViews and Buttons
        FrameLayout frameLayout = (FrameLayout)v.findViewById(R.id.activity_history_framelayout);
        TextView textView = (TextView)v.findViewById(R.id.activity_history_textview);
        ImageButton commentButton = (ImageButton)v.findViewById(R.id.activity_history_comment_btn);

        //Define FrameLayout width and height with device width and height
        getDeviceMetrics();
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                (int) (deviceWidth*viewSizeMultiplier[moodOfTheDay]),
                (int) deviceHeight/9);

        //Set FrameLayout
        frameLayout.setLayoutParams(params);
        frameLayout.setBackgroundColor(getResources().getIntArray(R.array.colorPagesViewPager)[moodOfTheDay]);

        //Set TextView according to Day
        textView.setText(getResources().getStringArray(R.array.dayCount)[numDay-1]);

        //Set Comment button if comment exists
        if(!commentOfTheDay.equals("")){
            //Show button + if click on button, show Comment (Toast)
            commentButton.setVisibility(ImageButton.VISIBLE);
            commentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(HistoryActivity.this, commentOfTheDay, Toast.LENGTH_SHORT).show();
                }}   );
        }else{
            commentButton.setVisibility(ImageButton.INVISIBLE);
        }
    }
}