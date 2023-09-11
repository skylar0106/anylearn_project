package ddwu.com.mobile.anylearn

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ddwu.com.mobile.anylearn.R

class MainPage : AppCompatActivity() {


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        val menuRecyclerView: RecyclerView = findViewById(R.id.MainPageRecyclerView)

        // 메뉴 항목 데이터 리스트 생성
        val menuItems = mutableListOf<MenuItem>().apply {
            add(MenuItem("AI와 대화하기", "AI와 함께 원하는 주제로 대화해보세요.", "단계 설정 ①", "시작하기"))
            add(MenuItem("My Script", "지금까지의 대화를 한번에 확인하세요.", " ", "확인하기"))
            add(MenuItem("My Diary", "학습한 표현을 다이어리에 기록해 보세요."," ","목록"))
        }

        // RecyclerView에 어댑터 연결
        val layoutManager = LinearLayoutManager(this)
        menuRecyclerView.setLayoutManager(layoutManager)

        val menuAdapter = MainPageAdapter(menuItems, this)
        menuRecyclerView.adapter = menuAdapter

        val settingButton: Button = findViewById(R.id.MainPageSetting)
        settingButton.setOnClickListener {
            val intent = Intent(this, SettingPage::class.java)
            startActivity(intent)
        }
    }
}