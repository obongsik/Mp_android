package com.example.habittracker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 데이터베이스 및 UserManager 초기화
        dbHelper = new MyDatabaseHelper(this);
        userManager = new UserManager(this);

        // UI 요소 초기화
        Button loginButton = findViewById(R.id.btn_login);
        EditText emailEditText = findViewById(R.id.edit_email);
        EditText passwordEditText = findViewById(R.id.edit_password);
        TextView signUpText = findViewById(R.id.text_sign_up);

        // 로그인 버튼 클릭 리스너
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (checkCredentials(email, password)) {
                Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show();

                // 로그인 성공 시 사용자 정보 저장
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM User WHERE email = ?", new String[]{email});
                if (cursor.moveToFirst()) {
                    String userId = cursor.getString(cursor.getColumnIndexOrThrow("user_id"));
                    String userName = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                    userManager.saveUser(userId, userName, email, password);
                }
                cursor.close();

                // MainActivity로 이동
                Intent intent = new Intent(LoginActivity.this, AddGoalActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "로그인 실패: 이메일 또는 비밀번호가 잘못되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // 회원가입 버튼 클릭 리스너
        signUpText.setOnClickListener(v -> {
            // RegisterActivity로 이동
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    // 데이터베이스에서 이메일과 비밀번호 확인
    private boolean checkCredentials(String email, String password) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = dbHelper.getReadableDatabase();
            String query = "SELECT * FROM User WHERE email = ? AND password = ?";
            cursor = db.rawQuery(query, new String[]{email, password});

            return cursor.moveToFirst(); // 결과가 있으면 로그인 성공
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }
}