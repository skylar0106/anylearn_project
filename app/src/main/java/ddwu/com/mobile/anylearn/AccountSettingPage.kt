package ddwu.com.mobile.anylearn

import LogoutApiService
import RemoveApiService
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import ddwu.com.mobile.anylearn.R
import ddwu.com.mobile.anylearn.databinding.ActivityAccountSettingPageBinding
import ddwu.com.mobile.anylearn.databinding.ActivityMyDiaryScriptBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountSettingPage : AppCompatActivity() {
    lateinit var asBinding : ActivityAccountSettingPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        asBinding = ActivityAccountSettingPageBinding.inflate(layoutInflater)
        setContentView(asBinding.root)

        val nickname = intent.getStringExtra("nickname")
        val username = intent.getStringExtra("username")
        val email = intent.getStringExtra("email")
        val phonenumber = intent.getStringExtra("phonenumber")
        val profile = intent.getStringExtra("profile")

        asBinding.AccountSettingPageNickname.setText(nickname)
        asBinding.AccountSettingPageName.setText(username)
        asBinding.AccountSettingPageEmail.setText(email)
        asBinding.AccountSettingPagePH.setText(phonenumber)

        if(profile != "") {
            Log.e("Profile", "Uri is not Null")
            Glide.with(this)
                .load(profile)
                .into(asBinding.imageView)
        }

        val undoButton: Button = findViewById(R.id.AccountSettingPageUndo)
        undoButton.setOnClickListener {
            finish()
        }

        val logout : Button = findViewById(R.id.accountSettingPageLogout)
        logout.setOnClickListener{
            logout()
        }
        asBinding.accountSettingPageRmove.setOnClickListener {
            remove()
        }
    }
    fun logout() {
        val mySharedPreferences = MySharedPreferences(this)

        val apiService = RetrofitConfig(this).retrofit.create(LogoutApiService::class.java)
        val csrfToken = mySharedPreferences.getCsrfToken()
        val cookieToken = mySharedPreferences.getCookieToken()
        val sessionId = mySharedPreferences.getSessionId()
        val call: Call<Void> =
            apiService.postSubject("$csrfToken", "csrftoken=$cookieToken; sessionid=$sessionId")

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("Logout", "logout 성공")
                    val intent = Intent(this@AccountSettingPage, SignIn::class.java)
                    startActivity(intent)
                } else {
                    Log.e("Logout", "no response")
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("Logout", "logout Failure")
            }
        })
    }
    fun remove() {
        val mySharedPreferences = MySharedPreferences(this)

        val apiService = RetrofitConfig(this).retrofit.create(RemoveApiService::class.java)
        val csrfToken = mySharedPreferences.getCsrfToken()
        val cookieToken = mySharedPreferences.getCookieToken()
        val sessionId = mySharedPreferences.getSessionId()
        val call: Call<Void> =
            apiService.postSubject("$csrfToken", "csrftoken=$cookieToken; sessionid=$sessionId")

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("Logout", "logout 성공")
                    val intent = Intent(this@AccountSettingPage, SignIn::class.java)
                    startActivity(intent)
                } else {
                    Log.e("Logout", "no response")
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("Logout", "logout Failure")
            }
        })
    }
}