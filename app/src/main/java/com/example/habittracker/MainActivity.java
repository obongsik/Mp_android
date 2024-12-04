package com.example.habittracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button btnLogout, btnGoalSetting;
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

        // 버튼 초기화
        btnLogout = findViewById(R.id.btnLogout);
        btnGoalSetting = findViewById(R.id.btnGoalSetting);

        // 로그아웃 버튼 클릭 리스너
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userManager.logout(); // 로그아웃 처리
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // MainActivity 종료
            }
        });

        // 목표 설정 버튼 클릭 리스너
        btnGoalSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // AddGoalActivity로 이동
                Intent intent = new Intent(MainActivity.this, AddGoalActivity.class);
                startActivity(intent);
            }
        });
    }
}