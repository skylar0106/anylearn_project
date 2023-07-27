package ddwu.com.mobile.anylearn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import ddwu.com.mobile.anylearn.R
import ddwu.com.mobile.anylearn.databinding.ActivityWithaiSelectBinding

class WithaiSelect : AppCompatActivity() {

    lateinit var wsBinding: ActivityWithaiSelectBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        wsBinding = ActivityWithaiSelectBinding.inflate(layoutInflater)
        setContentView(wsBinding.root)

        wsBinding.school.setOnClickListener {
            val intent = Intent(this, WithaiLevel::class.java)
            startActivity(intent)
        }

        wsBinding.subjectOkBtn.setOnClickListener {
            val intent = Intent(this, WithaiLevel::class.java)
            startActivity(intent)
        }
    }
}