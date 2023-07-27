package ddwu.com.mobile.anylearn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import ddwu.com.mobile.anylearn.databinding.ActivityMyScriptBinding

class MyScript : AppCompatActivity() {

    lateinit var msBinding : ActivityMyScriptBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        msBinding = ActivityMyScriptBinding.inflate(layoutInflater)
        setContentView(msBinding.root)

        msBinding.scriptHomeBtn.setOnClickListener{
            val intent = Intent(this, MainPage::class.java)
            startActivity(intent)
        }

        msBinding.scriptListBtn.setOnClickListener{
            val intent = Intent(this, MyScriptList::class.java)
            startActivity(intent)
        }

        val settingButton: Button = findViewById(R.id.MyScriptSetting)
        settingButton.setOnClickListener {
            val intent = Intent(this, SettingPage::class.java)
            startActivity(intent)
        }
    }
}