package com.example.habittracker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

public class HabitDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "habit.db";  // 데이터베이스 이름
    private static final int DATABASE_VERSION = 1;  // 데이터베이스 버전

    // 테이블 생성 쿼리
    private static final String CREATE_USER_TABLE =
            "CREATE TABLE IF NOT EXISTS User (" +
                    "user_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT NOT NULL, " +
                    "email TEXT NOT NULL UNIQUE, " +
                    "password TEXT NOT NULL);";

    private static final String CREATE_GOAL_TABLE =
            "CREATE TABLE IF NOT EXISTS Goal (" +
                    "goal_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_id INTEGER NOT NULL, " +
                    "goal_name TEXT NOT NULL, " +
                    "creation_date TEXT NOT NULL, " +
                    "start_date TEXT NOT NULL, " +
                    "end_date TEXT, " +
                    "reminder_enabled INTEGER NOT NULL, " +
                    "FOREIGN KEY (user_id) REFERENCES User(user_id));";

    private static final String CREATE_ACHIEVEMENT_CHECK_TABLE =
            "CREATE TABLE IF NOT EXISTS Achievement_Check (" +
                    "achievement_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "goal_id INTEGER NOT NULL, " +
                    "check_date TEXT NOT NULL, " +
                    "achieved INTEGER NOT NULL, " +
                    "weekly_achievement_count INTEGER NOT NULL DEFAULT 0, " +
                    "FOREIGN KEY (goal_id) REFERENCES Goal(goal_id));";

    private static final String CREATE_STATISTICS_TABLE =
            "CREATE TABLE IF NOT EXISTS Statistics (" +
                    "statistics_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_id INTEGER NOT NULL, " +
                    "goal_id INTEGER NOT NULL, " +
                    "total_goal_days INTEGER NOT NULL, " +
                    "achieved_days INTEGER NOT NULL, " +
                    "achievement_rate DECIMAL(5,2) NOT NULL, " +
                    "FOREIGN KEY (user_id) REFERENCES User(user_id), " +
                    "FOREIGN KEY (goal_id) REFERENCES Goal(goal_id));";

    private static final String CREATE_REMINDER_TABLE =
            "CREATE TABLE IF NOT EXISTS Reminder (" +
                    "reminder_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "goal_id INTEGER NOT NULL, " +
                    "reminder_time TEXT NOT NULL, " +
                    "frequency TEXT NOT NULL, " +
                    "is_active INTEGER NOT NULL, " +
                    "FOREIGN KEY (goal_id) REFERENCES Goal(goal_id));";

    // 생성자
    public HabitDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 데이터베이스 생성
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_GOAL_TABLE);
        db.execSQL(CREATE_ACHIEVEMENT_CHECK_TABLE);
        db.execSQL(CREATE_STATISTICS_TABLE);
        db.execSQL(CREATE_REMINDER_TABLE);
    }

    // 데이터베이스 업그레이드
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 데이터베이스 업그레이드 시 기존 데이터 삭제 후 재생성
        db.execSQL("DROP TABLE IF EXISTS User");
        db.execSQL("DROP TABLE IF EXISTS Goal");
        db.execSQL("DROP TABLE IF EXISTS Achievement_Check");
        db.execSQL("DROP TABLE IF EXISTS Statistics");
        db.execSQL("DROP TABLE IF EXISTS Reminder");
        onCreate(db);
    }

    // 회원가입: 사용자 정보 삽입
    public boolean addUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("email", email);
        values.put("password", password);

        long result = db.insert("User", null, values);  // 사용자 정보 삽입
        db.close();

        return result != -1; // 성공적으로 삽입되었으면 true 반환
    }

    // 로그인: 사용자가 입력한 이메일과 비밀번호로 사용자 정보 조회
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM User WHERE email = ? AND password = ?";
        Cursor cursor = db.rawQuery(query, new String[] {email, password});

        boolean isValid = cursor.getCount() > 0;  // 이메일과 비밀번호가 일치하는 사용자가 있으면 true
        cursor.close();
        db.close();

        return isValid;  // 유효한 사용자면 true, 아니면 false
    }

    // 주별 성취도 데이터 조회 (예시: week과 achievement_count를 반환)
    public Cursor getWeeklyAchievementData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT strftime('%W', check_date) AS week, SUM(achieved) AS achievement_count FROM Achievement_Check GROUP BY week";
        return db.rawQuery(query, null);
    }

    // 월별 성취도 데이터 조회 (예시: month과 achievement_count를 반환)
    public Cursor getMonthlyAchievementData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT strftime('%m', check_date) AS month, SUM(achieved) AS achievement_count FROM Achievement_Check GROUP BY month";
        return db.rawQuery(query, null);
    }
}


