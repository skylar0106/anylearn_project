package ddwu.com.mobile.anylearn

import HeaderInterceptor
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.gson.annotations.SerializedName
import ddwu.com.mobile.anylearn.databinding.ActivityWithaiSelectBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WithaiSelect : AppCompatActivity() {

    lateinit var wsBinding: ActivityWithaiSelectBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        wsBinding = ActivityWithaiSelectBinding.inflate(layoutInflater)
        setContentView(wsBinding.root)

//        wsBinding.park.setOnClickListener {
//            val notificationHelper = NotificationHelper()
//            notificationHelper.createNotificationChannel()
//            notificationHelper.showNotification()
//        }

        wsBinding.subjectOkBtn.setOnClickListener {
            val level = intent.getIntExtra("level", 0)
            val situation = wsBinding.editSubject.text.toString()
            val myRole = wsBinding.userRoleEdit.text.toString()
            val gptRole = wsBinding.aiRoleEdit.text.toString()

            if (level != null) {
                Log.d("WithaiSelect", "level: $level, situation: $situation, my_role: $myRole, gpt_role: $gptRole")
                if(situation == "" || myRole == "" || gptRole == "")
                    Toast.makeText(this, "역할과 주제를 모두 입력해주세요", Toast.LENGTH_SHORT).show()
                else
                    aiSelectInfo(level, situation, myRole, gptRole)
            }
            else{
                Log.e("WithaiSelect", "levelNull!")
            }
        }






    }
    data class SelectResponseModel(
        @SerializedName("id") val id: Int
//        @SerializedName("level") val level: Int,
//        @SerializedName("situation") val situation: String,
//        @SerializedName("situation_en") val situationEn: String,
//        @SerializedName("my_role") val my_role: String,
//        @SerializedName("my_role_en") val my_roleEn: String,
//        @SerializedName("gpt_role") val gpt_role: String,
//        @SerializedName("gpt_role_en") val gpt_roleEn: String
    )
    data class SelectRequestModel(
        @SerializedName("level") val level: Int,
        @SerializedName("situation") val situation: String,
        @SerializedName("my_role") val my_role: String,
        @SerializedName("gpt_role") val gpt_role: String
    )
    private fun aiSelectInfo(level: Int, situation: String, myRole:String, gptRole: String){
        val mySharedPreferences = MySharedPreferences(this)


        val apiService = RetrofitConfig(this).retrofit.create(AiSelectApiService::class.java)
        val requestModel = SelectRequestModel(level= level, situation = situation, my_role = myRole, gpt_role = gptRole)
        val csrfToken = mySharedPreferences.getCsrfToken()
        val cookieToken = mySharedPreferences.getCookieToken()
        val sessionId = mySharedPreferences.getSessionId()
        val call: Call<SelectResponseModel> = apiService.postSubject( "$csrfToken","csrftoken=$cookieToken; sessionid=$sessionId", requestModel)

        call.enqueue(object : Callback<SelectResponseModel> {
            override fun onResponse(call: Call<SelectResponseModel>, response: Response<SelectResponseModel>) {
                val responseBody = response.body()
                val id = responseBody?.id

                if (response.isSuccessful) {
                    Log.e("WithaiSelect", "성공!")
                    Log.e("WithaiSelectResponse", "id: $id")
                    val intent = Intent(this@WithaiSelect, WithaiLevel::class.java)
                    intent.putExtra("roomId", id)
                    intent.putExtra("level", level)
                    startActivity(intent)
                    // 서버로부터 성공적인 응답을 받았을 때 수행할 작업
                } else {

                    Log.e("WithaiSelect", "HTTP signup request failed. Error code: ${response.code()}")
                    Log.e("WithaiSelectResponse", "id: $id"+" token: $csrfToken"+" cookieToken: $cookieToken"+" sessionId: $sessionId")

                }
            }
            override fun onFailure(call: Call<SelectResponseModel>, t: Throwable) {
                Log.e("Signup", "HTTP withaiselect request error: ${t.message}")

            }
        })
    }
}