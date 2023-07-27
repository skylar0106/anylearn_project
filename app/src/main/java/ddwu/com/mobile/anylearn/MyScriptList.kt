package ddwu.com.mobile.anylearn

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ddwu.com.mobile.anylearn.databinding.ActivityMyScriptListBinding

class MyScriptList : AppCompatActivity() {

    lateinit var mslBinding : ActivityMyScriptListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mslBinding = ActivityMyScriptListBinding.inflate(layoutInflater)
        setContentView(mslBinding.root)

        mslBinding.scriptListHomeBtn.setOnClickListener{
            val intent = Intent(this, MainPage::class.java)
            startActivity(intent)
        }

        val settingButton: Button = findViewById(R.id.MyScriptListSetting)
        settingButton.setOnClickListener {
            val intent = Intent(this, SettingPage::class.java)
            startActivity(intent)
        }
    }
}