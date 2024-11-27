package com.example.habittracker;


import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddGoalActivity extends AppCompatActivity {
    // UI 요소 선언
    private EditText etGoalName, etTargetCount, etStartDate, etEndDate;
    private Switch swReminderEnabled;
    private CheckBox cbMonday, cbTuesday, cbWednesday, cbThursday, cbFriday, cbSaturday, cbSunday;
    private Button btnSaveHabit;
    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);

        // UI 요소 초기화
        etGoalName = findViewById(R.id.et_goal_name);
        etTargetCount = findViewById(R.id.et_target_count);
        etStartDate = findViewById(R.id.et_start_date);
        etEndDate = findViewById(R.id.et_end_date);
        swReminderEnabled = findViewById(R.id.sw_reminder_enabled);
        cbMonday = findViewById(R.id.cb_monday);
        cbTuesday = findViewById(R.id.cb_tuesday);
        cbWednesday = findViewById(R.id.cb_wednesday);
        cbThursday = findViewById(R.id.cb_thursday);
        cbFriday = findViewById(R.id.cb_friday);
        cbSaturday = findViewById(R.id.cb_saturday);
        cbSunday = findViewById(R.id.cb_sunday);
        btnSaveHabit = findViewById(R.id.btn_save_habit);
        dbHelper = new MyDatabaseHelper(this);

        // 저장 버튼 클릭 이벤트
        btnSaveHabit.setOnClickListener(v -> {
            saveHabit();
            Intent intent = new Intent(AddGoalActivity.this, GoalDetailsActivity.class);
            startActivity(intent);
        });

    }

    // 요일 데이터 가져오기 메서드
    private String getRepeatDays() {
        String repeatDays = (cbMonday.isChecked() ? "1," : "0,") +
                (cbTuesday.isChecked() ? "1," : "0,") +
                (cbWednesday.isChecked() ? "1," : "0,") +
                (cbThursday.isChecked() ? "1," : "0,") +
                (cbFriday.isChecked() ? "1," : "0,") +
                (cbSaturday.isChecked() ? "1," : "0,") +
                (cbSunday.isChecked() ? "1" : "0"); // 마지막 값은 콤마 없이 추가
        return repeatDays;
    }


    // 습관 저장 메서드
    private void saveHabit() {
        String goalName = etGoalName.getText().toString().trim();
        String repeatDays = getRepeatDays();
        String startDate = etStartDate.getText().toString().trim();
        String endDate = etEndDate.getText().toString().trim();
        boolean reminderEnabled = swReminderEnabled.isChecked();
        int targetCount;

        // 데이터 유효성 검사
        if (goalName.isEmpty()) {
            Toast.makeText(this, "목표 이름을 입력하세요!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (startDate.isEmpty()) {
            Toast.makeText(this, "시작 날짜를 입력하세요!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidDate(startDate)) {
            Toast.makeText(this, "올바른 시작 날짜 형식이 아닙니다! (YYYY-MM-DD)", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!endDate.isEmpty() && !isValidDate(endDate)) {
            Toast.makeText(this, "올바른 종료 날짜 형식이 아닙니다! (YYYY-MM-DD)", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            targetCount = Integer.parseInt(etTargetCount.getText().toString().trim());
            if (targetCount <= 0) {
                Toast.makeText(this, "반복 횟수는 1 이상이어야 합니다!", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "올바른 반복 횟수를 입력하세요!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 데이터베이스 저장
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("goal_name", goalName);
            values.put("repeat_days", repeatDays); // 반복 요일
            values.put("start_date", startDate);
            values.put("end_date", endDate.isEmpty() ? null : endDate);
            values.put("reminder_enabled", reminderEnabled ? 1 : 0);
            values.put("target_count", targetCount); // 하루 반복 횟수

            long result = db.insert("Goals", null, values);
            if (result == -1) {
                Toast.makeText(this, "목표 저장에 실패했습니다!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "목표가 저장되었습니다!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Database error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if (db != null) db.close();
        }
    }

    private boolean isValidDate(String date) {
        String regex = "\\d{4}-\\d{2}-\\d{2}";
        return date.matches(regex);
    }
}