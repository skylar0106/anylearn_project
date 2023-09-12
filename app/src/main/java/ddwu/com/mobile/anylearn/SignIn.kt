package ddwu.com.mobile.anylearn

import HeaderInterceptor
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignIn : AppCompatActivity() {

    lateinit var siBinding: ActivitySignInBinding
    private lateinit var kakaoLogin: KakaoLogin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        siBinding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(siBinding.root)

        //로그인
        siBinding.btnStart.setOnClickListener {
            val email = siBinding.editEmail.text.toString()
            val password = siBinding.editName.text.toString()


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
        @SerializedName("ok") val ok: String
    )

    data class YourTokenRequestModel(
        @SerializedName("email") val email: String,
        @SerializedName("password") val password: String)

    private fun checkConnection(requestBody1: String, requestBody2: String) {
        val mySharedPreferences = MySharedPreferences(this)


        val apiService = RetrofitConfig(this).retrofit.create(SignInApiService::class.java)

        // YourTokenRequestModel 객체 생성 및 전달
        val requestModel = YourTokenRequestModel(email = requestBody1, password = requestBody2)
        val call: Call<YourTokenResponseModel> = apiService.getToken(requestModel)

        call.enqueue(object : Callback<YourTokenResponseModel> {
            override fun onResponse(call: Call<YourTokenResponseModel>, response: Response<YourTokenResponseModel>) {
                if (response.isSuccessful) {
                    val Response = response.body()

                    val ok = Response?.ok

                    HeaderInterceptor(mySharedPreferences)

                    val csrfToken = response.headers()["X-Csrftoken"].toString() // 헤더에서 CSRF 토큰 가져오기
                    val sessionId = response.headers()["Session-id"].toString() // 헤더에서 세션 아이디 가져오기



                    mySharedPreferences.saveCsrfToken(csrfToken)
                    mySharedPreferences.saveSessionId(sessionId)

                    if (csrfToken != null) {
                        // 여기에서 토큰을 사용할 수 있습니다.
                        Log.d("Token", "Received token: $csrfToken" +
                                "session_id: $sessionId"+"ok: $ok")


                        Log.d("CsrfToken", "Received token: $csrfToken" +
                                "ok: $ok")

                        // 다음 단계로 진행하거나 필요한 작업을 수행하세요.
                        val intent = Intent(this@SignIn, MainPage::class.java)
                        startActivity(intent)
                    } else {
                        Log.e("CsrfToken", "Token response body is null")

                    }
                } else {
                    Log.e(
                        "CsrfToken",
                        "HTTP token request failed. Error code: ${response.code()}"
                    )

                }
            }

            override fun onFailure(call: Call<YourTokenResponseModel>, t: Throwable) {
                Log.e("CsrfToken", "HTTP token request error: ${t.message}")

            }
        })
    }

}