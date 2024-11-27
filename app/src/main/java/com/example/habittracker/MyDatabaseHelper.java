package com.example.habittracker;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    // Database Name and Version
    private static final String DATABASE_NAME = "HabitTracker.db";
    private static final int DATABASE_VERSION = 3; // 새로운 테이블 구조에 따라 버전 증가

    // Constructor
    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Goal 테이블 생성
        db.execSQL("CREATE TABLE Goals (" +
                "goal_id INTEGER PRIMARY KEY AUTOINCREMENT, " + // 기본 키
//                "user_id INTEGER NOT NULL, " + // 사용자 ID
                "goal_name TEXT NOT NULL, " + // 목표 이름
                "creation_date DATE DEFAULT (date('now')), " + // 생성 날짜 (기본값: 오늘 날짜)
                "start_date DATE NOT NULL, " + // 시작 날짜
                "end_date DATE, " + // 종료 날짜 (옵션)
                "repeat_days TEXT NOT NULL, " + // 반복 요일
                "target_count INTEGER NOT NULL, " + // 하루 반복 횟수
                "reminder_enabled INTEGER NOT NULL DEFAULT 0);"); // 리마인더 활성화 여부 (0 = false, 1 = true)
    }

    // Upgrade Database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 기존 테이블 삭제 및 새로 생성
        db.execSQL("DROP TABLE IF EXISTS Goals");
        onCreate(db);
    }
}