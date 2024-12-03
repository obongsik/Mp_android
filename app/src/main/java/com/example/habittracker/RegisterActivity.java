package com.example.habittracker;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new MyDatabaseHelper(this); // dbHelper 초기화

        // 회원가입 필드 및 버튼
        EditText nameEditText = findViewById(R.id.edit_name);
        EditText emailEditText = findViewById(R.id.edit_email);
        EditText passwordEditText = findViewById(R.id.edit_password);
        Button signUpButton = findViewById(R.id.btn_sign_up);

        // 회원가입 버튼 클릭 리스너
        signUpButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "모든 정보를 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "유효한 이메일 주소를 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(this, "비밀번호는 6자 이상이어야 합니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db = null;
            try {
                db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("username", name);
                values.put("email", email); // 이메일 추가
                values.put("password", password);

                long result = db.insert("User", null, values);
                if (result == -1) {
                    System.err.println("Insert failed: " + values.toString());
                    Toast.makeText(this, "회원가입에 실패했습니다. 다시 시도하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    System.out.println("Insert success: " + values.toString());
                    Toast.makeText(this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                    finish(); // LoginActivity로 이동
                }
            } catch (Exception e) {
                System.err.println("Database error: " + e.getMessage());
                Toast.makeText(this, "Database error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            } finally {
                if (db != null) db.close();
            }
        });
    }
}
