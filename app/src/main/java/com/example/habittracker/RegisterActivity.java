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
    private Button backButton;

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
        backButton = findViewById(R.id.btn_back); // 뒤로가기 버튼

        // 비밀번호 입력 시 도움말 텍스트 보이기
        passwordEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                passwordInputLayout.setHelperText("4자리 이상의 숫자만 가능합니다.");
            } else {
                passwordInputLayout.setHelperTextEnabled(false);
            }
        });

        // 회원가입 버튼 클릭 리스너
        signUpButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (!isValidEmail(email)) {
                Toast.makeText(this, "이메일 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidPassword(password)) {
                Toast.makeText(this, "비밀번호는 4자리 이상의 숫자만 가능합니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "모든 정보를 입력하세요.", Toast.LENGTH_SHORT).show();
            } else {
                HabitDatabaseHelper dbHelper = new HabitDatabaseHelper(this);
                boolean isUserAdded = dbHelper.addUser(name, email, password);

                if (isUserAdded) {
                    Toast.makeText(this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "회원가입 실패. 다시 시도하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 뒤로가기 버튼 클릭 리스너
        backButton.setOnClickListener(v -> {
            finish(); // RegisterActivity 종료
        });
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidPassword(String password) {
        String passwordPattern = "^\\d{4,}$";  // 4자리 이상의 숫자
        Pattern pattern = Pattern.compile(passwordPattern);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}



