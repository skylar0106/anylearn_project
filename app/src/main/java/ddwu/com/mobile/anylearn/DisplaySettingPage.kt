package ddwu.com.mobile.anylearn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CompoundButton
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.widget.SwitchCompat
import ddwu.com.mobile.anylearn.R

class DisplaySettingPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_setting_page)

        val undoButton: Button = findViewById(R.id.DisplaySettingPageUndo)
        undoButton.setOnClickListener {
            finish()
        }

        val textSizeButton: RadioGroup = findViewById(R.id.radioGroup)
        textSizeButton.setOnCheckedChangeListener { group, checkedId ->
            // 라디오 버튼의 체크 상태 변경 시에 실행될 코드를 작성합니다.
            val radioButtonS: RadioButton = findViewById(R.id.radioButton_s)
            val radioButtonM: RadioButton = findViewById(R.id.radioButton_m)
            val radioButtonL: RadioButton = findViewById(R.id.radioButton_l)

            when (checkedId) {
                R.id.radioButton_s -> {
                    // "작게" 라디오 버튼이 선택된 경우
                    radioButtonS.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.shape_radio_button_true, 0, 0)
                    radioButtonM.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.shape_radio_button, 0, 0)
                    radioButtonL.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.shape_radio_button, 0, 0)
                }
                R.id.radioButton_m -> {
                    // "중간" 라디오 버튼이 선택된 경우
                    radioButtonS.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.shape_radio_button, 0, 0)
                    radioButtonM.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.shape_radio_button_true, 0, 0)
                    radioButtonL.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.shape_radio_button, 0, 0)
                }
                R.id.radioButton_l -> {
                    // "크게" 라디오 버튼이 선택된 경우
                    radioButtonS.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.shape_radio_button, 0, 0)
                    radioButtonM.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.shape_radio_button, 0, 0)
                    radioButtonL.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.shape_radio_button_true, 0, 0)
                }
            }
        }
    }
}