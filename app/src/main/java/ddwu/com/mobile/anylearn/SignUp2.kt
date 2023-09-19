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
                //signUp2Info(nickname, )
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
            var currentImageURL = intent?.data
            // Base64 인코딩부분
            val ins: InputStream? = currentImageURL?.let {
                applicationContext.contentResolver.openInputStream(
                    it
                )
            }
            val img: Bitmap = BitmapFactory.decodeStream(ins)
            ins?.close()
            val resized = Bitmap.createScaledBitmap(img, 256, 256, true)
            val byteArrayOutputStream = ByteArrayOutputStream()
            resized.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream)
            val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
            val outStream = ByteArrayOutputStream()
            val res: Resources = resources
            var profileImageBase64 = Base64.encodeToString(byteArray, NO_WRAP)
            // 여기까지 인코딩 끝

            // 이미지 뷰에 선택한 이미지 출력
            val imageview: ImageView = suBinding2.imageView2
            imageview.setImageURI(currentImageURL)
            try {
                //이미지 선택 후 처리
            }catch (e: Exception){
                e.printStackTrace()
            }
        } else{
            Log.d("ActivityResult", "something wrong")
        }
    }

    data class SignUp2ResponseModel(
        @SerializedName("message") val message: String
    )
    data class Signup2RequestModel(
        @SerializedName("nickname") val birth: String,
        @SerializedName("avatar") val avatar: String
    )

    private fun signUp2Info(nickname: String, imageUri: Uri) {
        val apiService = RetrofitConfig(this).retrofit.create(Signup2ApiService::class.java)
        val mySharedPreferences = MySharedPreferences(this@SignUp2)
        val csrfToken = mySharedPreferences.getCsrfToken()
        val sessionId = mySharedPreferences.getSessionId()
        val cookieToken = mySharedPreferences.getCookieToken()

        // SignupRequestModel 객체 생성 및 전달
        val requestModel = Signup2RequestModel(nickname, imageUri.toString())
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