package com.example.habittracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
public class AlarmSetting extends AppCompatActivity {

    // UI 요소 및 변수 선언
    private RadioGroup radioGroup; // 주간/일일 라디오 그룹
    private RadioButton rdoWeek, rdoDay; // 주간/일일 선택 라디오 버튼
    private Button btnMon, btnTue, btnWed, btnThu, btnFri, btnSat, btnSun; // 요일 버튼
    private TimePicker timePicker; // 시간 선택
    private TextView tvYear, tvMonth, tvDay, tvHour, tvMinute; // 날짜 및 시간 표시 텍스트
    private EditText etFrequency; // 빈도 입력 필드
    private Button btnSetAlarm, btnCancelAlarm; // 알림 설정 버튼, 알림 설정 취소 버튼

    private int[] daySelection = new int[7]; // 요일 선택 상태 저장 (0: 선택 해제, 1: 선택됨)
    private ArrayList<Button> dayButtons = new ArrayList<>(); // 요일 버튼 리스트

    private int goalId; // 전달받은 목표 ID
    private NotificationDatabaseHelper dbHelper; // 알림 데이터베이스 헬퍼 클래스

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        dbHelper = new NotificationDatabaseHelper(this); // 데이터베이스 초기화

        // 전달받은 목표 ID 확인
        goalId = getIntent().getIntExtra("goal_id", -1);
        if (goalId == -1) {
            // 목표 ID가 유효하지 않을 경우 처리
            Toast.makeText(this, "유효하지 않은 목표입니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeViews(); // UI 초기화
        setupListeners(); // 이벤트 리스너 설정
    }

    // UI 요소 초기화
    private void initializeViews() {
        radioGroup = findViewById(R.id.radioGroup);
        rdoWeek = findViewById(R.id.rdoWeek);
        rdoDay = findViewById(R.id.rdoDay);

        btnMon = findViewById(R.id.btnMon);
        btnTue = findViewById(R.id.btnTue);
        btnWed = findViewById(R.id.btnWed);
        btnThu = findViewById(R.id.btnThu);
        btnFri = findViewById(R.id.btnFri);
        btnSat = findViewById(R.id.btnSat);
        btnSun = findViewById(R.id.btnSun);

        timePicker = findViewById(R.id.timePicker1);
        timePicker.setIs24HourView(true); // 24시간 형식 설정

        tvYear = findViewById(R.id.tvYear);
        tvMonth = findViewById(R.id.tvMonth);
        tvDay = findViewById(R.id.tvDay);
        tvHour = findViewById(R.id.tvHour);
        tvMinute = findViewById(R.id.tvMinute);

        etFrequency = findViewById(R.id.etFrequency);
        btnSetAlarm = findViewById(R.id.btnSetAlarm);
        btnCancelAlarm = findViewById(R.id.btnCancelAlarm);

        // 요일 버튼 리스트 초기화
        dayButtons.add(btnMon);
        dayButtons.add(btnTue);
        dayButtons.add(btnWed);
        dayButtons.add(btnThu);
        dayButtons.add(btnFri);
        dayButtons.add(btnSat);
        dayButtons.add(btnSun);

        setViewVisibility(false, false); // 초기 UI 상태 설정
    }

    // 이벤트 리스너 설정
    private void setupListeners() {
        // 라디오 버튼 선택 이벤트
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rdoWeek) {
                setViewVisibility(true, true); // 주간 알림 설정 UI 표시
            } else if (checkedId == R.id.rdoDay) {
                setViewVisibility(true, false); // 일일 알림 설정 UI 표시
            }
        });

        // 요일 버튼 클릭 이벤트
        for (int i = 0; i < dayButtons.size(); i++) {
            int index = i;
            dayButtons.get(i).setOnClickListener(view -> toggleDaySelection(index, dayButtons.get(index)));
        }

        // 알림 설정 버튼 클릭 이벤트
        btnSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rdoWeek.isChecked()) {
                    setAlarms(); // 주간 알림 설정
                } else if (rdoDay.isChecked()) {
                    setDailyAlarm(); // 일일 알림 설정
                }
            }
        });

        //알람 설정 취소 구현
        btnCancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarmAndReturn();
            }
        });


        // 시간 선택 변경 이벤트
        timePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> updateDateTimeText());
    }

    // 데이터베이스에 알림 정보 저장
    private void saveAlarmToDatabase(String reminderTime, int frequency, boolean isActive) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NotificationDatabaseHelper.COLUMN_GOAL_ID, goalId);
        values.put(NotificationDatabaseHelper.COLUMN_TIME, reminderTime); // 알림 시간
        values.put(NotificationDatabaseHelper.COLUMN_FREQUENCY, frequency); // 빈도
        values.put("is_active", isActive ? 1 : 0); // 활성 상태

        long rowId = db.insert(NotificationDatabaseHelper.TABLE_REMINDER, null, values);
        if (rowId > 0) {
            Toast.makeText(this, "알림 설정이 저장되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    // UI 가시성 설정
    private void setViewVisibility(boolean showAlarmSettings, boolean showDayButtons) {
        int alarmVisibility = showAlarmSettings ? View.VISIBLE : View.GONE;
        int dayVisibility = showDayButtons ? View.VISIBLE : View.GONE;

        timePicker.setVisibility(alarmVisibility);
        etFrequency.setVisibility(alarmVisibility);
        btnSetAlarm.setVisibility(alarmVisibility);

        for (Button button : dayButtons) {
            button.setVisibility(dayVisibility);
        }
    }

    // 요일 버튼 선택 상태 변경
    private void toggleDaySelection(int index, Button button) {
        daySelection[index] = 1 - daySelection[index]; // 선택 상태 반전
        button.setBackgroundColor(daySelection[index] == 1 ? Color.GREEN : Color.LTGRAY); // 버튼 색상 변경
    }

    // 주간 알림 설정
    private void setAlarms() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        for (int i = 0; i < daySelection.length; i++) {
            if (daySelection[i] == 1) { // 선택된 요일만 처리
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_WEEK, getDayOfWeek(i));
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);

                if (calendar.before(Calendar.getInstance())) {
                    calendar.add(Calendar.WEEK_OF_YEAR, 1); // 이미 지난 시간은 다음 주로 설정
                }

                Intent intent = new Intent(this, AlarmReceiver.class);
                intent.putExtra("goal_id", goalId); // 목표 ID 전달
                intent.putExtra("day", dayButtons.get(i).getText().toString());
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        this, goalId * 10 + i, intent, PendingIntent.FLAG_UPDATE_CURRENT
                );

                if (alarmManager != null) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }
            }
        }
    }

    // 일일 알림 설정
    private void setDailyAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DAY_OF_YEAR, 1); // 이미 지난 시간은 다음 날로 설정
        }

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("goal_id", goalId); // 목표 ID 전달
        intent.putExtra("type", "daily");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, goalId, intent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

        // 데이터베이스에 저장
        String time = String.format("%02d:%02d", hour, minute);
        saveAlarmToDatabase(time, 1, true);
    }

    // 인덱스에 따른 요일 반환
    private int getDayOfWeek(int index) {
        return Calendar.MONDAY + index; // 인덱스와 Calendar 요일 매칭
    }

    // 현재 날짜 및 시간 갱신
    private void updateDateTimeText() {
        Calendar calendar = Calendar.getInstance();
        tvYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));
        tvMonth.setText(String.format("%02d", calendar.get(Calendar.MONTH) + 1));
        tvDay.setText(String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)));
    }

    //알람설정 취소 함수
    private void cancelAlarmAndReturn() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = NotificationDatabaseHelper.COLUMN_GOAL_ID + " = ?";
        String[] whereArgs = {String.valueOf(goalId)};

        ContentValues values = new ContentValues();
        values.put("is_active", 0);
        db.update(NotificationDatabaseHelper.TABLE_REMINDER, values, whereClause, whereArgs);

        Toast.makeText(this, "알림 설정이 취소되었습니다.", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, AddGoalActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
