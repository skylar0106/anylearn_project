package ddwu.com.mobile.anylearn

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ddwu.com.mobile.anylearn.databinding.ActivityScriptSaveChoiceBinding
import org.json.JSONObject

class ScriptSaveChoice : AppCompatActivity() {

    lateinit var sscBinding : ActivityScriptSaveChoiceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sscBinding = ActivityScriptSaveChoiceBinding.inflate(layoutInflater)
        setContentView(sscBinding.root)

        // WithaiLevel 클래스의 WebSocket 객체 얻기
        val webSocket = WithaiLevel.getWebSocketInstance()

        sscBinding.saveBtn.setOnClickListener{
            val intent = Intent(this, ScriptNameInput::class.java)
            startActivity(intent)
        }

        sscBinding.noSaveBtn.setOnClickListener{
            val noSavejJson = JSONObject()
            noSavejJson.put("type", "notSave")
            noSavejJson.put("message", "notSave")

            webSocket.send(noSavejJson.toString())

            val intent = Intent(this, MainPage::class.java)
            startActivity(intent)
        }

        val settingButton: Button = findViewById(R.id.ScriptSaveChoiceSetting)
        settingButton.setOnClickListener {
            val intent = Intent(this, SettingPage::class.java)
            startActivity(intent)
        }
    }
}