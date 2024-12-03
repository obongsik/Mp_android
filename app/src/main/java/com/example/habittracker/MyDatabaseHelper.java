package com.example.habittracker;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    // Database Name and Version
    private static final String DATABASE_NAME = "HabitTracker.db";
    private static final int DATABASE_VERSION = 6; // 새로운 테이블 구조에 따라 버전 증가

    // Constructor
    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 외래 키 활성화
        db.execSQL("PRAGMA foreign_keys=ON;");

        // User Table
        db.execSQL("CREATE TABLE User (" +
                "user_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL, " +
                "email TEXT NOT NULL, " +  // 이메일 컬럼 추가
                "password TEXT NOT NULL);");

        // Goal Table
        db.execSQL("CREATE TABLE Goal (" +
                "goal_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER NOT NULL, " +
                "goal_name TEXT NOT NULL, " +
                "creation_date DATE NOT NULL DEFAULT (date('now')), " +
                "start_date DATE NOT NULL, " +
                "end_date DATE, " +
                "repeat_days TEXT NOT NULL, " + // 반복 요일
                "target_count INTEGER NOT NULL, " + // 하루 반복 횟수
                "reminder_enabled INTEGER NOT NULL, " +
                "FOREIGN KEY (user_id) REFERENCES User(user_id));");

        // Achievement_Check Table
        db.execSQL("CREATE TABLE Achievement_Check (" +
                "achievement_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "goal_id INTEGER NOT NULL, " +
                "check_date DATE NOT NULL, " +
                "achieved INTEGER NOT NULL, " +
                "FOREIGN KEY (goal_id) REFERENCES Goal(goal_id));");

        // Statistics Table
        db.execSQL("CREATE TABLE Statistics (" +
                "statistics_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER NOT NULL, " +
                "goal_id INTEGER NOT NULL, " +
                "total_goal_days INTEGER NOT NULL, " +
                "achieved_days INTEGER NOT NULL, " +
                "achievement_rate REAL NOT NULL, " + // DECIMAL(5,2)를 REAL로 변환
                "FOREIGN KEY (user_id) REFERENCES User(user_id), " +
                "FOREIGN KEY (goal_id) REFERENCES Goal(goal_id));");

        // Reminder Table
        db.execSQL("CREATE TABLE Reminder (" +
                "reminder_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "goal_id INTEGER NOT NULL, " +
                "reminder_time TIME NOT NULL, " +
                "frequency TEXT NOT NULL, " +
                "is_active INTEGER NOT NULL, " +
                "FOREIGN KEY (goal_id) REFERENCES Goal(goal_id));");
    }


    // Upgrade Database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 기존 테이블 삭제
        db.execSQL("DROP TABLE IF EXISTS Reminder");
        db.execSQL("DROP TABLE IF EXISTS Statistics");
        db.execSQL("DROP TABLE IF EXISTS Achievement_Check");
        db.execSQL("DROP TABLE IF EXISTS Goal");
        db.execSQL("DROP TABLE IF EXISTS User");

        // 새 테이블 생성
        onCreate(db);
    }

}