package ddwu.com.mobile.anylearn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CompoundButton
import androidx.appcompat.widget.SwitchCompat
import ddwu.com.mobile.anylearn.R

class SoundSettingPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sound_setting_page)

        val mySwitch: SwitchCompat = findViewById(R.id.SoundSettingPageSwitch)
        // 스위치 상태 변경 시 색상 변경
        mySwitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                // switchButton이 체크된 경우
                mySwitch.thumbDrawable = resources.getDrawable(R.drawable.shape_switch_thumb)
            } else {
                // switchButton이 체크되지 않은 경우
                mySwitch.thumbDrawable = resources.getDrawable(R.drawable.shape_switch_thumb_off)
            }
        })

        val undoButton: Button = findViewById(R.id.SoundSettingPageUndo)
        undoButton.setOnClickListener {
            finish()
        }
    }
}