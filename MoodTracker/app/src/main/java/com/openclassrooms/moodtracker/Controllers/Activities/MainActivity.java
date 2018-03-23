package com.openclassrooms.moodtracker.Controllers.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.openclassrooms.moodtracker.Adapters.PageAdapter;
import com.openclassrooms.moodtracker.MoodManager;
import com.openclassrooms.moodtracker.R;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.activity_main_frame_layout)
    FrameLayout mMainLayout;
    @BindView(R.id.activity_main_comment_button)
    Button mCommentButton;
    @BindView(R.id.activity_main_history_button)
    Button mHistoryButton;

    private VerticalViewPager viewPager;

    private SharedPreferences mPreferences;
    private MoodManager mMoodManager;

    private int currentY, currentM, currentD;
    private int inBetweenDays;

    private final int[] smileys = {R.drawable.smiley_sad, R.drawable.smiley_disappointed,
            R.drawable.smiley_normal, R.drawable.smiley_happy, R.drawable.smiley_super_happy};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        //Setup SharedPreferences
        mPreferences = getSharedPreferences("dailyMoods", MODE_PRIVATE);
        mMoodManager = MoodManager.getInstance();

        //If not same day, update WeeklyMoods according with the numbers of day since last opening
        inBetweenDays = inBetweenDays(mPreferences);
        Log.e("MainActivity onCreate()", "inBetweenDays = " + inBetweenDays);

        if(inBetweenDays != 0) mMoodManager.updateWeeklyMoods(mPreferences, inBetweenDays);

        this.configureHistoryButton();
        this.configureCommentButton();
        this.configureViewPager(mMoodManager.getMoodFromPrefs(mPreferences, 0));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Get Current Time
        currentY = Calendar.getInstance().get(Calendar.YEAR);
        currentM = Calendar.getInstance().get(Calendar.MONTH);
        currentD = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        //Save current date
        mPreferences.edit().putInt("TodayDay", currentD).apply();
        mPreferences.edit().putInt("TodayMonth", currentM).apply();
        mPreferences.edit().putInt("TodayYear", currentY).apply();

        //Save current mood with viewpager position index
        mMoodManager.saveMoodToPrefs(mPreferences, 0, viewPager.getCurrentItem());
        Log.e("MainActivity onDestroy", "mood saved = " + viewPager.getCurrentItem());
    }

    private int inBetweenDays (SharedPreferences prefsFile) {

        int betweenDays;

        int savedYear = prefsFile.getInt("TodayYear", currentY);
        int savedMonth = prefsFile.getInt("TodayMonth", currentM);
        int savedDay = prefsFile.getInt("TodayDay", currentD);

        if (savedYear == currentY && savedMonth == currentM){
            return currentD - savedDay;

        }else if ((currentD < 7) && (currentY - savedYear <= 1)
                && ((currentM - savedMonth)==1) || (savedMonth==12 && currentM==0)) {
            int monthNbOfDays = 0;
            switch(savedMonth) {
                case 0: case 2 : case 4 : case 6 : case 7 : case 9 : case 11: monthNbOfDays = 31; break;
                case 3 : case 5 : case 8 : case 10 : monthNbOfDays = 30; break;
                case 1 : //February
                    if((savedYear % 4 == 0)&&((savedYear%100 !=0)||(savedYear %400 == 0)))
                        monthNbOfDays = 29;
                    else
                        monthNbOfDays = 28;
                    break;
            }
            betweenDays = currentD + (monthNbOfDays-savedDay);
        }else{
            betweenDays = 7;
        }
        return betweenDays;
    }

    private void configureViewPager(int todayMood) {
        viewPager = (VerticalViewPager) findViewById(R.id.activity_main_viewpager);
        viewPager.setAdapter(new PageAdapter(
                getSupportFragmentManager(),
                getResources().getIntArray(R.array.colorPagesViewPager), smileys) {});
        viewPager.setCurrentItem(todayMood);
    }

    private void configureHistoryButton(){
        mHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start Activity History when History Button clicked
                Intent historyActivityIntent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(historyActivityIntent);
            }
        });

        mCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show Popup Window when Comment Button clicked
                configureCommentPopupWindow();
            }
        });
    }

    private void configureCommentButton(){
        // Initialize a new instance of LayoutInflater service
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        // Inflate the custom layout/view
        View commentPopupView = inflater.inflate(R.layout.activity_main_comment_popup,null);

        getDeviceMetrics();
        // Initialize a new instance of popup window
        mPopupWindow = new PopupWindow(commentPopupView, (int)(deviceWidth/1.2), (int)(deviceHeight/3), true);

        // Get a reference for the close and validate buttons
        cancelButton = (Button) commentPopupView.findViewById(R.id.activity_main_comment_popup_cancel_btn);
        validateButton = (Button) commentPopupView.findViewById(R.id.activity_main_comment_popup_validate_btn);
        commentsText = (EditText) commentPopupView.findViewById((R.id.activity_main_comment_popup_validate_edittext));

        configureCommentCancelButton();
        configureCommentValidateButton();

        //Positions popup window
        mPopupWindow.showAtLocation(mMainLayout, Gravity.TOP,0,0);
    }

    private void configureCommentCancelButton(){
        // Set a click listener for the popup window close button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                mPopupWindow.dismiss();
            }
        });
    }

    private void configureCommentValidateButton(){
        //Save Text in EditText as a comment when Validate clicked
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getComment from EditText
                String dailyComment = commentsText.getText().toString();

                if(!dailyComment.equals("")){
                    //Save the comment
                    mMoodManager.saveCommentToPrefs(mPreferences, 0, dailyComment);
                    mPopupWindow.dismiss();
                    Toast.makeText(MainActivity.this, "Commentaire enregistré", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "Ecrivez votre commentaire", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}