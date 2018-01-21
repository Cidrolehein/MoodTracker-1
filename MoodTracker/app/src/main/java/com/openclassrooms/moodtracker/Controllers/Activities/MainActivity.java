package com.openclassrooms.moodtracker.Controllers.Activities;

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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.openclassrooms.moodtracker.Adapters.PageAdapter;
import com.openclassrooms.moodtracker.Models.WeeklyMoods;
import com.openclassrooms.moodtracker.R;

import java.util.Calendar;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private PopupWindow mPopupWindow;
    private FrameLayout mMainLayout;
    private VerticalViewPager viewPager;
    private Button mCommentButton;
    private Button mHistoryButton;

    private WeeklyMoods mTodayMood;
    private int intTodayMood;
    private SharedPreferences mPreferences;
    private int currentDay;

    int[] smileys = {R.drawable.smiley_sad, R.drawable.smiley_disappointed, R.drawable.smiley_normal, R.drawable.smiley_happy, R.drawable.smiley_super_happy};

    private static final float MIN_SCALE = 0.75f;
    private static final float MIN_ALPHA = 0.75f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Configure Comment and History Buttons
        mCommentButton = (Button) findViewById(R.id.activity_main_comment_button);
        mHistoryButton = (Button) findViewById(R.id.activity_main_history_button);
        mMainLayout = (FrameLayout) findViewById(R.id.activity_main_frame_layout);
        mContext = getApplicationContext();
        this.configureButtons();

        //Configure ViewPager if same day (get last saved mood) or new day (default mood)
        mPreferences = getSharedPreferences("dailyMoods", MODE_PRIVATE);
        mTodayMood = new WeeklyMoods();
        currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        this.configureDailyMoodWithDate(mPreferences, currentDay, mTodayMood);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Save currentDay and currentMood as WeeklyMood[0]
        int position = viewPager.getCurrentItem();
        mPreferences.edit().putInt("Today", currentDay).apply();
        mTodayMood.setDailyMood(mPreferences, 0, position);
    }

    private void configureViewPager() {
        viewPager = (VerticalViewPager) findViewById(R.id.activity_main_viewpager);
        viewPager.setAdapter(new PageAdapter(
                getSupportFragmentManager(),
                getResources().getIntArray(R.array.colorPagesViewPager), smileys) {});
        viewPager.setCurrentItem(intTodayMood);

        viewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View view, float position) {
                int pageWidth = view.getWidth();
                int pageHeight = view.getHeight();

                if (position < -1) { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    view.setAlpha(0);

                } else if (position <= 1) { // [-1,1]
                    // Modify the default slide transition to shrink the page as well
                    float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                    float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                    float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                    if (position < 0) {
                        view.setTranslationY(vertMargin - horzMargin / 2);
                    } else {
                        view.setTranslationY(-vertMargin + horzMargin / 2);
                    }

                    // Scale the page down (between MIN_SCALE and 1)
                    view.setScaleX(scaleFactor);
                    view.setScaleY(scaleFactor);

                    // Fade the page relative to its size.
                    view.setAlpha(MIN_ALPHA +
                            (scaleFactor - MIN_SCALE) /
                                    (1 - MIN_SCALE) * (1 - MIN_ALPHA));

                } else { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    view.setAlpha(0);
                }
            }
        });
    }

    private void configureDailyMoodWithDate (SharedPreferences prefsFile, int currentDay, WeeklyMoods wm){
        //Get savedDay
        int savedDay = prefsFile.getInt("Today", currentDay);

        //If savedDay is different from today, update WeeklyMoods and Comments and show default mood
        if(currentDay != savedDay)  wm.updateWeeklyMoods(prefsFile);
        intTodayMood = wm.getDailyMood(prefsFile, 0);
        this.configureViewPager();
    }

    private void configureButtons(){
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
                showPopupWindow();
            }
        });
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

        //Save Text in EditText as a comment when Validate clicked
        final EditText commentsText = (EditText) commentPopupView.findViewById((R.id.activity_main_comment_popup_validate_edittext));

        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getComment from EditText
                String dailyComment = commentsText.getText().toString();

                if(!dailyComment.equals("")){
                    //Save the comment
                    mTodayMood.setDailyComment(mPreferences, 0, dailyComment);
                    mPopupWindow.dismiss();
                    Toast.makeText(MainActivity.this, "Commentaire enregistré", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "Ecrivez votre commentaire", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Positions popup window
        mPopupWindow.showAtLocation(mMainLayout, Gravity.TOP,0,0);
    }
}