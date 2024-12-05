package com.example.habittracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GoalDetailsActivity extends AppCompatActivity {
    private TextView tvHabitDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_details);

        // UI 요소 초기화
        tvHabitDetails = findViewById(R.id.tv_habit_details);

        // 데이터베이스에서 데이터를 가져와 TextView에 표시
        loadHabitsFromDatabase();
    }

    private void loadHabitsFromDatabase() {
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            // UserManager를 사용해 현재 로그인한 사용자 ID 가져오기
            UserManager userManager = new UserManager(this);
            String userId = userManager.getUserId();

            if (userId == null) {
                tvHabitDetails.setText("로그인 정보를 확인할 수 없습니다.");
                return;
            }

            db = dbHelper.getReadableDatabase();

            // Goal 테이블에서 현재 로그인한 사용자의 데이터만 조회
            cursor = db.rawQuery(
                    "SELECT goal_name FROM Goal WHERE user_id = ?",
                    new String[]{userId}
            );

            StringBuilder habitDetails = new StringBuilder();

            // 데이터가 없을 경우 처리
            if (cursor.getCount() == 0) {
                habitDetails.append("No habits found for the current user.\n");
            } else {
                // 데이터 읽기
                while (cursor.moveToNext()) {
                    String habitName = cursor.getString(cursor.getColumnIndex("goal_name"));
                    habitDetails.append("Habit Name: ").append(habitName).append("\n");
                }
            }
            tvHabitDetails.setText(habitDetails.toString());
        } catch (Exception e) {
            tvHabitDetails.setText("Error loading data: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }
}