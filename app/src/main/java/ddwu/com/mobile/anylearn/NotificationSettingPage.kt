package ddwu.com.mobile.anylearn

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat

class NotificationSettingPage : AppCompatActivity() {

    private var alarmEnabled = false // 알람 활성화 여부를 나타내는 변수

    @SuppressLint("UseSwitchCompatOrMaterialCode", "UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_setting_page)
        lateinit var notificationHelper: NotificationHelper
        notificationHelper = NotificationHelper(this)

        val alarmReceiver = AlarmReceiver()

        val mySwitch: SwitchCompat = findViewById(R.id.NotificationSettingPageNotiSwitch)

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val switchState = sharedPreferences.getBoolean("switchState", false) // 기본값은 false

        mySwitch.isChecked = switchState

        if (mySwitch.isChecked) {
            // switchButton이 체크된 경우
            mySwitch.thumbDrawable = resources.getDrawable(R.drawable.shape_switch_thumb)
        } else {
            // switchButton이 체크되지 않은 경우
            mySwitch.thumbDrawable = resources.getDrawable(R.drawable.shape_switch_thumb_off)
        }
        // 스위치 상태 변경 시 색상 변경
        mySwitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                // switchButton이 체크된 경우
                mySwitch.thumbDrawable = resources.getDrawable(R.drawable.shape_switch_thumb)
                alarmEnabled = true // 알람 활성화
                Log.d("Alram", "isChecked")
                Toast.makeText(this, "알림권한을 확인해주세요.", Toast.LENGTH_SHORT).show()
                Thread {
                    Thread.sleep(3000)
                    notificationHelper.setAlarm()
                }.start()

                val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean("switchState", mySwitch.isChecked)
                editor.apply()
            } else {
                // switchButton이 체크되지 않은 경우
                mySwitch.thumbDrawable = resources.getDrawable(R.drawable.shape_switch_thumb_off)
                alarmEnabled = false // 알람 비활성화
                Thread {
                    Thread.sleep(3000)
                    notificationHelper.cancelAlarm()
                }.start()
                val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean("switchState", mySwitch.isChecked)
                editor.apply()
            }
        })

        val undoButton: Button = findViewById(R.id.NotificationSettingPageUndo)
        undoButton.setOnClickListener {
            finish()
        }
    }

}

