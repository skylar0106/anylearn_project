package ddwu.com.mobile.anylearn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ddwu.com.mobile.anylearn.R

class FirstSetting : AppCompatActivity() {

    private val delayMillis = 5000L // 딜레이 시간 설정 (5초)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_setting)

        android.os.Handler().postDelayed({
            val intent = Intent(this, MainPage::class.java)
            startActivity(intent)
            finish() // 현재 액티비티 종료
        }, delayMillis)
    }
}