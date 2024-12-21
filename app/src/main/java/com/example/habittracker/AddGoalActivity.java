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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AddGoalActivity extends AppCompatActivity {
    // UI 요소 선언
    private EditText etGoalName, etTargetCount, etStartDate, etEndDate; // 목표 이름, 반복 횟수, 시작/종료 날짜 입력 필드
    private Switch swReminderEnabled; // 알림 활성화 여부 스위치
    private CheckBox cbMonday, cbTuesday, cbWednesday, cbThursday, cbFriday, cbSaturday, cbSunday; // 요일 선택 체크박스
    private Button btnSaveHabit, btnViewGoals, btnViewStatistics; // 목표 저장 버튼
    private MyDatabaseHelper dbHelper; // 데이터베이스 헬퍼 클래스
    private UserManager userManager; // 로그인 상태 및 사용자 정보를 관리하는 클래스
    private RecyclerView goalRecyclerView;
    private com.example.habittracker.GoalAdapter goalAdapter;
    private ArrayList<String> goalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);

        // UserManager 초기화
        userManager = new UserManager(this);

        // 로그인 상태 확인
        if (!userManager.isLoggedIn()) {
            // 로그인이 안 되어 있으면 메시지 표시 후 액티비티 종료
            Toast.makeText(this, "로그인 상태가 아닙니다. 다시 로그인해주세요.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

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
        btnViewGoals = findViewById(R.id.btn_view_your_goals);
        btnViewStatistics = findViewById(R.id.btn_view_statistics);

        dbHelper = new MyDatabaseHelper(this);

        goalList = new ArrayList<>();
        goalAdapter = new com.example.habittracker.GoalAdapter(goalList);

        goalRecyclerView = findViewById(R.id.goalRecyclerView);
        goalRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        goalRecyclerView.setAdapter(goalAdapter);

        // 저장 버튼 클릭 이벤트
        btnSaveHabit.setOnClickListener(v -> {
            int goalId = saveHabit(); // 목표 저장 및 goal_id 반환
            if (goalId != -1) {

                goalList.add(0, etGoalName.getText().toString() + etTargetCount.getText().toString() + " 번 반복");
                goalAdapter.notifyItemInserted(0); // RecyclerView 업데이트
                // RecyclerView를 맨 위로 스크롤
                goalRecyclerView.scrollToPosition(0);

                // 추가적으로 스위치가 켜져 있을 경우 AlarmSetting으로 이동
                if (swReminderEnabled.isChecked()) {
                    Intent alarmIntent = new Intent(AddGoalActivity.this, AlarmSetting.class);
                    alarmIntent.putExtra("goal_id", goalId); // 생성된 goal_id 전달
                    startActivity(alarmIntent);
                }
            } else {
                // 저장 실패 시 오류 메시지 출력
                Toast.makeText(this, "목표 저장에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        btnViewGoals.setOnClickListener(v -> {
            Intent intent = new Intent(AddGoalActivity.this, GoalDetailsActivity.class);
            startActivity(intent);
            finish();
        });

        btnViewStatistics.setOnClickListener(v -> {
            Intent intent = new Intent(AddGoalActivity.this, StatisticsActivity.class);
            startActivity(intent);
            finish();
        });
    }


    // 요일 데이터 가져오기 메소드
    // 각 요일의 선택 상태를 "1" 또는 "0" 형식의 문자열로 변환
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
    // 목표 데이터를 데이터베이스에 저장하고, 생성된 goal_id를 반환
    private int saveHabit() {
        String userId = userManager.getUserId();
        if (userId == null) {
            // 로그인이 안 되어 있을 경우 처리
            Toast.makeText(this, "사용자 정보를 확인할 수 없습니다. 다시 로그인해주세요.", Toast.LENGTH_SHORT).show();
            return -1; // 실패 시 -1 반환
        }

        // 사용자가 입력한 목표 데이터 가져오기
        String goalName = etGoalName.getText().toString().trim();
        String startDate = etStartDate.getText().toString().trim();
        String endDate = etEndDate.getText().toString().trim();
        String repeatDays = getRepeatDays();
        int targetCount;

        // 반복 횟수 입력값 확인 및 변환
        try {
            targetCount = Integer.parseInt(etTargetCount.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "올바른 반복 횟수를 입력하세요!", Toast.LENGTH_SHORT).show();
            return -1; // 실패 시 -1 반환
        }

        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("user_id", userId); // 사용자 ID 저장
            values.put("goal_name", goalName); // 목표 이름 저장
            values.put("start_date", startDate); // 시작 날짜 저장
            values.put("end_date", endDate.isEmpty() ? null : endDate); // 종료 날짜 저장 (비어 있으면 null 처리)
            values.put("repeat_days", repeatDays); // 반복 요일 데이터 저장
            values.put("target_count", targetCount); // 반복 횟수 저장
            values.put("reminder_enabled", swReminderEnabled.isChecked() ? 1 : 0); // 알림 활성화 여부 저장

            // 데이터 저장 및 ID 반환
            long result = db.insert("Goal", null, values);
            if (result == -1) {
                Toast.makeText(this, "목표 저장에 실패했습니다!", Toast.LENGTH_SHORT).show();
                return -1; // 저장 실패
            } else {
                Toast.makeText(this, "목표가 저장되었습니다!", Toast.LENGTH_SHORT).show();
                return (int) result; // 생성된 goal_id 반환
            }
        } catch (Exception e) {
            // 데이터베이스 작업 중 오류 처리
            Toast.makeText(this, "Database error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return -1;
        } finally {
            if (db != null) db.close(); // 데이터베이스 연결 종료
        }
    }

    // 날짜 유효성 검사
    // 입력된 날짜 문자열이 "YYYY-MM-DD" 형식인지 확인
    private boolean isValidDate(String date) {
        String regex = "\\d{4}-\\d{2}-\\d{2}"; // 정규식 패턴
        return date.matches(regex);
    }
}
