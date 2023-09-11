package ddwu.com.mobile.anylearn

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import ddwu.com.mobile.anylearn.R

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

        override fun onBindViewHolder(holder: MenuItemViewHolder, position: Int) {
            val menuItem = menuItems[position]
            holder.button.text = menuItem

            // 버튼 클릭 이벤트 처리 (필요에 따라 구현)
            holder.button.setOnClickListener {
                when (menuItem) {
                    "계정 설정" -> {
                        val intent = Intent(context, AccountSettingPage::class.java)
                        context.startActivity(intent)
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
}