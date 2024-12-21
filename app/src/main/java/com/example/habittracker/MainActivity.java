package com.example.habittracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button btnGoalSetting, btnStatistics, btnLogout;
    private TextView tvQuestion; // 텍스트뷰
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UserManager 초기화
        userManager = new UserManager(this);

        // 로그인 상태 확인
        if (!userManager.isLoggedIn()) {
            // 로그인하지 않은 경우 LoginActivity로 이동
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // MainActivity 종료
            return;
        }

        // 버튼 및 텍스트뷰 초기화
        tvQuestion = findViewById(R.id.tvQuestion); // "무엇을 하시겠습니까?" 텍스트뷰
        btnGoalSetting = findViewById(R.id.btnGoalSetting); // 목표 설정 버튼
        btnStatistics = findViewById(R.id.btnStatistics); // 통계 페이지 버튼
        btnLogout = findViewById(R.id.btnLogout); // 로그아웃 버튼

        // 목표 설정 버튼 클릭 리스너
        btnGoalSetting.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddGoalActivity.class);
            startActivity(intent); // AddGoalActivity로 이동
        });

        // 통계 페이지 버튼 클릭 리스너
        btnStatistics.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
            startActivity(intent); // StatisticsActivity로 이동
        });

        // 로그아웃 버튼 클릭 리스너
        btnLogout.setOnClickListener(v -> {
            userManager.logout(); // 로그아웃 처리
            Toast.makeText(MainActivity.this, "로그아웃되었습니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent); // LoginActivity로 이동
            finish(); // MainActivity 종료
        });
    }
}
