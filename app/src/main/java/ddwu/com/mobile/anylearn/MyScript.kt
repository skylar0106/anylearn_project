package ddwu.com.mobile.anylearn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import ddwu.com.mobile.anylearn.databinding.ActivityMyScriptBinding

class MyScript : AppCompatActivity() {

    lateinit var msBinding : ActivityMyScriptBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        msBinding = ActivityMyScriptBinding.inflate(layoutInflater)
        setContentView(msBinding.root)

        val intent = intent // 현재 액티비티의 Intent 객체를 가져옴
        val scriptTitle = intent.getStringExtra("script_title") // "script_title" 키로 전달한 데이터를 받아옴
        val scriptDate = intent.getStringExtra("script_learningDate") // "script_date" 키로 전달한 데이터를 받아옴
        val scriptContents = intent.getStringExtra("script_contents")
        val scriptAddDiary = intent.getIntExtra("script_addDiary", 0)
        val scriptHashTag = intent.getStringArrayExtra("script_hashtag")
        Log.d("script내용 myscript", "title: ${scriptTitle}, learningDate: ${scriptDate}")

        val hashTagString = scriptHashTag?.joinToString(" ")

        msBinding.scriptSubject.text = scriptTitle
        msBinding.scriptDate.text = scriptDate
        msBinding.scriptContent.text = scriptContents
        msBinding.scriptTag.text = hashTagString


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