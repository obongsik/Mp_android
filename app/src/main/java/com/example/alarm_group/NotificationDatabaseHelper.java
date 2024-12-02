package com.example.alarm_group;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NotificationDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "notifications.db";
    private static final int DATABASE_VERSION = 2; // 버전 업데이트 (1 -> 2)

    // Table and columns
    public static final String TABLE_REMINDER = "Reminder";
    public static final String COLUMN_ID = "reminder_id";
    public static final String COLUMN_GOAL_ID = "goal_id";
    public static final String COLUMN_TIME = "reminder_time";
    public static final String COLUMN_FREQUENCY = "frequency";
    public static final String COLUMN_IS_ACTIVE = "is_active";

    public NotificationDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Reminder table
        String CREATE_REMINDER_TABLE = "CREATE TABLE " + TABLE_REMINDER + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_GOAL_ID + " INTEGER NOT NULL, " +
                COLUMN_TIME + " TEXT NOT NULL, " +
                COLUMN_FREQUENCY + " INTEGER NOT NULL, " +
                COLUMN_IS_ACTIVE + " BOOLEAN DEFAULT 1)"; // is_active 기본값 추가
        db.execSQL(CREATE_REMINDER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 데이터베이스 버전이 1에서 2로 업그레이드되는 경우
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_REMINDER + " ADD COLUMN " + COLUMN_IS_ACTIVE + " BOOLEAN DEFAULT 1");
        }
    }
}
