package ddwu.com.mobile.anylearn

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ddwu.com.mobile.anylearn.databinding.ActivityMyDiaryMainBinding

class MyDiaryMain : AppCompatActivity() {

    lateinit var mdmBinding: ActivityMyDiaryMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mdmBinding = ActivityMyDiaryMainBinding.inflate(layoutInflater)
        setContentView(mdmBinding.root)
    }
}



