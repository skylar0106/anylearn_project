package ddwu.com.mobile.anylearn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CompoundButton
import androidx.appcompat.widget.SwitchCompat
import ddwu.com.mobile.anylearn.R

class ModeSettingPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mode_setting_page)

        val HomeSwitch: SwitchCompat = findViewById(R.id.ModeSettingPageHomeSwitch)
        val PublicSwitch: SwitchCompat = findViewById(R.id.ModeSettingPagePublicSwitch)
        val mySharedPreferences = MySharedPreferences(this)

        // 스위치 상태 변경 시 색상 변경
        HomeSwitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                // switchButton이 체크된 경우
                HomeSwitch.thumbDrawable = resources.getDrawable(R.drawable.shape_switch_thumb)
                PublicSwitch.thumbDrawable = resources.getDrawable(R.drawable.shape_switch_thumb_off)
                PublicSwitch.isChecked = false
                mySharedPreferences.saveTTSBoolean(false)
            } else {
                // switchButton이 체크되지 않은 경우
                HomeSwitch.thumbDrawable = resources.getDrawable(R.drawable.shape_switch_thumb_off)
                PublicSwitch.thumbDrawable = resources.getDrawable(R.drawable.shape_switch_thumb)
                PublicSwitch.isChecked = true
                mySharedPreferences.saveTTSBoolean(true)
            }
        })
        PublicSwitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                // switchButton이 체크된 경우
                PublicSwitch.thumbDrawable = resources.getDrawable(R.drawable.shape_switch_thumb)
                HomeSwitch.thumbDrawable = resources.getDrawable(R.drawable.shape_switch_thumb_off)
                HomeSwitch.isChecked = false
                mySharedPreferences.saveTTSBoolean(true)
            } else {
                // switchButton이 체크되지 않은 경우
                PublicSwitch.thumbDrawable = resources.getDrawable(R.drawable.shape_switch_thumb_off)
                HomeSwitch.thumbDrawable = resources.getDrawable(R.drawable.shape_switch_thumb)
                HomeSwitch.isChecked = true
                mySharedPreferences.saveTTSBoolean(false)
            }
        })

        val undoButton: Button = findViewById(R.id.ModeSettingPageUndo)
        undoButton.setOnClickListener {
            finish()
        }
    }
}