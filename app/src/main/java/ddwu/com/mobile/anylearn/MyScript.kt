package ddwu.com.mobile.anylearn

import ScriptGetService
import ScriptsRCApiService
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.BackgroundColorSpan
import android.text.style.ClickableSpan
import android.util.Log
import android.view.*
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.gson.annotations.SerializedName
import ddwu.com.mobile.anylearn.databinding.ActivityMyScriptBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class MyScript : AppCompatActivity() {
    lateinit var msBinding : ActivityMyScriptBinding
    lateinit var selectedSentence : String
    lateinit var dialog: Dialog
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

        msBinding.scriptChangedBtn.setOnClickListener{
            if(selectedSentence != null)
                if(scriptTitle != null)
                    getParaphrase(selectedSentence, scriptTitle)
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
    // 문자열을 문장으로 분리하는 함수
    fun splitTextIntoSentences(text: String): List<String> {
        // 정규 표현식을 사용하여 문장을 분리 (마침표, 물음표, 느낌표, 물결 기호)
        val sentences = text.split("[.?!~]".toRegex()).map { it.trim() }
        return sentences
    }

    data class PrResponseModel(
        @SerializedName("paraphrase") val paraphrase: String
    )
    data class PrRequestModel(
        @SerializedName("show_expr") val show_expr: Int,
        @SerializedName("input_expr") val input_expr: String
    )

    private fun getParaphrase(sentence: String, title: String){
        val mySharedPreferences = MySharedPreferences(this)
        val apiService = RetrofitConfig(this).retrofit.create(ScriptsRCApiService::class.java)
        val csrfToken = mySharedPreferences.getCsrfToken()
        val cookieToken = mySharedPreferences.getCookieToken()
        val sessionId = mySharedPreferences.getSessionId()
        val roomId = "$title"
        Log.d("url확인", "url: $roomId")

        val requestModel =
            PrRequestModel(show_expr = 1, input_expr = sentence)

        val call: Call<PrResponseModel> = apiService.scriptGet( "$csrfToken","csrftoken=$cookieToken; sessionid=$sessionId", roomId, requestModel)

        call.enqueue(object : Callback<PrResponseModel> {
            override fun onResponse(
                call: Call<PrResponseModel>,
                response: Response<PrResponseModel>
            ) {
                val responseBody = response.body()

                if (response.isSuccessful) {
                    if (responseBody != null) {
                        val paraphrase = responseBody.paraphrase
                        Log.e("Paraphrase", "성공!")
                        showPopupDialog(paraphrase)
                    }
                }else
                    Log.e(
                        "Paraphrase",
                        "HTTP request failed. Error code: ${response.code()}"
                    )
            }
            override fun onFailure(call: Call<PrResponseModel>, t: Throwable) {
                Log.e("Signup", "HTTP withaiselect request error: ${t.message}")

            }
        })
    }

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
                // 클릭 가능한 문장을 담을 리스트
                val clickableSentences = mutableListOf<Pair<String, ClickableSpan>>()

                if (response.isSuccessful) {
                    if (responseBody != null) {
                        val title = responseBody.title
                        val hashtag = responseBody.hashtag
                        val contents = responseBody.contents
                        val learningDate = responseBody.learningDate
                        val addDiary = responseBody.addDiary
                        val showExpr = responseBody.showExpr
                        val inputExpr = responseBody.inputExpr

                        Log.e("MyScript", "성공!")

                        // 문장별로 분리
                        val sentences = splitTextIntoSentences(contents ?: contents)
                        // sentences 리스트에 분리된 문장이 들어 있음

                        // 각 문장에 대해 ClickableSpan을 생성하고 리스트에 추가
                        for (sentence in sentences) {
                            val clickableSpan = object : ClickableSpan() {
                                override fun onClick(widget: View) {
                                    // 특정 문장을 클릭했을 때 수행할 작업을 여기에 추가
                                    selectedSentence = sentence
                                }

                                override fun updateDrawState(ds: TextPaint) {
                                    super.updateDrawState(ds)
                                    ds.color = Color.BLACK // 클릭 가능한 텍스트의 색상 변경 (예: 파란색)
                                    ds.isUnderlineText = false // 클릭 가능한 텍스트에 밑줄 추가
                                }
                            }
                            val startIndex = contents.indexOf(sentence)
                            val endIndex = startIndex + sentence.length
                            val spannableSentence = SpannableString(sentence)
                            spannableSentence.setSpan(clickableSpan, 0, sentence.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                            clickableSentences.add(Pair(spannableSentence.toString(), clickableSpan))
                        }

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

                        // SpannableString을 생성하여 텍스트뷰에 설정
                        val text = msBinding.scriptContent.text.toString()
                        val spannableString = SpannableString(text)
                        for ((sentence, clickableSpan) in clickableSentences) {
                            val startIndex = text.indexOf(sentence)
                            val endIndex = startIndex + sentence.length
                            spannableString.setSpan(
                                clickableSpan,
                                startIndex,
                                endIndex,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                        }

                        // 텍스트뷰에 SpannableString 설정
                        val textView = msBinding.scriptContent
                        textView.text = spannableString

                        // 텍스트뷰에서 롱클릭 모드 설정
                        textView.isLongClickable = true

                        msBinding.scriptContent.text = spannableString
                        msBinding.scriptContent.movementMethod = LinkMovementMethod.getInstance()

                        } else {
                        Log.e(
                            "MyScript",
                            "HTTP signup request failed. Error code: ${response.code()}"
                        )
                    }
                }
            }
            override fun onFailure(call: Call<ScriptResponseModel>, t: Throwable) {
                Log.e("Signup", "HTTP withaiselect request error: ${t.message}")

            }
        })
    }
    private fun showPopupDialog(selectedSentence: String) {
        // 다이얼로그 생성
        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.paraphrase_popup) // 팝업 레이아웃 리소스 파일을 설정합니다.

        // 팝업 내용 설정
        val popupText = dialog.findViewById<TextView>(R.id.popupText)
        popupText.text = selectedSentence

        // 팝업 내 버튼 설정
        val closeButton = dialog.findViewById<Button>(R.id.closeButton)
        closeButton.setOnClickListener {
            dialog.dismiss() // 다이얼로그 닫기
        }

        // 팝업 크기 및 위치 설정 (예: 가운데 정렬)
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window!!.attributes)
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.gravity = android.view.Gravity.CENTER

        // 다이얼로그에 레이아웃 파라미터 설정
        dialog.window!!.attributes = layoutParams

        // 다이얼로그 표시
        dialog.show()
    }
}


