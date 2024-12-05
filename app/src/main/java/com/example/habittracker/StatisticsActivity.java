package com.example.habittracker;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class StatisticsActivity extends AppCompatActivity {

    private RecyclerView weeklyRecyclerView, monthlyRecyclerView;
    private HabitDatabaseHelper dbHelper;
    private TextView weeklyStatsText, monthlyStatsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        dbHelper = new HabitDatabaseHelper(this);
        weeklyRecyclerView = findViewById(R.id.weekRecyclerView);
        monthlyRecyclerView = findViewById(R.id.monthRecyclerView);
        weeklyStatsText = findViewById(R.id.weekTextView);
        monthlyStatsText = findViewById(R.id.monthTextView);

        // 주별 성취도 데이터 가져오기
        Cursor weeklyData = dbHelper.getWeeklyAchievementData();
        WeeklyStatsAdapter weeklyAdapter = new WeeklyStatsAdapter(this, weeklyData);
        weeklyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        weeklyRecyclerView.setAdapter(weeklyAdapter);

        // 월별 성취도 데이터 가져오기
        Cursor monthlyData = dbHelper.getMonthlyAchievementData();
        MonthlyStatsAdapter monthlyAdapter = new MonthlyStatsAdapter(this, monthlyData);
        monthlyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        monthlyRecyclerView.setAdapter(monthlyAdapter);
    }
}

