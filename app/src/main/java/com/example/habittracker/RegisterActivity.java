package com.example.habittracker;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout passwordInputLayout;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 회원가입 필드 및 버튼
        EditText nameEditText = findViewById(R.id.edit_name);
        EditText emailEditText = findViewById(R.id.edit_email);
        passwordEditText = findViewById(R.id.edit_password);
        Button signUpButton = findViewById(R.id.btn_sign_up);
        passwordInputLayout = findViewById(R.id.password_input_layout);

        // 비밀번호 입력 시 도움말 텍스트 보이기
        passwordEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // 비밀번호 입력란에 포커스를 맞췄을 때 조건 텍스트를 보이도록 설정
                passwordInputLayout.setHelperText("4자리 이상의 숫자만 가능합니다.");
            } else {
                // 포커스를 잃으면 조건 텍스트를 숨김
                passwordInputLayout.setHelperTextEnabled(false);
            }
        });

        // 회원가입 버튼 클릭 리스너
        signUpButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // 이메일 형식 검증
            if (!isValidEmail(email)) {
                Toast.makeText(this, "이메일 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            // 비밀번호 형식 검증 (4자리 이상의 숫자)
            if (!isValidPassword(password)) {
                Toast.makeText(this, "비밀번호는 4자리 이상의 숫자만 가능합니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "모든 정보를 입력하세요.", Toast.LENGTH_SHORT).show();
            } else {
                // 회원가입 로직 (예: 데이터베이스에 저장)
                HabitDatabaseHelper dbHelper = new HabitDatabaseHelper(this);
                boolean isUserAdded = dbHelper.addUser(name, email, password);

                if (isUserAdded) {
                    Toast.makeText(this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                    finish(); // 회원가입 후 LoginActivity로 돌아감
                } else {
                    Toast.makeText(this, "회원가입 실패. 다시 시도하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // 이메일 형식 검증
    private boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // 비밀번호 형식 검증 (4자리 이상의 숫자만 가능)
    private boolean isValidPassword(String password) {
        String passwordPattern = "^\\d{4,}$";  // 4자리 이상의 숫자
        Pattern pattern = Pattern.compile(passwordPattern);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}


