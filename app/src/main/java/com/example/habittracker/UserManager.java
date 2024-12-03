package com.example.habittracker;

import android.content.Context;
import android.content.SharedPreferences;

public class UserManager {
    private static final String PREF_NAME = "UserPrefs";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public UserManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // 사용자 정보 저장
    public void saveUser(String userId, String userName, String email, String password) {
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_USER_NAME, userName);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    // 로그인 상태 확인
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // 로그아웃 처리
    public void logout() {
        editor.putBoolean(KEY_IS_LOGGED_IN, false);
        editor.apply();
    }

    // 사용자 ID 가져오기
    public String getUserId() {
        return sharedPreferences.getString(KEY_USER_ID, null);
    }

    // 사용자 이름 가져오기
    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, null);
    }

    // 사용자 이메일 가져오기
    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, null);
    }
}
