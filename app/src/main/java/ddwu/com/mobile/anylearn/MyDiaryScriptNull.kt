package ddwu.com.mobile.anylearn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.annotations.SerializedName
import ddwu.com.mobile.anylearn.R
import ddwu.com.mobile.anylearn.databinding.ActivityMyDiaryMainBinding
import ddwu.com.mobile.anylearn.databinding.ActivityMyDiaryScriptBinding
import ddwu.com.mobile.anylearn.databinding.ActivityMyDiaryScriptNullBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class MyDiaryScriptNull : AppCompatActivity() {
    lateinit var mdsnBinding: ActivityMyDiaryScriptNullBinding
    var selectedYear by Delegates.notNull<Int>()
    var selectedMonth by Delegates.notNull<Int>()
    var selectedDay by Delegates.notNull<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mdsnBinding = ActivityMyDiaryScriptNullBinding.inflate(layoutInflater)
        setContentView(mdsnBinding.root)

        mdsnBinding.diaryScriptHomeBtnNull.setOnClickListener {
            val intent = Intent(this, MainPage::class.java)
            startActivity(intent)
        }

        mdsnBinding.DiaryScriptNullSetting.setOnClickListener {
            val intent = Intent(this, SettingPage::class.java)
            startActivity(intent)
        }

        // Intent로부터 년/월/일 정보를 받아옵니다.
        selectedYear = intent.getIntExtra("selectedYear", 0)
        selectedMonth = intent.getIntExtra("selectedMonth", 0)
        selectedDay = intent.getIntExtra("selectedDay", 0)

        mdsnBinding.diaryScriptYearNull.setText("${selectedYear}년")
        mdsnBinding.diaryScriptMonthNull.setText("${selectedMonth}월")
        mdsnBinding.diaryScriptDayNull.setText("${selectedDay}일")

        //이전 누르면 전일로 이동
        mdsnBinding.diaryPreBtnNull.setOnClickListener {
            MyDiaryScriptInfo(selectedYear, selectedMonth, selectedDay-1)
        }

        //다음 누르면 후일로 이동
        mdsnBinding.diaryNextBtnNull.setOnClickListener {
            MyDiaryScriptInfo(selectedYear, selectedMonth, selectedDay+1)
        }
    }

    private fun calculatePreviousDate (year: Int, month: Int, day: Int) {

        // 날짜 변경 로직 추가
        val isFirstDayOfMonth = day+1 == 1
        val isLastDayOfMonth =
            day-1 == 31 && (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12)
        val isLeapYear = year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)

        if (isFirstDayOfMonth) {
            if (month == 1) {
                selectedYear = year - 1
                selectedMonth = 12
                selectedDay = 31
            } else {
                val previousMonth = month - 1
                val previousMonthDays = when (previousMonth) {
                    4, 6, 9, 11 -> 30
                    2 -> if (isLeapYear) 29 else 28
                    else -> 31
                }
                selectedMonth = previousMonth
                selectedDay = previousMonthDays
            }
        }
        else if (isLastDayOfMonth) {
            if (month == 12) {
                selectedYear = year + 1
                selectedMonth = 1
                selectedDay = 1
            } else {
                val nextMonth = month + 1
                val nextMonthDays = 1
                selectedMonth = nextMonth
                selectedDay = nextMonthDays
            }
        }
        else {
            selectedDay = day
        }

        // 변경된 날짜를 텍스트로 출력
        Log.d("MyDiaryMain", "Formatted Date: $selectedYear-$selectedMonth-$selectedDay")
    }

    fun MyDiaryScriptInfo(year: Int, month: Int, day: Int) {
        val mySharedPreferences = MySharedPreferences(this)
        lateinit var dateString : String

        calculatePreviousDate(year, month, day)

        if(selectedMonth < 10) {
            if (selectedDay < 10)
                dateString = "$selectedYear-0$selectedMonth-0$selectedDay"
            else
                dateString = "$selectedYear-0$selectedMonth-$selectedDay"
        }
        else {
            if (selectedDay < 10)
                dateString = "$selectedYear-$selectedMonth-0$selectedDay"
            else
                dateString = "$selectedYear-$selectedMonth-$selectedDay"
        }

        val apiService = RetrofitConfig(this).retrofit.create(MyDiaryMainApiService::class.java)
        val csrfToken = mySharedPreferences.getCsrfToken()
        val cookieToken = mySharedPreferences.getCookieToken()
        val sessionId = mySharedPreferences.getSessionId()
        val call: Call<MyDiaryMain.MyDiaryMainResponseModel> =
            apiService.postSubject( "$csrfToken","csrftoken=$cookieToken; sessionid=$sessionId", dateString)

        call.enqueue(object : Callback<MyDiaryMain.MyDiaryMainResponseModel> {
            override fun onResponse(
                call: Call<MyDiaryMain.MyDiaryMainResponseModel>,
                response: Response<MyDiaryMain.MyDiaryMainResponseModel>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val diaryContents = responseBody?.diaryContents
                    val comment = responseBody?.comment

                    if (diaryContents != null) {
                        if (diaryContents.isNotEmpty()) {
                            // diaryContents 내용을 로그로 출력
                            for (content in diaryContents) {
                                Log.e("Diary Content", "Contents: ${content.contents}")
                                Log.e(
                                    "Diary Content",
                                    "Hashtags: ${content.hashtag.joinToString(", ") { hashtag -> hashtag.tag }}"
                                )
                                Log.e("Diary Content", "Add Diary: ${content.add_diary}")
                                Log.e("Diary Content", "Show Expression: ${content.show_expr}")
                                Log.e("Diary Content", "Input Expression: ${content.input_expr}")
                            }

                            val intent = Intent(this@MyDiaryScriptNull, MyDiaryScript::class.java)
                            intent.putExtra("selectedYear", selectedYear)
                            intent.putExtra("selectedMonth", selectedMonth)
                            intent.putExtra("selectedDay", selectedDay)

                            // diaryContents를 Intent에 추가
                            intent.putExtra("diaryContents", diaryContents.toTypedArray())

                            if (comment != null){
                                if (comment.isNotEmpty())
                                    intent.putExtra("comment", comment)
                            }
                            startActivity(intent)
                        } else {
                            // diaryContents가 비어 있는 경우 처리
                            Log.e("MyDiaryMain", "diaryContents is empty")

                            val intent = Intent(this@MyDiaryScriptNull, MyDiaryScriptNull::class.java)
                            intent.putExtra("selectedYear", selectedYear)
                            intent.putExtra("selectedMonth", selectedMonth)
                            intent.putExtra("selectedDay", selectedDay)

                            if (comment != null){
                                if (comment.isNotEmpty())
                                    intent.putExtra("comment", comment)
                            }
                            startActivity(intent)
                        }
                    } else {
                        // diaryContents가 null일 때 처리
                        Log.e("MyDiaryMain", "diaryContents is null")

                        val intent = Intent(this@MyDiaryScriptNull, MyDiaryScriptNull::class.java)
                        intent.putExtra("selectedYear", selectedYear)
                        intent.putExtra("selectedMonth", selectedMonth)
                        intent.putExtra("selectedDay", selectedDay)

                        if (comment != null){
                            if (comment.isNotEmpty())
                                intent.putExtra("comment", comment)
                        }
                        startActivity(intent)
                    }
                }
                else{
                    // 응답이 없을때
                    Log.e("MyDiaryMain", "NO response")

                    val intent = Intent(this@MyDiaryScriptNull, MyDiaryScriptNull::class.java)
                    intent.putExtra("selectedYear", selectedYear)
                    intent.putExtra("selectedMonth", selectedMonth)
                    intent.putExtra("selectedDay", selectedDay)
                    startActivity(intent)
                }
            }
            override fun onFailure(call: Call<MyDiaryMain.MyDiaryMainResponseModel>, t: Throwable) {
                Log.e("MyDiaryMain", "HTTP MyDiaryMain request error: ${t.message}")
            }
        })
    }
}