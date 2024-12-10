package com.example.habittracker;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout passwordInputLayout;
    private EditText passwordEditText;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 회원가입 필드 및 버튼 초기화
        EditText nameEditText = findViewById(R.id.edit_name);
        EditText emailEditText = findViewById(R.id.edit_email);
        passwordEditText = findViewById(R.id.edit_password);
        passwordInputLayout = findViewById(R.id.password_input_layout);
        Button signUpButton = findViewById(R.id.btn_sign_up);
        backButton = findViewById(R.id.btn_back);

        // 비밀번호 도움말 표시
        passwordEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                passwordInputLayout.setHelperText("4자리 이상의 숫자만 가능합니다.");
            } else {
                passwordInputLayout.setHelperTextEnabled(false);
            }
        });

        // 회원가입 버튼 클릭 리스너
        signUpButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // 입력값 유효성 검사
            if (name.isEmpty()) {
                Toast.makeText(this, "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (email.isEmpty() || !isValidEmail(email)) {
                Toast.makeText(this, "유효한 이메일을 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.isEmpty() || !isValidPassword(password)) {
                Toast.makeText(this, "비밀번호는 4자리 이상의 숫자만 가능합니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            // 회원정보 데이터베이스에 저장
            HabitDatabaseHelper dbHelper = new HabitDatabaseHelper(this);
            boolean isUserAdded = dbHelper.addUser(name, email, password);

            if (isUserAdded) {
                Toast.makeText(this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "회원가입 실패. 다시 시도하세요.", Toast.LENGTH_SHORT).show();
            }
        });

        // 뒤로가기 버튼 클릭 리스너
        backButton.setOnClickListener(v -> finish());
    }

    // 이메일 유효성 검사
    private boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(emailPattern, email);
    }

    // 비밀번호 유효성 검사
    private boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false; // 비어 있는 경우 false 반환
        }
        String passwordPattern = "^\\d{4,}$"; // 4자리 이상의 숫자
        return Pattern.matches(passwordPattern, password);
    }
}



