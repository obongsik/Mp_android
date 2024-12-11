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

    private RadioGroup radioGroup;
    private RadioButton rdoWeek, rdoDay;
    private Button btnMon, btnTue, btnWed, btnThu, btnFri, btnSat, btnSun;
    private TimePicker timePicker;
    private TextView tvYear, tvMonth, tvDay, tvHour, tvMinute;
    private EditText etFrequency;
    private Button btnSetAlarm;

    private int[] daySelection = new int[7]; // 요일 선택 상태 저장 (0: 선택 해제, 1: 선택됨)
    private ArrayList<Button> dayButtons = new ArrayList<>(); // 요일 버튼 리스트

    private int goalId; // 전달받은 목표 ID
    private NotificationDatabaseHelper dbHelper; // 데이터베이스 헬퍼 클래스

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        dbHelper = new NotificationDatabaseHelper(this);

        // 전달받은 목표 ID 확인
        goalId = getIntent().getIntExtra("goal_id", -1);
        if (goalId == -1) {
            Toast.makeText(this, "유효하지 않은 목표입니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        // UI 요소 초기화
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
        timePicker.setIs24HourView(true);

        tvYear = findViewById(R.id.tvYear);
        tvMonth = findViewById(R.id.tvMonth);
        tvDay = findViewById(R.id.tvDay);
        tvHour = findViewById(R.id.tvHour);
        tvMinute = findViewById(R.id.tvMinute);

        etFrequency = findViewById(R.id.etFrequency);
        btnSetAlarm = findViewById(R.id.btnSetAlarm);

        // 요일 버튼 리스트 초기화
        dayButtons.add(btnMon);
        dayButtons.add(btnTue);
        dayButtons.add(btnWed);
        dayButtons.add(btnThu);
        dayButtons.add(btnFri);
        dayButtons.add(btnSat);
        dayButtons.add(btnSun);

        setViewVisibility(false, false);
    }

    private void setupListeners() {
        // 라디오 버튼 선택 이벤트
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rdoWeek) {
                setViewVisibility(true, true);
            } else if (checkedId == R.id.rdoDay) {
                setViewVisibility(true, false);
            }
        });

        // 요일 버튼 클릭 이벤트
        for (int i = 0; i < dayButtons.size(); i++) {
            int index = i;
            dayButtons.get(i).setOnClickListener(view -> toggleDaySelection(index, dayButtons.get(index)));
        }

        // 알림 설정 버튼
        btnSetAlarm.setOnClickListener(v -> {
            if (rdoWeek.isChecked()) {
                setAlarms(); // 요일별 알람 설정
            } else if (rdoDay.isChecked()) {
                setDailyAlarm(); // 매일 알람 설정
            }

            // 알림 설정 완료 후 GoalDetailsActivity로 이동
            Intent intent = new Intent(AlarmSetting.this, GoalDetailsActivity.class);
            intent.putExtra("goal_id", goalId); // goal_id 전달 (선택 사항)
            startActivity(intent);
            finish(); // 현재 Activity 종료
        });

        timePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> updateDateTimeText());
    }

    private void saveAlarmToDatabase(String reminderTime, int frequency, boolean isActive) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NotificationDatabaseHelper.COLUMN_GOAL_ID, goalId);
        values.put(NotificationDatabaseHelper.COLUMN_TIME, reminderTime);
        values.put(NotificationDatabaseHelper.COLUMN_FREQUENCY, frequency);
        values.put("is_active", isActive ? 1 : 0);

        long rowId = db.insert(NotificationDatabaseHelper.TABLE_REMINDER, null, values);
        if (rowId > 0) {
            Toast.makeText(this, "알림 설정이 저장되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }

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

    private void toggleDaySelection(int index, Button button) {
        daySelection[index] = 1 - daySelection[index];
        button.setBackgroundColor(daySelection[index] == 1 ? Color.GREEN : Color.LTGRAY);
    }

    private void setAlarms() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        for (int i = 0; i < daySelection.length; i++) {
            if (daySelection[i] == 1) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_WEEK, getDayOfWeek(i));
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);

                if (calendar.before(Calendar.getInstance())) {
                    calendar.add(Calendar.WEEK_OF_YEAR, 1);
                }

                Intent intent = new Intent(this, AlarmReceiver.class);
                intent.putExtra("goal_id", goalId);
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

    private void setDailyAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("goal_id", goalId);
        intent.putExtra("type", "daily");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, goalId, intent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

        String time = String.format("%02d:%02d", hour, minute);
        saveAlarmToDatabase(time, 1, true);
    }

    private int getDayOfWeek(int index) {
        return Calendar.MONDAY + index;
    }

    private void updateDateTimeText() {
        Calendar calendar = Calendar.getInstance();
        tvYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));
        tvMonth.setText(String.format("%02d", calendar.get(Calendar.MONTH) + 1));
        tvDay.setText(String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)));
    }
}
