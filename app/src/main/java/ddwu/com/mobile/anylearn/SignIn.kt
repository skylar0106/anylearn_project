package ddwu.com.mobile.anylearn

import CsrfTokenInterceptor
import SignInApiService
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.annotations.SerializedName
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
import java.util.concurrent.TimeUnit


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

            val intent = Intent(this@SignIn, FirstSetting::class.java)
            startActivity(intent)

            checkConnection(email, password)
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

    //토큰 모델
    data class YourTokenResponseModel(
        @SerializedName("token") val token: String // 서버 응답에서 토큰 필드에 맞게 조정
    )

    data class YourTokenRequestModel(
        @SerializedName("email") val email: String,
        @SerializedName("password") val password: String)

    private fun checkConnection(requestBody1: String, requestBody2: String) {

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

        val apiService = retrofit.create(SignInApiService::class.java)
        // YourTokenRequestModel 객체 생성 및 전달
        val requestModel = YourTokenRequestModel(email = requestBody1, password = requestBody2)
        val call: Call<YourTokenResponseModel> = apiService.getToken(requestModel)

        call.enqueue(object : Callback<YourTokenResponseModel> {
            override fun onResponse(call: Call<YourTokenResponseModel>, response: Response<YourTokenResponseModel>) {
                if (response.isSuccessful) {
                    val tokenResponse = response.body()
                    val token = tokenResponse?.token
                    if (token != null) {
                        // 여기에서 토큰을 사용할 수 있습니다.
                        Log.d("Token", "Received token: $tokenResponse")

                        // 다음 단계로 진행하거나 필요한 작업을 수행하세요.
                        val intent = Intent(this@SignIn, FirstSetting::class.java)
                        startActivity(intent)
                    } else {
                        Log.e("Token", "Token response body is null")
                    }
                } else {
                    Log.e(
                        "Token",
                        "HTTP token request failed. Error code: ${response.code()}"
                    )
                }
            }

            override fun onFailure(call: Call<YourTokenResponseModel>, t: Throwable) {
                Log.e("Token", "HTTP token request error: ${t.message}")
            }
        })
    }

}