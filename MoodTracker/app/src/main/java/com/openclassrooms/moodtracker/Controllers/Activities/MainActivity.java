package com.openclassrooms.moodtracker.Controllers.Activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.openclassrooms.moodtracker.Adapters.PageAdapter;
import com.openclassrooms.moodtracker.MoodManager;
import com.openclassrooms.moodtracker.R;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.activity_main_comment_button)
    Button mCommentButton;
    @BindView(R.id.activity_main_history_button)
    Button mHistoryButton;

    private VerticalViewPager viewPager;

    private SharedPreferences mPreferences;
    private MoodManager mMoodManager;
    private int currentY, currentM, currentD;
    private int inBetweenDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        //Setup SharedPreferences and instance of MoodManager
        mPreferences = getSharedPreferences("dailyMoods", MODE_PRIVATE);
        mMoodManager = MoodManager.getInstance();

        //If not same day, update WeeklyMoods according with the numbers of day since last opening
        inBetweenDays = inBetweenDays(mPreferences);
        if(inBetweenDays != 0)
            mMoodManager.updateWeeklyMoods(mPreferences, inBetweenDays);

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
    }

    //---------------------------
    // MANAGEMENT OF DAY DIFFERENCE
    //---------------------------

    private int inBetweenDays (SharedPreferences prefsFile) {

        //Get Current Time
        currentY = Calendar.getInstance().get(Calendar.YEAR);
        currentM = Calendar.getInstance().get(Calendar.MONTH);
        currentD = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        //Get Saved Time
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
            inBetweenDays = currentD + (monthNbOfDays-savedDay);
        }else{
            inBetweenDays = 7;
        }
        return inBetweenDays;
    }

    //---------------------------
    // CONFIGURATION OF VIEW
    //---------------------------

    private void configureViewPager(int todayMood) {
        final int[] smileys = {R.drawable.smiley_sad, R.drawable.smiley_disappointed,
                R.drawable.smiley_normal, R.drawable.smiley_happy, R.drawable.smiley_super_happy};

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
    }

    private void configureCommentButton(){
        mCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show Popup Window when Comment Button clicked
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                builder .setMessage("Commentaire")
                        .setView(inflater.inflate(R.layout.activity_main_comment_popup, null))
                        .setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Dialog f = (Dialog) dialog;
                                EditText commentsText = (EditText)f.findViewById(R.id.activity_main_edit_text);

                                //getComment from EditText
                                String dailyComment = commentsText.getText().toString();

                                if(!dailyComment.equals("")){
                                    //Save the comment
                                    mMoodManager.saveCommentToPrefs(mPreferences, 0, dailyComment);
                                    Toast.makeText(MainActivity.this, "Commentaire enregistré", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }
                        })
                        .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .create()
                        .show();
            }
        });
    }
}