package com.example.habittracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 로그인 버튼
        Button loginButton = findViewById(R.id.btn_login);
        EditText emailEditText = findViewById(R.id.edit_email);
        EditText passwordEditText = findViewById(R.id.edit_password);

        // 회원가입 텍스트
        TextView signUpText = findViewById(R.id.text_sign_up);

        // 뒤로가기 버튼
        Button backButton = findViewById(R.id.btn_back);

        HabitDatabaseHelper dbHelper = new HabitDatabaseHelper(this);

        // 로그인 버튼 클릭 리스너
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            } else {
                // 로그인 로직 (데이터베이스에서 이메일과 비밀번호 확인)
                boolean isValid = dbHelper.checkUser(email, password);
                if (isValid) {
                    Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show();
                    // 로그인 성공 시 MainActivity로 이동
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "잘못된 이메일 또는 비밀번호입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 회원가입 버튼 클릭 리스너
        signUpText.setOnClickListener(v -> {
            // RegisterActivity로 이동
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // 뒤로가기 버튼 클릭 리스너
        backButton.setOnClickListener(v -> {
            // 현재 액티비티 종료
            finish();
        });
    }
}

