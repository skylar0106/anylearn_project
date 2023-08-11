package ddwu.com.mobile.anylearn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.kakao.sdk.common.KakaoSdk
import ddwu.com.mobile.anylearn.R

class SignIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val loginButton : Button = findViewById(R.id.btn_start)
        val kakaoButton : Button = findViewById(R.id.kakao_login)

        loginButton.setOnClickListener {
            val intent = Intent(this, FirstSetting::class.java)
            startActivity(intent)
        }

        /** KakoSDK init */
        KakaoSdk.init(this, "e421b29a0c6f375748cc83767fee0df7")

        kakaoButton.setOnClickListener{

        }


    }
}