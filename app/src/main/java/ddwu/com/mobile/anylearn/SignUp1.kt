package ddwu.com.mobile.anylearn

import EmailCheckApiService
import HeaderInterceptor
import SignupApiService
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import ddwu.com.mobile.anylearn.databinding.ActivitySignUp1Binding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class SignUp1 : AppCompatActivity() {

    lateinit var suBinding1: ActivitySignUp1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        suBinding1 = ActivitySignUp1Binding.inflate(layoutInflater)
        setContentView(suBinding1.root)

        suBinding1.btnBirth.setOnClickListener {
            val cal = Calendar.getInstance()
            val data = DatePickerDialog.OnDateSetListener { view, year, month, day ->
                val selectedMonth = if (month + 1 < 10) "0${month + 1}" else (month + 1).toString()
                val selectedDay = if (day < 10) "0$day" else day.toString()
                val selectedDate = "$year$selectedMonth$selectedDay"
                suBinding1.editBirth.setText(selectedDate)
            }
            DatePickerDialog(this, data, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        var emailFlag = 0
        suBinding1.btnEmail.setOnClickListener {
            emailFlag = emailCheck(suBinding1.editEmail.text.toString())
            Log.e("EmailCheck", "flag: ${emailFlag}")
        }
        suBinding1.btnStart.setOnClickListener{
            val email = suBinding1.editEmail.text.toString()
            val name = suBinding1.editName.text.toString()
            val birth = suBinding1.editBirth.text.toString()
            val phone = suBinding1.editPhone.text.toString()
            val pwd = suBinding1.editPwd2.text.toString()
            val pwdCheck = suBinding1.editPwdCheck.text.toString()

            if(email.isEmpty())
                Toast.makeText(applicationContext, "e-mail을 입력해주세요", LENGTH_SHORT).show()
            else if(name.isEmpty())
                Toast.makeText(applicationContext, "이름을 입력해주세요", LENGTH_SHORT).show()
            else if(birth.isEmpty())
                Toast.makeText(applicationContext, "생년월일을 입력해주세요", LENGTH_SHORT).show()
            else if(phone.isEmpty())
                Toast.makeText(applicationContext, "전화번호를 입력해주세요", LENGTH_SHORT).show()
            else if(pwd.isEmpty())
                Toast.makeText(applicationContext, "비밀번호를 입력해주세요", LENGTH_SHORT).show()
            else if(pwdCheck.isEmpty())
                Toast.makeText(applicationContext, "비밀번호를 한번 더 입력해주세요", LENGTH_SHORT).show()
            else if(pwd != pwdCheck)
                Toast.makeText(applicationContext, "입력한 비밀번호가 동일하지 않습니다", LENGTH_SHORT).show()
//            else if(emailFlag != 1)
//                Toast.makeText(applicationContext, "이메일 중복 확인을 완료해주세요", LENGTH_SHORT).show()
            else {
                signUpInfo(name, phone, email, pwd, birth)

            }

        }
    }
    data class EmailChekRequestModel(
        @SerializedName("email") val email: String,
    )
    data class EmailCheckResponseModel(
        @SerializedName("impossible") val impossible: String,
        @SerializedName("possible") val possible: String
    )
    data class SignupResponseModel(
        @SerializedName("username") val username: ArrayList<String>,
        @SerializedName("email") val email: ArrayList<String>
    )
    data class SignupRequestModel(
        @SerializedName("username") val birth: String,
        @SerializedName("phonenumber") val phone: String,
        @SerializedName("email") val email: String,
        @SerializedName("password") val pwd: String,
        @SerializedName("birth") val name: String
    )

    private fun emailCheck(email: String): Int {
//        val mySharedPreferences = MySharedPreferences(this)
        var flag = 0
        val apiService = RetrofitConfig(this).retrofit.create(EmailCheckApiService::class.java)
//        val csrfToken = mySharedPreferences.getCsrfToken()
//        val cookieToken = mySharedPreferences.getCookieToken()
//        val sessionId = mySharedPreferences.getSessionId()
        val path = EmailChekRequestModel(email)
        val call: Call<EmailCheckResponseModel> = apiService.emailCheck()

        call.enqueue(object : Callback<EmailCheckResponseModel> {
            val mySharedPreferences = MySharedPreferences(this@SignUp1)

            override fun onResponse(call: Call<EmailCheckResponseModel>, response: Response<EmailCheckResponseModel>) {
                if (response.isSuccessful) {
                    val responseData = response.body()

                    HeaderInterceptor(mySharedPreferences)

                    val csrfToken = response.headers()["X-Csrftoken"].toString() // 헤더에서 CSRF 토큰 가져오기
                    val sessionId = response.headers()["Session-ID"].toString() // 헤더에서 세션 아이디 가져오기
                    mySharedPreferences.saveCsrfToken(csrfToken)
                    mySharedPreferences.saveSessionId(sessionId)



                    if (csrfToken != null) {
                        // 여기에서 토큰을 사용할 수 있습니다.
                        Log.d(
                            "Token", "Received token: $csrfToken" +
                                    "session_id: $sessionId"
                        )
                        if (responseData?.possible == "이미 존재하는 email입니다."){
                           Toast.makeText(this@SignUp1, "이미 존재하는 email입니다.", Toast.LENGTH_SHORT).show()
                        }
                        else if (responseData?.impossible == "사용 가능한 email입니다."){
                            Toast.makeText(this@SignUp1, "사용 가능한 email입니다.", Toast.LENGTH_SHORT).show()
                            flag = 1
                        }
                        // 다음 단계로 진행하거나 필요한 작업을 수행하세요.
                    } else {

                        Log.e("EmailCheck", "Token response body is null")
                    }
                }
            }

            override fun onFailure(call: Call<EmailCheckResponseModel>, t: Throwable) {
                Log.e("EmailCheck", "error: ${t.message}")
            }
        })
        return flag
    }
    private fun signUpInfo(name: String, phone: String, email: String, pwd: String, birth: String) {
        val apiService = RetrofitConfig(this).retrofit.create(SignupApiService::class.java)

        // SignupRequestModel 객체 생성 및 전달
        val requestModel = SignupRequestModel(name, phone, email, pwd, birth)
        val call: Call<Void> = apiService.signup(requestModel)

        call.enqueue(object : Callback<Void> {
            val mySharedPreferences = MySharedPreferences(this@SignUp1)

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.code() == 400) {
                    // 클라이언트 오류 응답을 처리
                    val errorResponse = response.errorBody()?.string()
                    if (errorResponse != null) {
                        Log.e("emailCheck", "${errorResponse}")
                        try {
                            val gson = Gson()
                            val errorMap = gson.fromJson(errorResponse, Map::class.java) as Map<String, Any> // 또는 사용자 정의 클래스로 변환할 수 있음

                            val usernameErrors = errorMap["username"]
                            val emailErrors = errorMap["email"]

                            if (usernameErrors != null && emailErrors != null) {
                                val usernameErrorMessage = (usernameErrors as List<String>).get(0)
                                val emailErrorMessage = (emailErrors as List<String>).get(0)
                                // 변수에 담긴 오류 메시지 사용
                                Toast.makeText(this@SignUp1, "이미 존재하는 email입니다.", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(this@SignUp1, "Unknown error", Toast.LENGTH_LONG).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(this@SignUp1, "Error parsing server response", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this@SignUp1, "Unknown error", Toast.LENGTH_LONG).show()
                    }
                } else if (response.isSuccessful) {
                    val responseData = response.body()


                    HeaderInterceptor(mySharedPreferences)

                    val csrfToken = response.headers()["X-Csrftoken"].toString() // 헤더에서 CSRF 토큰 가져오기
                    val sessionId = response.headers()["Session-ID"].toString() // 헤더에서 세션 아이디 가져오기
                    mySharedPreferences.saveCsrfToken(csrfToken)
                    mySharedPreferences.saveSessionId(sessionId)


                    if (csrfToken != null) {
                        // 여기에서 토큰을 사용할 수 있습니다.
                        Log.d(
                            "Token", "Received token: $csrfToken" +
                                    "session_id: $sessionId"
                        )
                        val intent = Intent(this@SignUp1, SignUp2::class.java)
                        startActivity(intent)
                    } else {
                        Log.e("CsrfToken", "Token response body is null")
                    }
                }

            }


            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("Signup", "HTTP signup request error: ${t.message}")
            }
        })
    }

}