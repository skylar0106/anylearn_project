package ddwu.com.mobile.anylearn

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ddwu.com.mobile.anylearn.databinding.ActivityScriptNameInputBinding

class ScriptNameInput : AppCompatActivity() {

    lateinit var sniBinding : ActivityScriptNameInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sniBinding = ActivityScriptNameInputBinding.inflate(layoutInflater)
        setContentView(sniBinding.root)

        sniBinding.nameInputListBtn.setOnClickListener{
            val intent = Intent(this, MyScriptList::class.java)
            startActivity(intent)
        }

        sniBinding.nameInputHomeBtn.setOnClickListener{
            val intent = Intent(this, MainPage::class.java)
            startActivity(intent)
        }

        val settingButton: Button = findViewById(R.id.ScriptNameInputSetting)
        settingButton.setOnClickListener {
            val intent = Intent(this, SettingPage::class.java)
            startActivity(intent)
        }
    }
}