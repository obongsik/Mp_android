package com.example.habittracker;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GoalcheckMainActivity extends AppCompatActivity {
    private EditText etHabitName, etTargetCount;
    private CheckBox cbMonday, cbTuesday;
    private Button btnSaveHabit;
    private RecyclerView goalRecyclerView;
    private com.example.habittracker.GoalAdapter goalAdapter;
    private ArrayList<String> goalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 목표 리스트 초기화
        goalList = new ArrayList<>();
        goalAdapter = new com.example.habittracker.GoalAdapter(goalList);

        // RecyclerView 설정
        goalRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        goalRecyclerView.setAdapter(goalAdapter);

        // 저장 버튼 클릭 이벤트
        btnSaveHabit.setOnClickListener(v -> {
            String habitName = etHabitName.getText().toString();
            int targetCount = Integer.parseInt(etTargetCount.getText().toString());
            String repeatDays = getRepeatDays(); // 요일 선택 데이터 처리

            // 데이터 유효성 검사
            if (habitName.isEmpty() || targetCount <= 0) {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                return;
            }

            // 목표 추가
            goalList.add(0, habitName);
            goalAdapter.notifyItemInserted(0); // RecyclerView 업데이트
            // RecyclerView를 맨 위로 스크롤
            goalRecyclerView.scrollToPosition(0);
            etHabitName.setText("");
            etTargetCount.setText("");// 입력란 초기화

            // 데이터베이스에 저장
            saveHabitToDatabase(habitName, repeatDays, targetCount);
        });
    }

    private String getRepeatDays() {
        // 요일 선택 데이터를 처리 (예: "1,0,1,0,0,0,1")
        StringBuilder repeatDays = new StringBuilder();
        repeatDays.append(cbMonday.isChecked() ? "1," : "0,");
        repeatDays.append(cbTuesday.isChecked() ? "1," : "0,");
        // 다른 요일도 적기
        return repeatDays.toString();
    }

    private void saveHabitToDatabase(String name, String repeatDays, int targetCount) {
        // SQLite 통해 데이터베이스에 저장 구현
        // Habit habit = new Habit(name, repeatDays, targetCount);

        Toast.makeText(this, "Habit Saved!", Toast.LENGTH_SHORT).show();
    }
}