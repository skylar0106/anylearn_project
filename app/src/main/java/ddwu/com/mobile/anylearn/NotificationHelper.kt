package ddwu.com.mobile.anylearn

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.*

class NotificationHelper(private val context: Context) {

    private val CHANNEL_ID = "anylearn_channel"
    private val notificationId = 1

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
    private val intent = Intent(context, AlarmReceiver::class.java)
    private val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    private val mainIntent = Intent(context, Anylearn::class.java)
    private val mainPendingIntent = PendingIntent.getActivity(context, 0, mainIntent, PendingIntent.FLAG_IMMUTABLE)
    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "anylearn"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, channelName, importance)

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    @SuppressLint("MissingPermission")
    fun showNotification() {
        createNotificationChannel()

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_action_name) // 알림 아이콘 설정
            .setContentTitle("Anylearn")
            .setContentText("학습한지 너무 오래됐어요 Anylearn을 다시 찾아주세요~!")
            .setContentIntent(mainPendingIntent)
            .setAutoCancel(true) // 사용자가 알림을 터치하면 알림이 자동으로 삭제되도록 설정
            .setPriority(NotificationCompat.PRIORITY_HIGH) // 알림 중요도 설정


        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(notificationId, notificationBuilder.build())
    }


    fun setAlarm() {

        Log.d("setAlarm", "setAlarmOK")

        alarmManager?.setRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_DAY*30,
            AlarmManager.INTERVAL_DAY*30, // 60초마다
            pendingIntent
//            AlarmManager.ELAPSED_REALTIME_WAKEUP,
//            SystemClock.elapsedRealtime() + 10*1000,
//            60*1000, // 60초마다
//            pendingIntent
        )
    }

    fun cancelAlarm() {
        alarmManager?.cancel(pendingIntent)
    }
}
