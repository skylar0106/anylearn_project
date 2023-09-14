package ddwu.com.mobile.anylearn

import ScriptsGetService
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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
    companion object{
        lateinit var scriptList: ArrayList<ListType>
        lateinit var scriptTitle: String
        lateinit var date: String
    }

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

        //recyclerview
        val list = ArrayList<ListType>()
        val adapter = ScriptsListAdapter(list)

        val layoutManager =  LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        mslBinding.scriptListRecyclerview.layoutManager = layoutManager
        mslBinding.scriptListRecyclerview.adapter = adapter

    }
    data class ListType(
        val title: String,
        val date: String
    )
    data class ScriptListResponseModel(
        @SerializedName("title") val title: String,
        @SerializedName("hashtag") val hashtag: List<String>,
        @SerializedName("contents") val contents: String,
        @SerializedName("level") val level: String,
        @SerializedName("learningDate") val learningDate: String,
        @SerializedName("add_diary") val addDiary: Int,
        @SerializedName("show_expr") val showExpr: Int,
        @SerializedName("input_expr") val inputExpr: String,
        @SerializedName("email") val email: String,
    )

    private fun scriptListGet() {
        val mySharedPreferences = MySharedPreferences(this)

        val apiService = RetrofitConfig(this).retrofit.create(ScriptsGetService::class.java)

        val cookieToken = mySharedPreferences.getCookieToken()
        val sessionId = mySharedPreferences.getSessionId()
        val call: Call<List<ScriptListResponseModel>> = apiService.scripListGet("csrftoken=$cookieToken; sessionid=$sessionId")
        scriptList = ArrayList<ListType>()

        call.enqueue(object : Callback<List<ScriptListResponseModel>> {


            override fun onResponse(call: Call<List<ScriptListResponseModel>>, response: Response<List<ScriptListResponseModel>>) {
                if (response.isSuccessful) {
                    // 성공적인 응답을 받았을 때 수행할 작업
                    val responseBody = response.body()

                    if (responseBody != null) {
                        for (responseData in responseBody.orEmpty()) {
                            scriptTitle = responseData.title.toString()
                            date = responseData.learningDate.toString()
                            scriptList.add(ListType(scriptTitle, date))

                            // 필요한 작업 수행
                        }
                    }
                } else {
                    Log.e("ScriptListGet", "HTTP signup request failed. Error code: ${response.code()}")
                    Log.e("ScriptListGet", " cookieToken: $cookieToken" + " sessionId: $sessionId")
                }
            }

            override fun onFailure(call: Call<List<ScriptListResponseModel>>, t: Throwable) {
                Log.e("Signup", "HTTP withaiselect request error: ${t.message}")
            }
        })
    }

}
