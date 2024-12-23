package com.example.habittracker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // 전달받은 데이터 추출
        int goalId = intent.getIntExtra("goal_id", -1);
        String day = intent.getStringExtra("day");
        String type = intent.getStringExtra("type");

        // 유효성 검사
        if (!isValidGoalId(goalId)) {
            return;
        }

        // 알림 생성
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "alarm_channel";

        // 알림 채널 생성 (Android 8.0 이상)
        createNotificationChannelIfNeeded(notificationManager, channelId);

        // 알림 내용 설정
        String contentText = generateContentText(type, day);

        // 알림 빌더
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("알람 알림")
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        // 알림 표시
        if (notificationManager != null) {
            int notificationId = generateNotificationId(goalId);
            notificationManager.notify(notificationId, builder.build());
        }
    }

    /**
     * Goal ID가 유효한지 확인.
     */
    private boolean isValidGoalId(int goalId) {
        if (goalId == -1) {
            // 유효하지 않은 goalId 처리
            return false;
        }
        return true;
    }

    /**
     * 알림 채널 생성 (Android 8.0 이상).
     */
    private void createNotificationChannelIfNeeded(NotificationManager notificationManager, String channelId) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            if (notificationManager != null) {
                NotificationChannel channel = new NotificationChannel(
                        channelId,
                        "알람 채널",
                        NotificationManager.IMPORTANCE_HIGH
                );
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    /**
     * 알림 내용 생성.
     */
    private String generateContentText(String type, String day) {
        if ("daily".equals(type)) {
            return "오늘 설정된 알림이 울립니다!";
        } else if (day != null) {
            return day + "에 설정된 알림이 울립니다!";
        } else {
            return "설정된 알림이 울립니다!";
        }
    }

    /**
     * 고유 알림 ID 생성.
     */
    private int generateNotificationId(int goalId) {
        return goalId * 1000 + (int) (System.currentTimeMillis() % 1000);
    }
}
