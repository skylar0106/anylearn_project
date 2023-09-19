package ddwu.com.mobile.anylearn

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ddwu.com.mobile.anylearn.databinding.ActivityScriptNameInputBinding
import org.json.JSONArray
import org.json.JSONObject

class ScriptNameInput : AppCompatActivity() {

    lateinit var sniBinding : ActivityScriptNameInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sniBinding = ActivityScriptNameInputBinding.inflate(layoutInflater)
        setContentView(sniBinding.root)

        // WithaiLevel 클래스의 WebSocket 객체 얻기
        val webSocket = WithaiLevel.getWebSocketInstance()



        sniBinding.nameInputListBtn.setOnClickListener{
            var title = sniBinding.editTitleInput.text.toString()
            var hashtagString = sniBinding.editSubjectInput.text.toString()
            var hashtags = hashtagString.split(" ").toTypedArray()
            Log.d("saveScripts", "title: $title hashtagString: $hashtagString")

            val saveJson = JSONObject()
            saveJson.put("type", "save")
            saveJson.put("message", "save")
            saveJson.put("title", "$title")
            val hashtagArray = JSONArray()
            for (hashtag in hashtags) {
                hashtagArray.put(hashtag)
            }
            saveJson.put("hashtags", hashtagArray)

            webSocket.send(saveJson.toString())


            val intent = Intent(this, MyScriptList::class.java)
            startActivity(intent)
        }

        sniBinding.nameInputHomeBtn.setOnClickListener{
            var title = sniBinding.editTitleInput.text.toString()
            var hashtagString = sniBinding.editSubjectInput.text.toString()
            var hashtags = hashtagString.split(" ")

            val saveJson = JSONObject()
            saveJson.put("type", "save")
            saveJson.put("message", "save")
            saveJson.put("title", "$title")
            saveJson.put("hashtags", "$hashtags")

            webSocket.send(saveJson.toString())
            Log.d("saveScripts", "title: $title hashtags: $hashtags")

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