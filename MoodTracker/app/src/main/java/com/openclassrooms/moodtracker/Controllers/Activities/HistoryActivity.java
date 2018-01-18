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
        TextView textView7 = (TextView) findViewById(R.id.activity_history_text_view_7);
        TextView textView6 = (TextView) findViewById(R.id.activity_history_text_view_6);
        TextView textView5 = (TextView) findViewById(R.id.activity_history_text_view_5);
        TextView textView4 = (TextView) findViewById(R.id.activity_history_text_view_4);
        TextView textView3 = (TextView) findViewById(R.id.activity_history_text_view_3);
        TextView textView2 = (TextView) findViewById(R.id.activity_history_text_view_2);
        TextView textView1 = (TextView) findViewById(R.id.activity_history_text_view_1);

        int [] feelings = {0, 1, 2, 3, 4};
        int intFeeling = feelings[3];

        int [] viewSize = {100, 250, 400, 550, 700};


        switch(intFeeling){
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                textView7.getLayoutParams().width = viewSize[intFeeling];
                textView7.setBackgroundColor(getResources().getIntArray(R.array.colorPagesViewPager)[intFeeling]);
                break;
            case 4:
                break;
            default:
                break;
        }
    }
}
