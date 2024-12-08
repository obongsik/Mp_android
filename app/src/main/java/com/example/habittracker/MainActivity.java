package com.example.habittracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button btnLogout;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogout = findViewById(R.id.btnLogout);
        btnBack = findViewById(R.id.btn_back); // 뒤로가기 버튼 연결

        UserManager userManager = new UserManager(this);

        // 통계 화면으로 이동
        startActivity(new Intent(MainActivity.this, StatisticsActivity.class));
        finish();  // MainActivity 종료

        // 로그아웃 버튼 클릭 리스너
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userManager.logout();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });

        // 뒤로가기 버튼 클릭 리스너
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MainActivity 종료 (뒤로가기)
                finish();
            }
        });
    }
}

