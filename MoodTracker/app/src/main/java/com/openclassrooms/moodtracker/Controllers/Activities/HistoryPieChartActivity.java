package com.openclassrooms.moodtracker.Controllers.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.openclassrooms.moodtracker.R;

import java.util.ArrayList;
import java.util.List;

public class HistoryPieChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_pie_chart);


        PieChart pieChart = (PieChart)findViewById(R.id.activity_history_piechart);

        List<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(18.5f, "Sad"));
        entries.add(new PieEntry(26.7f, "Disappointed"));
        entries.add(new PieEntry(24.0f, "Normal"));
        entries.add(new PieEntry(15.8f, "Happy"));
        entries.add(new PieEntry(15.0f, "Super Happy"));


        PieDataSet set = new PieDataSet(entries, "Election Results");
        PieData data = new PieData(set);
        pieChart.setData(data);
        pieChart.invalidate(); // refresh
    }
}
