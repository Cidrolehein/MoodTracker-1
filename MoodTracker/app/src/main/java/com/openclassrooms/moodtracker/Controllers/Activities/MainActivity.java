package com.openclassrooms.moodtracker.Controllers.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.openclassrooms.moodtracker.Adapters.PageAdapter;
import com.openclassrooms.moodtracker.R;

public class MainActivity extends AppCompatActivity {

    private Button mCommentButton;
    private Button mHistoryButton;

    int [] smileys = {R.drawable.smiley_sad, R.drawable.smiley_disappointed, R.drawable.smiley_normal, R.drawable.smiley_happy, R.drawable.smiley_super_happy};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCommentButton = (Button) findViewById(R.id.activity_main_comment_button);
        mHistoryButton = (Button) findViewById(R.id.activity_main_history_button);

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
                Toast.makeText(MainActivity.this, "Ajoutez un commentaire", Toast.LENGTH_SHORT).show();
        }});

        this.configureViewPager();
    }

    private void configureViewPager(){
        ViewPager viewPager = (ViewPager) findViewById(R.id.activity_main_viewpager);
        viewPager.setAdapter(new PageAdapter(
                getSupportFragmentManager(),
                getResources().getIntArray(R.array.colorPagesViewPager),
                smileys){});
    }




}
