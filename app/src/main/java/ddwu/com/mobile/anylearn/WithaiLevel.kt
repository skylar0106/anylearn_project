package ddwu.com.mobile.anylearn

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ddwu.com.mobile.anylearn.databinding.ActivityWithaiLevelBinding

class WithaiLevel : AppCompatActivity() {

    lateinit var wlBinding : ActivityWithaiLevelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wlBinding = ActivityWithaiLevelBinding.inflate(layoutInflater)
        setContentView(wlBinding.root)

        wlBinding.levelFinishBtn.setOnClickListener{
            val intent = Intent(this, ScriptSaveChoice::class.java)
            startActivity(intent)
        }

        val settingButton: Button = findViewById(R.id.WithaiLevelSetting)
        settingButton.setOnClickListener {
            val intent = Intent(this, SettingPage::class.java)
            startActivity(intent)
        }
    }
}