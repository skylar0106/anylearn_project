package ddwu.com.mobile.anylearn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ddwu.com.mobile.anylearn.databinding.ActivityMyDiaryScriptBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class MyDiaryScript : AppCompatActivity() {
    lateinit var mdsBinding: ActivityMyDiaryScriptBinding
    lateinit var recyclerView: RecyclerView
    var selectedYear by Delegates.notNull<Int>()
    var selectedMonth by Delegates.notNull<Int>()
    var selectedDay by Delegates.notNull<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mdsBinding = ActivityMyDiaryScriptBinding.inflate(layoutInflater)
        setContentView(mdsBinding.root)

        recyclerView = mdsBinding.diaryScriptComment // XML에서 RecyclerView를 가져옴

        // Intent로부터 diaryContents 데이터를 가져옴
        val diaryContents = intent.getSerializableExtra("diaryContents") as? Array<MyDiaryMain.DiaryContent>
        val diaryContentsList = ArrayList(diaryContents?.toList())

        // Intent로부터 comment 데이터를 가져옴
        val comment = intent.getStringExtra("comment")

        if(comment != null)
            mdsBinding.diaryScriptUsercomment.setText(comment)

        // RecyclerView에 레이아웃 매니저 설정 (수직 스크롤)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // RecyclerView 어댑터 생성 및 설정
        val adapter = DiaryContentAdapter(diaryContentsList)
        recyclerView.adapter = adapter

        mdsBinding.diaryScriptHomeBtn.setOnClickListener {
            val intent = Intent(this, MainPage::class.java)
            startActivity(intent)
        }

        mdsBinding.DiaryScriptSetting.setOnClickListener {
            val intent = Intent(this, SettingPage::class.java)
            startActivity(intent)
        }

        // Intent로부터 년/월/일 정보를 받아옵니다.
        selectedYear = intent.getIntExtra("selectedYear", 0)
        selectedMonth = intent.getIntExtra("selectedMonth", 0)
        selectedDay = intent.getIntExtra("selectedDay", 0)

        mdsBinding.diaryScriptYear.setText("${selectedYear}년")
        mdsBinding.diaryScriptMonth.setText("${selectedMonth}월")
        mdsBinding.diaryScriptDay.setText("${selectedDay}일")

        //이전 누르면 전일로 이동
        mdsBinding.diaryPreBtn.setOnClickListener {
            MyDiaryScriptInfo(selectedYear, selectedMonth, selectedDay-1)
        }

        //다음 누르면 후일로 이동
        mdsBinding.diaryNextBtn.setOnClickListener {
            MyDiaryScriptInfo(selectedYear, selectedMonth, selectedDay+1)
        }

        //수정하기 누르면 수정가능
        mdsBinding.diaryScriptChangeBtn.setOnClickListener{
            // RecyclerView의 모든 아이템에 있는 EditText의 focusable을 true로 설정하여 수정 가능하게 함
            for (i in 0 until recyclerView.childCount) {
                val viewHolder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i))
                if (viewHolder is DiaryContentAdapter.DiaryContentViewHolder) {
                    viewHolder.contentsTextView.isFocusable = true
                    viewHolder.contentsTextView.isFocusableInTouchMode = true
                    viewHolder.hashtagTextView.isFocusable = true
                    viewHolder.hashtagTextView.isFocusableInTouchMode = true
                }
            }
        }
    }

    private fun calculatePreviousDate (year: Int, month: Int, day: Int) {

        // 날짜 변경 로직 추가
        val isFirstDayOfMonth = day+1 == 1
        val isLastDayOfMonth_Feb =
            (day-1 == 28 || day-1 == 29) && month == 2
        val isLastDayOfMonth_30 =
            day-1 == 30 && (month == 4 || month == 6 || month == 9 || month == 11)
        val isLastDayOfMonth_31 =
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
        else if (isLastDayOfMonth_31) {
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
        else if (isLastDayOfMonth_30) {
            val nextMonth = month + 1
            val nextMonthDays = 1
            selectedMonth = nextMonth
            selectedDay = nextMonthDays
        }
        else if (isLastDayOfMonth_Feb) {
            if (isLeapYear){
                if(day-1 == 29) {
                    val nextMonth = 3
                    val nextMonthDays = 1
                    selectedMonth = nextMonth
                    selectedDay = nextMonthDays
                }
                else
                    selectedDay = day
            }
            else{
                if(day-1 == 28) {
                    val nextMonth = 3
                    val nextMonthDays = 1
                    selectedMonth = nextMonth
                    selectedDay = nextMonthDays
                }
                else
                    selectedDay = day
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
                                    "Hashtags: ${content.hashtag?.joinToString(", ") { hashtag -> hashtag.tag }}"
                                )
                                Log.e("Diary Content", "Add Diary: ${content.add_diary}")
                                Log.e("Diary Content", "Show Expression: ${content.show_expr}")
                                Log.e("Diary Content", "Input Expression: ${content.input_expr}")
                            }

                            val diaryContentsList = ArrayList(diaryContents?.toList())

                            if(comment != null) {
                                if (comment.isNotEmpty())
                                    mdsBinding.diaryScriptUsercomment.setText(comment)
                                else
                                    mdsBinding.diaryScriptUsercomment.setText("오늘의 스터디 코멘트를 남겨보세요")
                            }

                            mdsBinding.diaryScriptYear.setText("${selectedYear}년")
                            mdsBinding.diaryScriptMonth.setText("${selectedMonth}월")
                            mdsBinding.diaryScriptDay.setText("${selectedDay}일")

                            // RecyclerView에 레이아웃 매니저 설정 (수직 스크롤)
                            recyclerView.layoutManager = LinearLayoutManager(this@MyDiaryScript)

                            // RecyclerView 어댑터 생성 및 설정
                            val adapter = DiaryContentAdapter(diaryContentsList)
                            recyclerView.adapter = adapter

                            // 다이어리 내용 갱신
                            adapter.updateDiaryContents(diaryContentsList)
                            adapter.notifyDataSetChanged()

                        } else {
                            // diaryContents가 비어 있는 경우 처리
                            Log.e("MyDiaryMain", "diaryContents is empty")

                            if(comment != null) {
                                if (comment.isNotEmpty())
                                    mdsBinding.diaryScriptUsercomment.setText(comment)
                                else
                                    mdsBinding.diaryScriptUsercomment.setText("오늘의 스터디 코멘트를 남겨보세요")
                            }

                            mdsBinding.diaryScriptYear.setText("${selectedYear}년")
                            mdsBinding.diaryScriptMonth.setText("${selectedMonth}월")
                            mdsBinding.diaryScriptDay.setText("${selectedDay}일")

                            val emptyDiaryContentsList = ArrayList(diaryContents?.toList())

                            // RecyclerView에 레이아웃 매니저 설정 (수직 스크롤)
                            recyclerView.layoutManager = LinearLayoutManager(this@MyDiaryScript)

                            // RecyclerView 어댑터 생성 및 설정
                            val adapter = DiaryContentAdapter(emptyDiaryContentsList)
                            recyclerView.adapter = adapter

                            // 다이어리 내용이 없을 때 RecyclerView 어댑터 갱신
                            adapter.updateDiaryContents(emptyDiaryContentsList)
                            adapter.notifyDataSetChanged()
                        }
                    } else {
                        // diaryContents가 null일 때 처리
                        Log.e("MyDiaryMain", "diaryContents is null")

                        if(comment != null) {
                            if (comment.isNotEmpty())
                                mdsBinding.diaryScriptUsercomment.setText(comment)
                            else
                                mdsBinding.diaryScriptUsercomment.setText("오늘의 스터디 코멘트를 남겨보세요")
                        }

                        mdsBinding.diaryScriptYear.setText("${selectedYear}년")
                        mdsBinding.diaryScriptMonth.setText("${selectedMonth}월")
                        mdsBinding.diaryScriptDay.setText("${selectedDay}일")

                        val emptyDiaryContentsList = listOf(
                            MyDiaryMain.DiaryContent(null, null, "저장된 다이어리가 없습니다", null, null, null)
                        )
                        // RecyclerView에 레이아웃 매니저 설정 (수직 스크롤)
                        recyclerView.layoutManager = LinearLayoutManager(this@MyDiaryScript)

                        // RecyclerView 어댑터 생성 및 설정
                        val adapter = DiaryContentAdapter(emptyDiaryContentsList)
                        recyclerView.adapter = adapter

                        // 다이어리 내용이 없을 때 RecyclerView 어댑터 갱신
                        adapter.updateDiaryContents(emptyDiaryContentsList)
                        adapter.notifyDataSetChanged()
                    }
                }
                else{
                    // 응답이 없을때
                    mdsBinding.diaryScriptYear.setText("${selectedYear}년")
                    mdsBinding.diaryScriptMonth.setText("${selectedMonth}월")
                    mdsBinding.diaryScriptDay.setText("${selectedDay}일")

                    mdsBinding.diaryScriptUsercomment.setText("오늘의 스터디 코멘트를 남겨보세요")

                    val emptyDiaryContentsList = listOf(
                        MyDiaryMain.DiaryContent(null, null, "저장된 다이어리가 없습니다", null, null, null)
                    )
                    // RecyclerView에 레이아웃 매니저 설정 (수직 스크롤)
                    recyclerView.layoutManager = LinearLayoutManager(this@MyDiaryScript)

                    // RecyclerView 어댑터 생성 및 설정
                    val adapter = DiaryContentAdapter(emptyDiaryContentsList)
                    recyclerView.adapter = adapter

                    // 다이어리 내용이 없을 때 RecyclerView 어댑터 갱신
                    adapter.updateDiaryContents(emptyDiaryContentsList)
                    adapter.notifyDataSetChanged()
                }
            }
            override fun onFailure(call: Call<MyDiaryMain.MyDiaryMainResponseModel>, t: Throwable) {
                Log.e("MyDiaryMain", "HTTP MyDiaryMain request error: ${t.message}")
            }
        })
    }
}