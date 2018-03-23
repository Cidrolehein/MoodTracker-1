package com.openclassrooms.moodtracker.Controllers.Activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.openclassrooms.moodtracker.MoodManager;
import com.openclassrooms.moodtracker.R;
import com.openclassrooms.moodtracker.Adapters.DecimalRemover;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryPieChartActivity extends AppCompatActivity {

    @BindView(R.id.activity_history_piechart)
    PieChart pieChart;

    private SharedPreferences mPreferences;
    private MoodManager mMoodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_pie_chart);
        ButterKnife.bind(this);

        //Initialize Preferences and instance of MoodManager
        mPreferences = getSharedPreferences("dailyMoods", MODE_PRIVATE);
        mMoodManager = MoodManager.getInstance();

        //Configure view
        this.configurePieChart();
    }

    private void configurePieChart(){
        float [] moods = getStats();
        String [] moodLabels = {"Tristesse", "Déception", "Normal", "Satisfaction", "Bonheur"};
        int [] colors = getResources().getIntArray(R.array.colorPagesViewPager);
        List<Integer> moodColors = new ArrayList<>();
        List<PieEntry> entries = new ArrayList<>();

        //Increase size of pie slice per mood present
        for(int i = 0; i<moods.length; i++){
            if (moods[i] != 0.0f){
                entries.add(new PieEntry(moods[i], moodLabels[i]));
                moodColors.add(colors[i]);
            }
        }

        //Configure legend
        Legend legend = pieChart.getLegend();
        legend.setEnabled(true);

        //Configure set of piechart
        PieDataSet set = new PieDataSet(entries, "");
        set.setColors(moodColors);

        //Configure data of piechart
        PieData data = new PieData(set);
        pieChart.setData(data);
        pieChart.setDescription(null);

        //Configure stats format of pie chart
        data.setValueFormatter(new DecimalRemover(new DecimalFormat("###,###,###")));
        pieChart.invalidate(); // refresh
    }

    private float [] getStats(){
        float [] moods = new float[5];

        //Get frequence of each mood
        for (int i = 1; i < 8; i++) {
            int mood = mMoodManager.getMoodFromPrefs(mPreferences, i);
            switch (mood) {
                case 0: moods[0]++; break;
                case 1: moods[1]++; break;
                case 2: moods[2]++; break;
                case 3: moods[3]++; break;
                case 4: moods[4]++; break;
            }
        }

        //Get percentage of frequence of each mood
        for (int i = 0; i<moods.length; i++){
            moods[i] = moods[i] / 7 * 100;
        }
    return moods;
    }


}
