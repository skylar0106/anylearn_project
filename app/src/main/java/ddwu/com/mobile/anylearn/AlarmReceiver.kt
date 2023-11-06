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


        val notificationHelper = NotificationHelper(context)
        notificationHelper.showNotification()
    }
}
