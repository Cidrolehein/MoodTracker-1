package com.openclassrooms.moodtracker.Controllers.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.openclassrooms.moodtracker.Models.WeeklyMoods;
import com.openclassrooms.moodtracker.R;

public class HistoryActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private Button mPieChartButton;

    private WeeklyMoods mWeeklyMoods = new WeeklyMoods();
    private int [] viewSize = {250, 500, 750, 1000, 1250};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mPieChartButton = (Button) findViewById(R.id.activity_history_piechart_button);
        mPreferences = getSharedPreferences("dailyMoods", MODE_PRIVATE);

        configureWeeklyMoods();
        configurePieChartButton();
    }

    private void configureWeeklyMoods(){
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout container = (LinearLayout) findViewById(R.id.activity_history_linearLayout);

        View [] views = new View [7];

        //Create the 7 views of daily moods
        for(int i = 0; i < views.length; i++) {
            //Inflate framelayouts and create new textview and comment button
            views[i] = inflater.inflate(R.layout.activity_history_frame, null);
            TextView textView = (TextView)views[i].findViewById(R.id.activity_history_textview);
            ImageButton commentButton = (ImageButton)views[i].findViewById(R.id.activity_history_comment_btn);

            //Set TextView according to Mood
            int dailyMood = mWeeklyMoods.getDailyMood(mPreferences, i+1);
            textView.setText(getResources().getStringArray(R.array.dayCount)[i]);
            textView.setLayoutParams(new FrameLayout.LayoutParams(viewSize[dailyMood],
                    150));

            textView.setBackgroundColor(getResources().getIntArray(R.array.colorPagesViewPager)[dailyMood]);

            //Set Comment button if comment exists
            if(!mWeeklyMoods.getDailyComment(mPreferences, i+1).equals("")){
                //Show button + if click on button, show Comment (Toast)
                commentButton.setVisibility(ImageButton.VISIBLE);
                final int finalI = i;
                commentButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String comment = mWeeklyMoods.getDailyComment(mPreferences, finalI+1);
                        Toast.makeText(HistoryActivity.this, comment, Toast.LENGTH_LONG).show();
                    }   });
            }else{
                commentButton.setVisibility(ImageButton.INVISIBLE);
            }

            //Create new view and add it to the container (LinearLayout)
            container.addView(views[i]);
        }
    }

    private void configurePieChartButton() {
        mPieChartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start Activity History when History Button clicked
                Intent pieChartActivityIntent = new Intent(HistoryActivity.this, HistoryPieChartActivity.class);
                startActivity(pieChartActivityIntent);
            }
        });
    }
}