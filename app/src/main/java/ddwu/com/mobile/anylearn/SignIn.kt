package ddwu.com.mobile.anylearn

import CsrfTokenInterceptor
import FetchCsrfTokenTask
import SignInApiService
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
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
    private lateinit var kakaoLogin: KakaoLogin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        siBinding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(siBinding.root)

        //로그인
        siBinding.btnStart.setOnClickListener {
            val email = siBinding.textEmail.text.toString()
            val password = siBinding.textName.text.toString()

//            val intent = Intent(this@SignIn, FirstSetting::class.java)
//            startActivity(intent)

            FetchCsrfTokenTask(object : FetchCsrfTokenTask.CsrfTokenListener {
                override fun onCsrfTokenFetched(csrfToken: String) {
                    checkConnection(csrfToken, email, password)
                }
            }).execute("http://34.81.3.83:8000/")

        }

        siBinding.btnJoin.setOnClickListener{
            val intent = Intent(this, SignUp1::class.java)
            startActivity(intent)
        }

        kakaoLogin = KakaoLogin(application)

        siBinding.kakaoLogin.setOnClickListener{
            // 카카오계정으로 로그인 공통 callback 구성
// 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Log.e(KakaoLogin.TAG, "카카오계정으로 로그인 실패", error)
                } else if (token != null) {
                    Log.i(KakaoLogin.TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
                }
            }

// 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        Log.e(KakaoLogin.TAG, "카카오톡으로 로그인 실패", error)

                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                    } else if (token != null) {
                        Log.i(KakaoLogin.TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }

    }
    private fun checkConnection(requestBody1: String, requestBody2: String, csrfToken: String) {
        //val csrfToken = "TeRgsQ77RizByRhRzs3u2H7BCUsbcmrl"

        val httpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(CsrfTokenInterceptor(csrfToken))
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://34.81.3.83:8000/") // 본인의 Django 서버 URL을 적는다.
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()

        val apiService = retrofit.create(SignInApiService::class.java)
        val call: Call<Void> = apiService.checkConnection(mapOf(
            "email" to requestBody1,
            "password" to requestBody2
        ))

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("Connection", "HTTP connection successful")
                    val intent = Intent(this@SignIn, FirstSetting::class.java)
                    startActivity(intent)
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