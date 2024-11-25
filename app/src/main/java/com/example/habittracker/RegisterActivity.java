package com.example.habittracker;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 회원가입 필드 및 버튼
        EditText nameEditText = findViewById(R.id.edit_name);
        EditText emailEditText = findViewById(R.id.edit_email);
        EditText passwordEditText = findViewById(R.id.edit_password);
        Button signUpButton = findViewById(R.id.btn_sign_up);

        // 회원가입 버튼 클릭 리스너
        signUpButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "모든 정보를 입력하세요.", Toast.LENGTH_SHORT).show();
            } else {
                // 회원가입 로직 (예: 서버와의 통신)
                Toast.makeText(this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                finish(); // 회원가입 후 LoginActivity로 돌아감
            }
        });
    }
}