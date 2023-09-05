package ddwu.com.mobile.anylearn

import ScriptsGetService
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
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
        checkConnection2()
    }

    private fun checkConnection2() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://34.81.3.83:8000/") // 본인의 디장고 서버 URL을 적는다.
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                    .build()
            )
            .build()

        val scriptGetService = retrofit.create(ScriptsGetService::class.java)
        val call: Call<Void> = scriptGetService.scriptsConnection()

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("Connection", "HTTP connection successful")
                } else {
                    Log.e(
                        "Connection",
                        "HTTP connection failed. Error code: ${response.code()}"
                    )
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("Connection", "HTTP connection error: ${t.message}")
            }
        })
    }
}
