package com.example.habittracker;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class StatisticsActivity extends AppCompatActivity {

    private RecyclerView weeklyRecyclerView, monthlyRecyclerView;
    private HabitDatabaseHelper dbHelper;
    private TextView weeklyStatsText, monthlyStatsText;
    private Button backButton; // 뒤로가기 버튼 추가

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        dbHelper = new HabitDatabaseHelper(this);
        weeklyRecyclerView = findViewById(R.id.weekRecyclerView);
        monthlyRecyclerView = findViewById(R.id.monthRecyclerView);
        weeklyStatsText = findViewById(R.id.weekTextView);
        monthlyStatsText = findViewById(R.id.monthTextView);
        backButton = findViewById(R.id.btn_back); // 뒤로가기 버튼 초기화

        try {
            // 주별 성취도 데이터 가져오기
            Cursor weeklyData = dbHelper.getWeeklyAchievementData();
            if (weeklyData != null && weeklyData.getCount() > 0) {
                WeeklyStatsAdapter weeklyAdapter = new WeeklyStatsAdapter(this, weeklyData);
                weeklyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                weeklyRecyclerView.setAdapter(weeklyAdapter);
            } else {
                weeklyStatsText.setText("주간 데이터가 없습니다.");
            }

            // 월별 성취도 데이터 가져오기
            Cursor monthlyData = dbHelper.getMonthlyAchievementData();
            if (monthlyData != null && monthlyData.getCount() > 0) {
                MonthlyStatsAdapter monthlyAdapter = new MonthlyStatsAdapter(this, monthlyData);
                monthlyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                monthlyRecyclerView.setAdapter(monthlyAdapter);
            } else {
                monthlyStatsText.setText("월간 데이터가 없습니다.");
            }

        } catch (Exception e) {
            Log.e("StatisticsActivity", "Error loading data", e);
        }

        // 뒤로가기 버튼 클릭 리스너
        backButton.setOnClickListener(v -> {
            finish(); // 현재 액티비티 종료
        });
    }
}



