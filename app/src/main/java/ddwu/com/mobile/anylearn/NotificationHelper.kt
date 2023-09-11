package ddwu.com.mobile.anylearn

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import java.util.*

class NotificationHelper : AppCompatActivity() {

    private val CHANNEL_ID = "anylearn_channel"
    private val notificationId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        showNotification()
        setAlarm()
    }

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "anylearn"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, channelName, importance)

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification() {
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.button2_style) // 알림 아이콘 설정
            .setContentTitle("알람테스트")
            .setContentText("알림 내용")
            .setAutoCancel(true) // 사용자가 알림을 터치하면 알림이 자동으로 삭제되도록 설정
            .setPriority(NotificationCompat.PRIORITY_HIGH) // 알림 중요도 설정

        val resultIntent = Intent(this, Anylearn::class.java)
        val resultPendingIntent = PendingIntent.getActivity(
            this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        notificationBuilder.setContentIntent(resultPendingIntent)

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    fun setAlarm(){
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 1) // 1일 후

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }
}