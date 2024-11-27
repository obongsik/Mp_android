package com.example.habittracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GoalDetailsActivity extends AppCompatActivity {
    //데이터 전송 확인 용 임시파일

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
            db = dbHelper.getReadableDatabase();

            // "Goals" 테이블에서 goal_name 데이터 조회 (정렬 없이)
            cursor = db.rawQuery("SELECT goal_name FROM Goals", null);

            StringBuilder habitDetails = new StringBuilder();

            // 데이터가 없을 경우 처리
            if (cursor.getCount() == 0) {
                habitDetails.append("No habits found.\n");
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
