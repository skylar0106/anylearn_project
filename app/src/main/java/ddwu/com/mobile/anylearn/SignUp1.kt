package ddwu.com.mobile.anylearn

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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




    }
}