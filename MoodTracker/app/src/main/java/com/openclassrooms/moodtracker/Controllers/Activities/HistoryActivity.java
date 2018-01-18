package com.openclassrooms.moodtracker.Controllers.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import com.openclassrooms.moodtracker.R;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        this.configureTextViews();
    }

    private void configureTextViews(){
        TextView [] textViews = {(TextView) findViewById(R.id.activity_history_text_view_7),
                                 (TextView) findViewById(R.id.activity_history_text_view_6),
                                 (TextView) findViewById(R.id.activity_history_text_view_5),
                                 (TextView) findViewById(R.id.activity_history_text_view_4),
                                 (TextView) findViewById(R.id.activity_history_text_view_3),
                                 (TextView) findViewById(R.id.activity_history_text_view_2),
                                 (TextView) findViewById(R.id.activity_history_text_view_1)};

        int [] viewSize = {100, 250, 400, 550, 700};

        String [] dayCount = {"Il y a une semaine", "Il y a 6 jours", "Il y a 5 jours",
                              "Il y a 4 jours", "Il y a 3 jours", "Avant-hier", "Hier"};

        // IntFeeling for every day (0=sad / 1=disappointed / 2=normal / 3=happy / 4=superhappy
        int [] days = {3, 2, 0, 2, 3, 1, 2};


        for (int i = 0; i < textViews.length; i++){
            textViews[i].setText(dayCount[i]);
            textViews[i].setTextSize(20);
            textViews[i].setPadding(10,0,0,0);

            textViews[i].setBackgroundColor(getResources().getIntArray(R.array.colorPagesViewPager)[days[i]]);
            textViews[i].getLayoutParams().width = viewSize[days[i]];
        }
    }
}
