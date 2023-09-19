package ddwu.com.mobile.anylearn

import Signup2ApiService
import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Base64.NO_WRAP
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.google.gson.annotations.SerializedName
import ddwu.com.mobile.anylearn.databinding.ActivitySignUp2Binding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.InputStream

class SignUp2 : AppCompatActivity() {

    lateinit var suBinding2: ActivitySignUp2Binding
    private var selectedImageUri: Uri? = null // 이미지 Uri를 저장할 변수 추가
    companion object {
        private const val REQUEST_PERMISSION = 1001
        private const val REQUEST_GALLERY = 1002
    }

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
                signUp2Info(nickname, selectedImageUri)
            }
        }

        suBinding2.imageView2.setOnClickListener {
            openGallery()
        }
    }

    private fun openGallery(){
        val intent = Intent(Intent.ACTION_PICK)

        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        intent.type = "image/*"
        startActivityForResult(intent, 102)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (requestCode == 102 && resultCode == Activity.RESULT_OK){
            selectedImageUri = intent?.data // 이미지 Uri를 저장
            // 이미지 뷰에 선택한 이미지 출력
            val imageview: ImageView = suBinding2.imageView2
            imageview.setImageURI(selectedImageUri)
        } else{
            Log.d("ActivityResult", "something wrong")
        }
    }

    data class SignUp2ResponseModel(
        @SerializedName("message") val message: String
    )
    data class Signup2RequestModel(
        @SerializedName("nickname") val birth: String,
        @SerializedName("avatar") val avatar: Uri?
    )

    private fun signUp2Info(nickname: String, imageUri: Uri?) {
        val apiService = RetrofitConfig(this).retrofit.create(Signup2ApiService::class.java)
        val mySharedPreferences = MySharedPreferences(this@SignUp2)
        val csrfToken = mySharedPreferences.getCsrfToken()
        val sessionId = mySharedPreferences.getSessionId()
        val cookieToken = mySharedPreferences.getCookieToken()

        // SignupRequestModel 객체 생성 및 전달
        val requestModel = Signup2RequestModel(nickname, imageUri)
        val call: Call<SignUp2ResponseModel> = apiService.signup2(
            "csrftoken=$cookieToken; sessionid=$sessionId",
            "$csrfToken",
            requestModel
        )

        call.enqueue(object : Callback<SignUp2ResponseModel> {

            override fun onResponse(
                call: Call<SignUp2ResponseModel>,
                response: Response<SignUp2ResponseModel>
            ) {
                if (response.isSuccessful) {
                    val responseData = response.body()

                    if (csrfToken != null) {
                        // 여기에서 토큰을 사용할 수 있습니다.
                        Log.d(
                            "Token", "Received token: $csrfToken" +
                                    "session_id: $sessionId"
                        )
                        val intent = Intent(this@SignUp2, SignIn::class.java)
                        startActivity(intent)

                    // 다음 단계로 진행하거나 필요한 작업을 수행하세요.
                    } else {
                        Log.e("CsrfToken", "Token response body is null")
                    }
                }
            }

            override fun onFailure(call: Call<SignUp2ResponseModel>, t: Throwable) {
                Log.e("Signup", "HTTP signup2 request error: ${t.message}")
            }
        })
    }
}