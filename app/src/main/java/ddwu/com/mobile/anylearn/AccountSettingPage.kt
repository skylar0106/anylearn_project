package ddwu.com.mobile.anylearn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import ddwu.com.mobile.anylearn.R

class AccountSettingPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_setting_page)

        val undoButton: Button = findViewById(R.id.AccountSettingPageUndo)
        undoButton.setOnClickListener {
            finish()
        }
    }
}