package ddwu.com.mobile.anylearn

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import ddwu.com.mobile.anylearn.R
import ddwu.com.mobile.anylearn.databinding.ActivitySignUp1Binding
import java.util.*

class SignUp1 : AppCompatActivity() {

    lateinit var suBinding1: ActivitySignUp1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        suBinding1 = ActivitySignUp1Binding.inflate(layoutInflater)
        setContentView(suBinding1.root)

        val email = suBinding1.editEmail.text.toString()
        val name = suBinding1.editName.text.toString()
        val birth = suBinding1.editBirth.text.toString()


        suBinding1.btnBirth.setOnClickListener {
            val cal = Calendar.getInstance()
            val data = DatePickerDialog.OnDateSetListener { view, year, month, day ->
                val selectedDate = "${year}/${month}/${day}"
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
            else {
                val intent = Intent(this, SignUp2::class.java)
                startActivity(intent)
            }
        }
    }
}