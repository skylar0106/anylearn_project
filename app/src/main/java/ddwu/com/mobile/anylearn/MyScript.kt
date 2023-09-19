package ddwu.com.mobile.anylearn

import ScriptGetService
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.gson.annotations.SerializedName
import ddwu.com.mobile.anylearn.databinding.ActivityMyScriptBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class MyScript : AppCompatActivity() {

    lateinit var msBinding : ActivityMyScriptBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        msBinding = ActivityMyScriptBinding.inflate(layoutInflater)
        setContentView(msBinding.root)

        val intent = intent // 현재 액티비티의 Intent 객체를 가져옴
        val scriptTitle = intent.getStringExtra("script_title") // "script_title" 키로 전달한 데이터를 받아옴
//        val scriptDate = intent.getStringExtra("script_learningDate") // "script_date" 키로 전달한 데이터를 받아옴
//        val scriptContents = intent.getStringExtra("script_contents")
//        val scriptAddDiary = intent.getIntExtra("script_addDiary", 0)
//        val scriptHashTag = intent.getStringArrayExtra("script_hashtag")
        Log.d("script내용 myscript", "title: $scriptTitle")




        if(scriptTitle != null) {
            myscriptGet(scriptTitle)
        }

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
    data class ScriptResponseModel(
        @SerializedName("title") val title: String,
        @SerializedName("hashtag") val hashtag: List<Map<String, String>>,
        @SerializedName("contents") val contents: String,
        @SerializedName("learningDate") val learningDate: String,
        @SerializedName("add_diary") val addDiary: Int,
        @SerializedName("show_expr") val showExpr: Int,
        @SerializedName("input_expr") val inputExpr: String
    )

    private fun myscriptGet(title: String){
        val mySharedPreferences = MySharedPreferences(this)
        val apiService = RetrofitConfig(this).retrofit.create(ScriptGetService::class.java)
        val csrfToken = mySharedPreferences.getCsrfToken()
        val cookieToken = mySharedPreferences.getCookieToken()
        val sessionId = mySharedPreferences.getSessionId()
        val roomId = "$title"
        Log.d("url확인", "url: $roomId")

        val call: Call<ScriptResponseModel> = apiService.scriptGet( "$csrfToken","csrftoken=$cookieToken; sessionid=$sessionId", roomId)


        call.enqueue(object : Callback<ScriptResponseModel> {
            override fun onResponse(call: Call<ScriptResponseModel>, response: Response<ScriptResponseModel>) {
                val responseBody = response.body()
                val title = responseBody?.title
                val hashtag = responseBody?.hashtag
                val contents = responseBody?.contents
                val learningDate = responseBody?.learningDate
                val addDiary = responseBody?.addDiary
                val showExpr = responseBody?.showExpr
                val inputExpr = responseBody?.inputExpr
                if (response.isSuccessful) {
                    Log.e("MyScript", "성공!")
                    // 서버로부터 성공적인 응답을 받았을 때 수행할 작업
                    if (hashtag != null) {
                        val hashTagString = StringBuilder()
                        for (map in hashtag) {
                            for ((key, value) in map) {
                                hashTagString.append("#")
                                hashTagString.append(value).append(" ")
                            }
                        }
                        msBinding.scriptTag.text = hashTagString.toString()
                    }


                    msBinding.scriptSubject.text = title
                    msBinding.scriptDate.text = learningDate
                    msBinding.scriptContent.text = contents
                } else {
                    Log.e("MyScript", "HTTP signup request failed. Error code: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<ScriptResponseModel>, t: Throwable) {
                Log.e("Signup", "HTTP withaiselect request error: ${t.message}")

            }
        })
    }
}


