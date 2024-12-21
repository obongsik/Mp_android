package com.example.habittracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.ArrayList;

public class GoalDetailsActivity extends AppCompatActivity {
    private RecyclerView goalRecyclerView;
    private GoalAdapter goalAdapter;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_details);

        // UI 요소 초기화
        goalRecyclerView = findViewById(R.id.goalRecyclerView);
        back = findViewById(R.id.btn_back);

        goalRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        goalAdapter = new GoalAdapter(new ArrayList<>());
        goalRecyclerView.setAdapter(goalAdapter);


        // 데이터베이스에서 데이터를 가져와 TextView에 표시
        loadHabitsFromDatabase();

        back.setOnClickListener(v -> {
            // AddGoalActivity로 이동하는 Intent 생성
            Intent intent = new Intent(GoalDetailsActivity.this, AddGoalActivity.class);
            startActivity(intent);
            finish();
        });
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
                return;
            }

            db = dbHelper.getReadableDatabase();

            // Goal 테이블에서 현재 로그인한 사용자의 데이터만 조회
            cursor = db.rawQuery(
                    "SELECT goal_name FROM Goal WHERE user_id = ?",
                    new String[]{userId}
            );

            List<String> goalList = new ArrayList<>();

            // 데이터 읽기
            while (cursor.moveToNext()) {
                int columnIndex = cursor.getColumnIndex("goal_name");
                if (columnIndex != -1) {
                    String habitName = cursor.getString(columnIndex);
                    goalList.add(habitName);
                }
            }

            // 어댑터에 데이터 설정
            goalAdapter = new GoalAdapter(new ArrayList<>(goalList));
            goalRecyclerView.setAdapter(goalAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }
}