package ddwu.com.mobile.anylearn

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmReceiver : BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        // 알림을 생성하고 표시하는 로직을 여기에 추가
        val notificationId = 1 // 다른 알림 ID 사용
        val channelId = "anylearn_channel" // 이전에 정의한 채널 ID와 동일해야 함

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.button2_style) // 알림 아이콘 설정
            .setContentTitle("1일 후 알림") // 알림 제목
            .setContentText("알림 내용") // 알림 내용
            .setAutoCancel(true) // 사용자가 알림을 터치하면 알림이 자동으로 삭제되도록 설정
            .setPriority(NotificationCompat.PRIORITY_HIGH) // 알림 중요도 설정

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}