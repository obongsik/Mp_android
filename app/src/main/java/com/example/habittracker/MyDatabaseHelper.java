package com.example.habittracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    // Database Name and Version
    private static final String DATABASE_NAME = "HabitTracker.db";
    private static final int DATABASE_VERSION = 6; // Increment as per new changes

    // Table and Columns for User
    public static final String TABLE_USER = "User";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";

    // Table and Columns for Goal
    public static final String TABLE_GOAL = "Goal";
    public static final String COLUMN_GOAL_ID = "goal_id";
    public static final String COLUMN_GOAL_NAME = "goal_name";
    public static final String COLUMN_CREATION_DATE = "creation_date";
    public static final String COLUMN_START_DATE = "start_date";
    public static final String COLUMN_END_DATE = "end_date";
    public static final String COLUMN_REPEAT_DAYS = "repeat_days";
    public static final String COLUMN_TARGET_COUNT = "target_count";
    public static final String COLUMN_REMINDER_ENABLED = "reminder_enabled";

    // Table and Columns for Achievement_Check
    public static final String TABLE_ACHIEVEMENT_CHECK = "Achievement_Check";
    public static final String COLUMN_ACHIEVEMENT_ID = "achievement_id";
    public static final String COLUMN_CHECK_DATE = "check_date";
    public static final String COLUMN_ACHIEVED = "achieved";

    // Table and Columns for Statistics
    public static final String TABLE_STATISTICS = "Statistics";
    public static final String COLUMN_STATISTICS_ID = "statistics_id";
    public static final String COLUMN_TOTAL_GOAL_DAYS = "total_goal_days";
    public static final String COLUMN_ACHIEVED_DAYS = "achieved_days";
    public static final String COLUMN_ACHIEVEMENT_RATE = "achievement_rate";

    // Table and Columns for Reminder
    public static final String TABLE_REMINDER = "Reminder";
    public static final String COLUMN_REMINDER_ID = "reminder_id";
    public static final String COLUMN_REMINDER_TIME = "reminder_time";
    public static final String COLUMN_FREQUENCY = "frequency";
    public static final String COLUMN_IS_ACTIVE = "is_active";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Enable foreign keys
        db.execSQL("PRAGMA foreign_keys=ON;");

        // Create User table
        db.execSQL("CREATE TABLE " + TABLE_USER + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT NOT NULL, " +
                COLUMN_EMAIL + " TEXT NOT NULL, " +
                COLUMN_PASSWORD + " TEXT NOT NULL);");

        // Create Goal table
        db.execSQL("CREATE TABLE " + TABLE_GOAL + " (" +
                COLUMN_GOAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_ID + " INTEGER NOT NULL, " +
                COLUMN_GOAL_NAME + " TEXT NOT NULL, " +
                COLUMN_CREATION_DATE + " DATE NOT NULL DEFAULT (date('now')), " +
                COLUMN_START_DATE + " DATE NOT NULL, " +
                COLUMN_END_DATE + " DATE, " +
                COLUMN_REPEAT_DAYS + " TEXT NOT NULL, " +
                COLUMN_TARGET_COUNT + " INTEGER NOT NULL, " +
                COLUMN_REMINDER_ENABLED + " INTEGER NOT NULL, " +
                "FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_USER_ID + "));");

        // Create Achievement_Check table
        db.execSQL("CREATE TABLE " + TABLE_ACHIEVEMENT_CHECK + " (" +
                COLUMN_ACHIEVEMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_GOAL_ID + " INTEGER NOT NULL, " +
                COLUMN_CHECK_DATE + " DATE NOT NULL, " +
                COLUMN_ACHIEVED + " INTEGER NOT NULL, " +
                "FOREIGN KEY (" + COLUMN_GOAL_ID + ") REFERENCES " + TABLE_GOAL + "(" + COLUMN_GOAL_ID + "));");

        // Create Statistics table
        db.execSQL("CREATE TABLE " + TABLE_STATISTICS + " (" +
                COLUMN_STATISTICS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_ID + " INTEGER NOT NULL, " +
                COLUMN_GOAL_ID + " INTEGER NOT NULL, " +
                COLUMN_TOTAL_GOAL_DAYS + " INTEGER NOT NULL, " +
                COLUMN_ACHIEVED_DAYS + " INTEGER NOT NULL, " +
                COLUMN_ACHIEVEMENT_RATE + " REAL NOT NULL, " +
                "FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_USER_ID + "), " +
                "FOREIGN KEY (" + COLUMN_GOAL_ID + ") REFERENCES " + TABLE_GOAL + "(" + COLUMN_GOAL_ID + "));");

        // Create Reminder table
        db.execSQL("CREATE TABLE " + TABLE_REMINDER + " (" +
                COLUMN_REMINDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_GOAL_ID + " INTEGER NOT NULL, " +
                COLUMN_REMINDER_TIME + " TEXT NOT NULL, " +
                COLUMN_FREQUENCY + " TEXT NOT NULL, " +
                COLUMN_IS_ACTIVE + " INTEGER DEFAULT 1, " +
                "FOREIGN KEY (" + COLUMN_GOAL_ID + ") REFERENCES " + TABLE_GOAL + "(" + COLUMN_GOAL_ID + "));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            // Drop old tables
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATISTICS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACHIEVEMENT_CHECK);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_GOAL);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
            // Recreate tables
            onCreate(db);
        }
    }
}
