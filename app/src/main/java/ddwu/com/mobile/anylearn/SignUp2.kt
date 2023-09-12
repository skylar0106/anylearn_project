package ddwu.com.mobile.anylearn

import Signup2ApiService
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.gson.annotations.SerializedName
import ddwu.com.mobile.anylearn.R
import ddwu.com.mobile.anylearn.databinding.ActivitySignUp1Binding
import ddwu.com.mobile.anylearn.databinding.ActivitySignUp2Binding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUp2 : AppCompatActivity() {

    lateinit var suBinding2: ActivitySignUp2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        suBinding2 = ActivitySignUp2Binding.inflate(layoutInflater)
        setContentView(suBinding2.root)

        suBinding2.btnStart.setOnClickListener {
            val nickname = suBinding2.editNick.text.toString()
            val image = R.mipmap.ic_launcher

            if (nickname.equals("닉네임") || nickname.isEmpty())
                Toast.makeText(applicationContext, "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show()
            else {
                signUp2Info(nickname)
            }
        }
    }
    data class SignUp2ResponseModel(
        @SerializedName("message") val message: String
    )
    data class Signup2RequestModel(
        @SerializedName("nickname") val birth: String
//        @SerializedName("avatar") val phone: String
    )

    private fun signUp2Info(nickname: String) {
        val apiService = RetrofitConfig(this).retrofit.create(Signup2ApiService::class.java)

        // SignupRequestModel 객체 생성 및 전달
        val requestModel = Signup2RequestModel(nickname)
        val call: Call<SignUp2ResponseModel> = apiService.signup2(requestModel)

        call.enqueue(object : Callback<SignUp2ResponseModel> {
            val mySharedPreferences = MySharedPreferences(this@SignUp2)

            override fun onResponse(call: Call<SignUp2ResponseModel>, response: Response<SignUp2ResponseModel>) {
                if (response.isSuccessful) {
                    val responseData = response.body()

                    val csrfToken =
                        response.headers()["X-CSRFToken"].toString() // 헤더에서 CSRF 토큰 가져오기
                    val sessionId = response.headers()["Session-ID"].toString() // 헤더에서 세션 아이디 가져오기
                    mySharedPreferences.saveCsrfToken(csrfToken)
                    mySharedPreferences.saveSessionId(sessionId)


                    if (csrfToken != null) {
                        // 여기에서 토큰을 사용할 수 있습니다.
                        Log.d(
                            "Token", "Received token: $csrfToken" +
                                    "session_id: $sessionId"
                        )
                        val intent = Intent(this@SignUp2, SignIn::class.java)
                        startActivity(intent)

                        Log.d(
                            "CsrfToken", "Received token: $csrfToken"
                        )

                        // 다음 단계로 진행하거나 필요한 작업을 수행하세요.
                    } else {
                        Log.e("CsrfToken", "Token response body is null")
                    }
                }
            }


            override fun onFailure(call: Call<SignUp2ResponseModel>, t: Throwable) {
                Log.e("Signup", "HTTP signup request error: ${t.message}")
            }
        })
    }
}