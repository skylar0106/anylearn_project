package ddwu.com.mobile.anylearn

import ScriptsGetService
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.annotations.SerializedName
import ddwu.com.mobile.anylearn.databinding.ActivityMyScriptListBinding
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyScriptList : AppCompatActivity() {

    lateinit var mslBinding : ActivityMyScriptListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mslBinding = ActivityMyScriptListBinding.inflate(layoutInflater)
        setContentView(mslBinding.root)

        mslBinding.scriptListHomeBtn.setOnClickListener {
            val intent = Intent(this, MainPage::class.java)
            startActivity(intent)
        }

        val settingButton: Button = findViewById(R.id.MyScriptListSetting)
        settingButton.setOnClickListener {
            val intent = Intent(this, SettingPage::class.java)
            startActivity(intent)
        }

        // checkConnection 함수 호출
        scriptListGet()
    }
    data class ScriptListResponseModel(
        @SerializedName("ok") val ok: String
    )

    private fun scriptListGet() {
        val mySharedPreferences = MySharedPreferences(this)

        val apiService = RetrofitConfig(this).retrofit.create(ScriptsGetService::class.java)

        val cookieToken = mySharedPreferences.getCookieToken()
        val sessionId = mySharedPreferences.getSessionId()
        val call: Call<ScriptListResponseModel> = apiService.scripListGet("csrftoken=$cookieToken; sessionid=$sessionId")

        call.enqueue(object : Callback<ScriptListResponseModel> {
            override fun onResponse(call: Call<ScriptListResponseModel>, response: Response<ScriptListResponseModel>) {
                if (response.isSuccessful) {
                    // 성공적인 응답을 받았을 때 수행할 작업
                    val responseBody = response.body()

                    if (responseBody != null) {
                        // responseBody를 사용하여 필요한 데이터를 추출하고 변수에 저장할 수 있습니다.
                        // 예를 들어, responseBody에서 데이터를 추출하여 저장하는 방법은 아래와 같을 수 있습니다.
                        // val data = responseBody.data

                        // 변수에 데이터를 저장한 후에 원하는 작업을 수행합니다.
                    }
                } else {
                    Log.e("ScriptListGet", "HTTP signup request failed. Error code: ${response.code()}")
                    Log.e("ScriptListGet", " cookieToken: $cookieToken" + " sessionId: $sessionId")
                }
            }

            override fun onFailure(call: Call<ScriptListResponseModel>, t: Throwable) {
                Log.e("Signup", "HTTP withaiselect request error: ${t.message}")
            }
        })
    }

}
