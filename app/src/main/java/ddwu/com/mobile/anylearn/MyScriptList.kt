package ddwu.com.mobile.anylearn

import ScriptsGetService
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.annotations.SerializedName
import ddwu.com.mobile.anylearn.databinding.ActivityMyScriptListBinding
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyScriptList : AppCompatActivity() {

    lateinit var mslBinding : ActivityMyScriptListBinding
    companion object{
        lateinit var scriptListOut: MutableList<outModel>
        lateinit var scriptList1: ArrayList<Item>
        lateinit var scriptList2: ArrayList<Item>
        lateinit var scriptList3: ArrayList<Item>
        lateinit var scriptList4: ArrayList<Item>
        lateinit var scriptList5: ArrayList<Item>
        lateinit var scriptList6: ArrayList<Item>
        lateinit var scriptList7: ArrayList<Item>
        lateinit var scriptList8: ArrayList<Item>
        lateinit var scriptList9: ArrayList<Item>
        lateinit var scriptList10: ArrayList<Item>
        lateinit var scriptList11: ArrayList<Item>
        lateinit var scriptList12: ArrayList<Item>
        lateinit var scriptTitle: String
        lateinit var date: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mslBinding = ActivityMyScriptListBinding.inflate(layoutInflater)
        setContentView(mslBinding.root)

        mslBinding.scriptListHomeBtn.setOnClickListener {
            val intent = Intent(this, MainPage::class.java)
            startActivity(intent)
        }

        val settingButton: Button = findViewById(R.id.MyScriptListSetting)
        settingButton.setOnClickListener {
            val intent = Intent(this, SettingPage::class.java)
            startActivity(intent)
        }

        // checkConnection 함수 호출
        scriptListGet()
        Log.d("ListCheck", "List: $scriptListOut")
        // 필요한 작업 수행 어댑터 다시



    }
    data class outModel(
        val month: String,
        val ListItem: ArrayList<Item>
    )
    data class ListType(
        val title: String,
        val date: String
    )
    data class Item(
        @SerializedName("title") val title: String,
        @SerializedName("hashtag") val hashtag: List<String>,
        @SerializedName("contents") val contents: String,
        @SerializedName("level") val level: String,
        @SerializedName("learningDate") val learningDate: String,
        @SerializedName("add_diary") val addDiary: Int,
        @SerializedName("show_expr") val showExpr: Int,
        @SerializedName("input_expr") val inputExpr: String,
        @SerializedName("email") val email: String
    )
    data class ScriptListResponseModel(
//        val ListData: Map<String, MutableList<Item>>,
//        val ListData2:  MutableList<Item>
        val month: String,
        val ListItem: MutableList<Item>
    )
    data class ScriptListResponseModel2(
        val ListData2:  MutableList<Item>
    )

    private fun scriptListGet() {
        scriptListOut = mutableListOf()
        scriptList1 = ArrayList()
        scriptList2 = ArrayList()
        scriptList3 = ArrayList()
        scriptList4 = ArrayList()
        scriptList5 = ArrayList()
        scriptList6 = ArrayList()
        scriptList7 = ArrayList()
        scriptList8 = ArrayList()
        scriptList9 = ArrayList()
        scriptList10 = ArrayList()
        scriptList11 = ArrayList()
        scriptList12 = ArrayList()

        val mySharedPreferences = MySharedPreferences(this)
        val apiService = RetrofitConfig(this).retrofit.create(ScriptsGetService::class.java)

        val cookieToken = mySharedPreferences.getCookieToken()
        val sessionId = mySharedPreferences.getSessionId()
        val call: Call<Map<String, MutableList<Item>>> = apiService.scripListGet("csrftoken=$cookieToken; sessionid=$sessionId")

        call.enqueue(object : Callback<Map<String, MutableList<Item>>> {
            override fun onResponse(call: Call<Map<String, MutableList<Item>>>, response: Response<Map<String, MutableList<Item>>>) {
                if (response.isSuccessful) {
                    // 성공적인 응답을 받았을 때 수행할 작업
                    Log.d("onResponse", "성공")
                    val responseBody = response.body()

                    var keyCheck = "1"
                    if (responseBody != null) {
                        Log.d("onResponse", "null아님")
                        for ((key, values) in responseBody) {
                            Log.d("keys", "key: $key")
                            for (value in values) {
                                if (key == "1") {
                                    scriptList1.add(value)
                                } else if (key == "2") {
                                    scriptList2.add(value)
                                } else if (key == "3") {
                                    scriptList3.add(value)
                                } else if (key == "4") {
                                    scriptList4.add(value)
                                } else if (key == "5") {
                                    scriptList5.add(value)
                                } else if (key == "6") {
                                    scriptList6.add(value)
                                } else if (key == "7") {
                                    scriptList7.add(value)
                                } else if (key == "8") {
                                    scriptList8.add(value)
                                } else if (key == "9") {
                                    scriptList9.add(value)
                                } else if (key == "10") {
                                    scriptList10.add(value)
                                } else if (key == "11") {
                                    scriptList11.add(value)
                                } else if (key == "12") {
                                    scriptList12.add(value)
                                }
                            }
                        }

                    }
                    scriptListOut.add(outModel("1월", scriptList1))
                    scriptListOut.add(outModel("2월", scriptList2))
                    scriptListOut.add(outModel("3월", scriptList3))
                    scriptListOut.add(outModel("4월", scriptList4))
                    scriptListOut.add(outModel("5월", scriptList5))
                    scriptListOut.add(outModel("6월", scriptList6))
                    scriptListOut.add(outModel("7월", scriptList7))
                    scriptListOut.add(outModel("8월", scriptList8))
                    scriptListOut.add(outModel("9월", scriptList9))
                    scriptListOut.add(outModel("10월", scriptList10))
                    scriptListOut.add(outModel("11월", scriptList11))
                    scriptListOut.add(outModel("12월", scriptList12))
                    

                    val adapter = ScriptListAdapterOut(this@MyScriptList, scriptListOut)
                    mslBinding.scriptListMainRecyclerview.adapter = adapter

                    val layoutManager = LinearLayoutManager(this@MyScriptList)
                    layoutManager.orientation = LinearLayoutManager.VERTICAL
                    mslBinding.scriptListMainRecyclerview.layoutManager = layoutManager

                }
                else {
                    Log.e("ScriptListGet", "HTTP signup request failed. Error code: ${response.code()}")
                    Log.e("ScriptListGet", " cookieToken: $cookieToken" + " sessionId: $sessionId")
                }
            }

            override fun onFailure(call: Call<Map<String, MutableList<Item>>>, t: Throwable) {
                Log.e("Signup", "HTTP withaiselect request error: ${t.message}")
            }
        })
    }

}
