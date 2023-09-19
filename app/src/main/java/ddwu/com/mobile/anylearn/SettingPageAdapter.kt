package ddwu.com.mobile.anylearn

import AccountSettingApiService
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.annotations.SerializedName
import ddwu.com.mobile.anylearn.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingPageAdapter(private val menuItems: List<String>, private val context: Context) :
    RecyclerView.Adapter<SettingPageAdapter.MenuItemViewHolder>(){
    class MenuItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val button: Button = itemView.findViewById(R.id.SettingPageButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuItemViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.setting_page_menu_layout, parent, false)
        return MenuItemViewHolder(itemView)
    }
    data class AccountResponseModel(
        @SerializedName("nickname") val nickname: String,
        @SerializedName("username") val username: String,
        @SerializedName("email") val email: String,
        @SerializedName("phonenumber") val phonenumber: String,
        @SerializedName("avatar") val profile: String
        //@SerializedName("pw") val pw: String
    )

    override fun onBindViewHolder(holder: MenuItemViewHolder, position: Int) {
        val menuItem = menuItems[position]
        holder.button.text = menuItem

        // 버튼 클릭 이벤트 처리 (필요에 따라 구현)
        holder.button.setOnClickListener {
            when (menuItem) {
                "계정 설정" -> {
                    getAccount()
                }
                "알림 설정" -> {
                    val intent = Intent(context, NotificationSettingPage::class.java)
                    context.startActivity(intent)
                }
                "화면 설정" -> {
                    val intent = Intent(context, DisplaySettingPage::class.java)
                    context.startActivity(intent)
                }
                "모드 설정" -> {
                    val intent = Intent(context, ModeSettingPage::class.java)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return menuItems.size
    }

    fun getAccount(){
        val mySharedPreferences = MySharedPreferences(context)

        val apiService = RetrofitConfig(context).retrofit.create(AccountSettingApiService::class.java)
        val csrfToken = mySharedPreferences.getCsrfToken()
        val cookieToken = mySharedPreferences.getCookieToken()
        val sessionId = mySharedPreferences.getSessionId()
        val call: Call<AccountResponseModel> =
            apiService.postSubject( "$csrfToken","csrftoken=$cookieToken; sessionid=$sessionId")

        call.enqueue(object : Callback<SettingPageAdapter.AccountResponseModel>{
            override fun onResponse(
                call: Call<AccountResponseModel>,
                response: Response<AccountResponseModel>
            ) {
                if(response.isSuccessful){
                    val responseBody = response.body()

                    val nickname = responseBody?.nickname
                    val username = responseBody?.username
                    val email = responseBody?.email
                    val phonenumber = responseBody?.phonenumber
                    val profile = responseBody?.profile
                    //val pw = responseBody?.pw

                    Log.d("Account", nickname + "\n" + username + "\n" + email + "\n" + phonenumber)
                    Log.d("Account", "이미지 uri : " + profile)

                    val intent = Intent(context, AccountSettingPage::class.java)
                    intent.putExtra("nickname", nickname)
                    intent.putExtra("username", username)
                    intent.putExtra("email", email)
                    intent.putExtra("phonenumber", phonenumber)
                    intent.putExtra("profile", profile)

                    context.startActivity(intent)
                }
                else{
                    Log.e("Account", "no response")
                }
            }
            override fun onFailure(call: Call<AccountResponseModel>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}