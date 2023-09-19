package ddwu.com.mobile.anylearn

import WelcomeApiService
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.annotations.SerializedName
import ddwu.com.mobile.anylearn.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class MainPage : AppCompatActivity() {
    var lt_year by Delegates.notNull<Int>()
    var lt_month by Delegates.notNull<Int>()
    var lt_day by Delegates.notNull<Int>()
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
        getAccount()
    }

    data class AccountResponseModel(
        @SerializedName("last_login") val last_login: String
    )

    fun getAccount(){
        val mySharedPreferences = MySharedPreferences(this)

        val apiService = RetrofitConfig(this).retrofit.create(WelcomeApiService::class.java)
        val csrfToken = mySharedPreferences.getCsrfToken()
        val cookieToken = mySharedPreferences.getCookieToken()
        val sessionId = mySharedPreferences.getSessionId()
        val call: Call<MainPage.AccountResponseModel> =
            apiService.postSubject( "$csrfToken","csrftoken=$cookieToken; sessionid=$sessionId")

        call.enqueue(object : Callback<MainPage.AccountResponseModel> {
            override fun onResponse(
                call: Call<MainPage.AccountResponseModel>,
                response: Response<MainPage.AccountResponseModel>
            ) {
                if(response.isSuccessful){
                    val responseBody = response.body()

                    val last_login = responseBody?.last_login
                    getLastLoginDate(last_login)

                    val calendar : Calendar = Calendar.getInstance()

                    val cr_year = calendar.get(Calendar.YEAR)
                    val cr_month = calendar.get(Calendar.MONTH)
                    val cr_day = calendar.get(Calendar.DAY_OF_MONTH)
                    val welcomeText = findViewById<TextView>(R.id.welcomeText)

                    if(cr_year - lt_year >= 1)
                        welcomeText.setText("${cr_year-lt_year}만에 방문하셨네요! \n앞으로 더 열심히 해야겠어요!")
                    else if(cr_year - lt_year == 0 && cr_month - lt_month >= 10)
                        welcomeText.setText("오랜만에 방문하셨네요! \n오늘은 많은 script를 만들어봐요!")
                    else if(cr_year - lt_year == 0 && cr_month - lt_month < 10 && cr_month - lt_month >= 5)
                        welcomeText.setText("다시 만나서 정말 반가워요! \n오늘은 diary를 채워볼까요?")
                    else if(cr_year - lt_year == 0 && cr_month - lt_month < 5 && cr_month - lt_month >= 1)
                        welcomeText.setText("기다리고 있었어요! \n함께 열심히 공부해요!")
                    else if(cr_month - lt_month == 0 && cr_day - lt_day < 31 && cr_day - lt_day >= 15)
                        welcomeText.setText("${cr_day-lt_day}만의 방문! \n앞으로 자주 공부해봐요!")
                    else if(cr_month - lt_month == 0 && cr_day - lt_day < 15 && cr_day - lt_day >= 5)
                        welcomeText.setText("${cr_day-lt_day}만의 방문! \n잘하고 있어요!")
                    else if(cr_month - lt_month == 0 && cr_day - lt_day < 5 && cr_day - lt_day >= 2)
                        welcomeText.setText("${cr_day-lt_day}만의 방문! \n최고! 내일도 파이팅!")
                    else if(cr_month - lt_month == 0 && cr_day - lt_day == 1)
                        welcomeText.setText("${cr_day-lt_day}만의 방문! \n와우! 정말 잘하고 있어요!")
                    else
                        welcomeText.setText("참 잘하고 있어요! \n또 재밌게 대화를 나눠봐요!")
                }
                else{
                    Log.e("Account", "no response")
                }
            }
            override fun onFailure(call: Call<MainPage.AccountResponseModel>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
    fun getLastLoginDate(inputString : String?) {
        // "T"를 기준으로 문자열을 분리
        val parts = inputString?.split("T")

        if (parts?.size == 2) {
            val datePart = parts[0] // "2023-09-20"

            // "-"를 기준으로 년, 월, 일을 분리
            val dateParts = datePart.split("-")

            if (dateParts.size == 3) {
                lt_year = dateParts[0].toInt() // 년
                lt_month = dateParts[1].toInt() // 월
                lt_day = dateParts[2].toInt() // 일

                // 결과 출력
                println("Year: $lt_year")
                println("Month: $lt_month")
                println("Day: $lt_day")
            } else {
                println("Invalid date format")
            }
        } else {
            println("Invalid input format")
        }
    }
}