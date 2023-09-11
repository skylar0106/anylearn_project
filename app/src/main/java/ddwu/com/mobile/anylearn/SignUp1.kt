package ddwu.com.mobile.anylearn

import SignupApiService
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.google.gson.annotations.SerializedName
import ddwu.com.mobile.anylearn.R
import ddwu.com.mobile.anylearn.databinding.ActivitySignUp1Binding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SignUp1 : AppCompatActivity() {

    lateinit var suBinding1: ActivitySignUp1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        suBinding1 = ActivitySignUp1Binding.inflate(layoutInflater)
        setContentView(suBinding1.root)

        suBinding1.btnBirth.setOnClickListener {
            val cal = Calendar.getInstance()
            val data = DatePickerDialog.OnDateSetListener { view, year, month, day ->
                val selectedDate = "${year}-${month+1}-${day}"
                suBinding1.editBirth.setText(selectedDate)
            }
            DatePickerDialog(this, data, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        val phone = suBinding1.editPhone.text.toString()
        val pwd = suBinding1.editPwd2.text.toString()
        val pwdCheck = suBinding1.editPwdCheck.text.toString()

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
            else {
                signUpInfo(birth, phone, name, email, pwd)
                val intent = Intent(this, SignUp2::class.java)
                startActivity(intent)
            }

        }
    }

    data class SignupResponseModel(
        @SerializedName("id") val id: String
    )
    data class SignupRequestModel(
        @SerializedName("username") val birth: String,
        @SerializedName("phonenumber") val phone: String,
        @SerializedName("nickname") val name: String,
        @SerializedName("email") val email: String,
        @SerializedName("password") val pwd: String
    )

    private fun signUpInfo(birth: String, phone: String, name: String, email: String, pwd: String) {
        val apiService = RetrofitConfig.retrofit.create(SignupApiService::class.java)

        // SignupRequestModel 객체 생성 및 전달
        val requestModel = SignupRequestModel(birth, phone, name, email, pwd)
        val call: Call<SignupResponseModel> = apiService.signup(requestModel)

        call.enqueue(object : Callback<SignupResponseModel> {
            override fun onResponse(call: Call<SignupResponseModel>, response: Response<SignupResponseModel>) {
                if (response.isSuccessful) {
                    val responseData = response.body()
                    // 서버로부터 받은 응답 데이터 처리
                    if (responseData != null) {
                        // 처리할 작업 수행
                    } else {
                        Log.e("Signup", "Response body is null")
                    }
                } else {
                    Log.e(
                        "Signup",
                        "HTTP signup request failed. Error code: ${response.code()}"
                    )
                }
            }

            override fun onFailure(call: Call<SignupResponseModel>, t: Throwable) {
                Log.e("Signup", "HTTP signup request error: ${t.message}")
            }
        })
    }

}