package ddwu.com.mobile.anylearn

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.gson.annotations.SerializedName
import com.prolificinteractive.materialcalendarview.*
import com.prolificinteractive.materialcalendarview.format.TitleFormatter
import com.prolificinteractive.materialcalendarview.format.WeekDayFormatter
import ddwu.com.mobile.anylearn.databinding.ActivityMyDiaryMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


class MyDiaryMain : AppCompatActivity() {

    lateinit var mdmBinding: ActivityMyDiaryMainBinding
    private lateinit var yearSpinner: Spinner
    private lateinit var monthSpinner: Spinner
    private lateinit var calendarView: MaterialCalendarView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mdmBinding = ActivityMyDiaryMainBinding.inflate(layoutInflater)
        setContentView(mdmBinding.root)

        yearSpinner = mdmBinding.yearSpinner
        monthSpinner = mdmBinding.monthSpinner
        calendarView = mdmBinding.calendarView

        // 년도 스피너를 초기화하고 어댑터 연결
        val yearList = createYearList()
        val yearAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, yearList)
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        yearSpinner.adapter = yearAdapter

        // 현재 월의 인덱스를 가져옵니다.
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)

        // 월 스피너를 초기화하고 어댑터 연결
        val monthList = createMonthList()
        val monthAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, monthList)
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        monthSpinner.adapter = monthAdapter

        // 현재 월을 초기 선택으로 설정합니다.
        monthSpinner.setSelection(currentMonth)

        // 스피너 선택 리스너 설정
        yearSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                updateCalendar()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // 아무것도 선택되지 않았을 때의 동작
            }
        })

        monthSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                updateCalendar()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // 아무것도 선택되지 않았을 때의 동작
            }
        })

        val sundayDecorator = SundayDecorator()
        val saturdayDecorator = SaturdayDecorator()
        val todayDecorator = TodayDecorator(this)
        val weekdayDecorator = WeekdayDecorator()

        calendarView.addDecorators(
            sundayDecorator,
            saturdayDecorator,
            todayDecorator,
            weekdayDecorator
        )
        //mdmBinding.calendarView.setTopbarVisible(false)

        val koreanWeekDayFormatter: WeekDayFormatter = KoreanWeekDayFormatter()
        calendarView.setWeekDayFormatter(koreanWeekDayFormatter)

        calendarView.setWeekDayTextAppearance(R.style.CustomCalendarWidget);
        calendarView.setDateTextAppearance(R.style.CustomDateTextAppearance);

        val customTitleFormatter: TitleFormatter = CustomTitleFormatter()
        calendarView.setTitleFormatter(customTitleFormatter)

        val clickDateDecorator = object : DayViewDecorator {
            override fun shouldDecorate(day: CalendarDay): Boolean {
                // 클릭한 날짜에만 적용하려면 클릭한 날짜와 같은 날짜에만 true를 반환합니다.
                return day == calendarView.selectedDate
            }

            override fun decorate(view: DayViewFacade) {
                view.addSpan(ForegroundColorSpan(Color.WHITE))
            }
        }

        calendarView.setOnDateChangedListener { widget, date, selected ->
            // 클릭한 날짜의 글자색을 변경합니다.
            widget.invalidateDecorators()
        }

        // 클릭한 날짜의 글자색을 변경하는 Decorator를 추가합니다.
        calendarView.addDecorator(clickDateDecorator)

        mdmBinding.diaryMainHomeBtn.setOnClickListener {
            val intent = Intent(this, MainPage::class.java)
            startActivity(intent)
        }
        mdmBinding.myDiaryMainSetting.setOnClickListener {
            val intent = Intent(this, SettingPage::class.java)
            startActivity(intent)
        }

        // 초기 달력 업데이트
        updateCalendar()

        calendarView.setOnDateChangedListener { widget, date, selected ->
            // 클릭한 날짜의 년, 월, 일 정보를 추출합니다.
            val year = date.year
            val month = date.month + 1 // 월은 0부터 시작하므로 1을 더합니다.
            val day = date.day

            MyDiaryScriptInfo(year, month, day)
        }
    }

    private fun createYearList(): List<String> {
        val yearList = mutableListOf<String>()
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        for (i in currentYear downTo 1900) {
            yearList.add(i.toString())
        }
        return yearList
    }

    private fun createMonthList(): List<String> {
        return listOf(
            "1월", "2월", "3월", "4월", "5월", "6월",
            "7월", "8월", "9월", "10월", "11월", "12월"
        )
    }

    inner class TodayDecorator(context: Context) : DayViewDecorator {
        private var date = CalendarDay.today()
        val drawable = ContextCompat.getDrawable(context, R.drawable.style_only_radius_10)
        override fun shouldDecorate(day: CalendarDay?): Boolean {
            return day?.equals(date)!!
        }

        override fun decorate(view: DayViewFacade?) {
            if (drawable != null) {
                view?.setBackgroundDrawable(drawable)
            }
        }
    }

    inner class SundayDecorator : DayViewDecorator {
        private val calendar = Calendar.getInstance()
        override fun shouldDecorate(day: CalendarDay?): Boolean {
            day?.copyTo(calendar)
            val weekDay = calendar.get(Calendar.DAY_OF_WEEK)
            return weekDay == Calendar.SUNDAY
        }

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(object : ForegroundColorSpan(Color.RED) {})
        }
    }

    inner class SaturdayDecorator : DayViewDecorator {
        private val calendar = Calendar.getInstance()
        override fun shouldDecorate(day: CalendarDay?): Boolean {
            day?.copyTo(calendar)
            val weekDay = calendar.get(Calendar.DAY_OF_WEEK)
            return weekDay == Calendar.SATURDAY
        }

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(object : ForegroundColorSpan(Color.BLUE) {})
        }
    }

    inner class WeekdayDecorator : DayViewDecorator {
        private val calendar = Calendar.getInstance()
        override fun shouldDecorate(day: CalendarDay?): Boolean {
            day?.copyTo(calendar)
            val weekDay = calendar.get(Calendar.DAY_OF_WEEK)
            return weekDay in Calendar.MONDAY..Calendar.FRIDAY
        }

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(ForegroundColorSpan(Color.parseColor("#375076")))
        }
    }

    private fun updateCalendar() {
        val selectedYear = yearSpinner.selectedItem.toString().toInt()
        val selectedMonth = monthSpinner.selectedItemPosition// 0부터 시작하는 인덱스

        // 달력 뷰 업데이트
        val calendar = Calendar.getInstance()
        calendar.set(selectedYear, selectedMonth - 1, 1) // 월은 0부터 시작하므로 -1
        calendarView.setCurrentDate(CalendarDay.from(selectedYear, selectedMonth, 1))
        calendarView.setSelectedDate(CalendarDay.from(selectedYear, selectedMonth, 1))

        // 선택한 날짜를 텍스트로 표시
        val dateFormat = SimpleDateFormat("yyyy년 MM월", Locale.getDefault())
    }

    data class MyDiaryMainResponseModel(
        @SerializedName("UserEmail") val user_email: UserEmail,
        @SerializedName("diaryContents") val diaryContents: List<DiaryContent>,
        @SerializedName("comment") val comment: String
    )

    data class UserEmail(
        val email: String
    ): Serializable

    data class DiaryContent(
        @SerializedName("hashtag") val hashtag: List<Hashtag>,
        @SerializedName("id") val id: Int,
        @SerializedName("contents") val contents: String,
        @SerializedName("add_diary") val add_diary: Int,
        @SerializedName("show_expr") val show_expr: Int,
        @SerializedName("input_expr") val input_expr: String
    ) : Serializable

    data class Hashtag(
        val tag: String
    ) : Serializable

    fun MyDiaryScriptInfo(year: Int, month: Int, day: Int) {
        val mySharedPreferences = MySharedPreferences(this)
        lateinit var dateString : String

        if(month < 10) {
            if (day < 10)
                dateString = "$year-0$month-0$day"
            else
                dateString = "$year-0$month-$day"
        }
        else {
            if (day < 10)
                dateString = "$year-$month-0$day"
            else
                dateString = "$year-$month-$day"
        }

        val apiService = RetrofitConfig(this).retrofit.create(MyDiaryMainApiService::class.java)
        val csrfToken = mySharedPreferences.getCsrfToken()
        val cookieToken = mySharedPreferences.getCookieToken()
        val sessionId = mySharedPreferences.getSessionId()
        val call: Call<MyDiaryMainResponseModel> =
            apiService.postSubject( "$csrfToken","csrftoken=$cookieToken; sessionid=$sessionId", dateString)

        call.enqueue(object : Callback<MyDiaryMainResponseModel> {
            override fun onResponse(
                call: Call<MyDiaryMainResponseModel>,
                response: Response<MyDiaryMainResponseModel>
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

                            val intent = Intent(this@MyDiaryMain, MyDiaryScript::class.java)
                            intent.putExtra("selectedYear", year)
                            intent.putExtra("selectedMonth", month)
                            intent.putExtra("selectedDay", day)

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

                            val intent = Intent(this@MyDiaryMain, MyDiaryScriptNull::class.java)
                            intent.putExtra("selectedYear", year)
                            intent.putExtra("selectedMonth", month)
                            intent.putExtra("selectedDay", day)

                            if (comment != null){
                                if (comment.isNotEmpty())
                                    intent.putExtra("comment", comment)
                            }
                            startActivity(intent)
                        }
                    } else {
                        // diaryContents가 null일 때 처리
                        Log.e("MyDiaryMain", "diaryContents is null")

                        val intent = Intent(this@MyDiaryMain, MyDiaryScriptNull::class.java)
                        intent.putExtra("selectedYear", year)
                        intent.putExtra("selectedMonth", month)
                        intent.putExtra("selectedDay", day)

                        if (comment != null){
                            if (comment.isNotEmpty())
                                intent.putExtra("comment", comment)
                        }
                        startActivity(intent)
                    }
                }
                else{
                    // 응답 없을때
                    Log.e("MyDiaryMain", "No response")

                    val intent = Intent(this@MyDiaryMain, MyDiaryScriptNull::class.java)
                    intent.putExtra("selectedYear", year)
                    intent.putExtra("selectedMonth", month)
                    intent.putExtra("selectedDay", day)
                    startActivity(intent)
                }
            }
            override fun onFailure(call: Call<MyDiaryMainResponseModel>, t: Throwable) {
                Log.e("MyDiaryMain", "HTTP MyDiaryMain request error: ${t.message}")
            }
        })
    }
}





