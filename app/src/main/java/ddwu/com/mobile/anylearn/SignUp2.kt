package ddwu.com.mobile.anylearn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ddwu.com.mobile.anylearn.R
import ddwu.com.mobile.anylearn.databinding.ActivitySignUp1Binding
import ddwu.com.mobile.anylearn.databinding.ActivitySignUp2Binding

class SignUp2 : AppCompatActivity() {

    lateinit var suBinding2: ActivitySignUp2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        suBinding2 = ActivitySignUp2Binding.inflate(layoutInflater)
        setContentView(suBinding2.root)

        suBinding2.btnStart.setOnClickListener {
            val nickname = suBinding2.editNick.text.toString()

            if (nickname.equals("닉네임") || nickname.isEmpty())
                Toast.makeText(applicationContext, "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show()
            else {
                val intent = Intent(this, SignIn::class.java)
                startActivity(intent)
            }
        }
    }
}