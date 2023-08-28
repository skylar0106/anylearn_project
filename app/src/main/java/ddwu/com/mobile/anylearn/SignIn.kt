package ddwu.com.mobile.anylearn

import CsrfTokenInterceptor
import ScriptsApiService
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import ddwu.com.mobile.anylearn.databinding.ActivitySignInBinding
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignIn : AppCompatActivity() {

    lateinit var siBinding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        siBinding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(siBinding.root)

        val loginButton : Button = findViewById(R.id.btn_start)
        val kakaoButton : Button = findViewById(R.id.kakao_login)


            siBinding.btnStart.setOnClickListener {
            val intent = Intent(this, FirstSetting::class.java)
            startActivity(intent)

            val email = siBinding.textEmail.text.toString()
            val password = siBinding.textName.text.toString()

            checkConnection(email, password)
        }





        kakaoButton.setOnClickListener{

        }


    }
    private fun checkConnection(requestBody1: String, requestBody2: String) {
        val csrfToken = "TeRgsQ77RizByRhRzs3u2H7BCUsbcmrl"

        val httpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(CsrfTokenInterceptor(csrfToken))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://35.229.205.158:8000/") // 본인의 디장고 서버 URL을 적는다.
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                    .build()
            )
            .build()

        val apiService = retrofit.create(ScriptsApiService::class.java)
        val call: Call<Void> = apiService.checkConnection(mapOf(
            "email" to requestBody1,
            "password" to requestBody2
        ))

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