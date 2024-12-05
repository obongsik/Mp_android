package com.example.habittracker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // 전달받은 goal_id, day, type 추출
        int goalId = intent.getIntExtra("goal_id", -1);
        String day = intent.getStringExtra("day");
        String type = intent.getStringExtra("type");

        // 유효성 검사
        if (goalId == -1) {
            Log.e("AlarmReceiver", "Invalid Goal ID");
            return;
        }

        // 알림 생성 준비
        String channelId = "alarm_channel";
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // 알림 채널 생성 (Android 8.0 이상)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId, "알람 채널", NotificationManager.IMPORTANCE_HIGH
            );
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        // 알림 내용 설정
        String contentText;
        if ("daily".equals(type)) {
            contentText = "오늘 설정된 알림이 울립니다!";
        } else if (day != null) {
            contentText = day + "에 설정된 알림이 울립니다!";
        } else {
            contentText = "설정된 알림이 울립니다!";
        }

        // 알림 빌더
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info) // 기본 아이콘
                .setContentTitle("알람 알림")
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        // 알림 표시
        if (notificationManager != null) {
            int notificationId = goalId * 1000 + (int) (System.currentTimeMillis() % 1000); // 고유 알림 ID 생성
            notificationManager.notify(notificationId, builder.build());
        }
    }
}
