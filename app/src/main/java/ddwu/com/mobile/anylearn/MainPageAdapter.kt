package ddwu.com.mobile.anylearn

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ddwu.com.mobile.anylearn.R

class MainPageAdapter (private val menuItems: List<MenuItem>, private val context: Context)
    : RecyclerView.Adapter<MainPageAdapter.MainPageViewHolder>() {

    class MainPageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val button1: Button = itemView.findViewById(R.id.MainPageButton1)
        val button2: Button = itemView.findViewById(R.id.MainPageButton2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainPageViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.main_page_menu_layout, parent, false)
        return MainPageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MainPageViewHolder, position: Int) {
        val currentItem = menuItems[position]

        holder.titleTextView.text = currentItem.title
        holder.descriptionTextView.text = currentItem.description

        if (currentItem.button1Text != " ") {
            holder.button1.text = currentItem.button1Text
            holder.button1.setOnClickListener{
                when(currentItem.button1Text){
                    "단계 설정 ①", "단계 설정 ②", "단계 설정 ③"-> {
                        val alertDialogBuilder = AlertDialog.Builder(context)
                        alertDialogBuilder.setTitle("단계 선택")

                        val items = arrayOf("①", "②", "③")
                        var selectedStep = -1

                        alertDialogBuilder.setSingleChoiceItems(items, -1) { dialog, which ->
                            selectedStep = which
                        }

                        alertDialogBuilder.setPositiveButton("확인") { dialog, which ->
                            // 팝업 확인 버튼을 눌렀을 때 선택한 단계에 대한 처리를 수행합니다.
                            if (selectedStep != -1) {
                                // 선택한 단계를 사용하여 처리할 작업을 수행합니다.
                                when (selectedStep) {
                                    0 -> {
                                        // 단계 1 선택 시 처리
                                        holder.button1.text = "단계 설정 ①"
                                    }
                                    1 -> {
                                        // 단계 2 선택 시 처리
                                        holder.button1.text = "단계 설정 ②"
                                    }
                                    2 -> {
                                        // 단계 3 선택 시 처리
                                        holder.button1.text = "단계 설정 ③"
                                    }
                                }
                            }
                        }

                        alertDialogBuilder.setNegativeButton("취소") { dialog, which ->
                            // 팝업 취소 버튼을 눌렀을 때 수행할 동작을 작성합니다.
                        }

                        val alertDialog = alertDialogBuilder.create()
                        alertDialog.show()
                    }
                }
            }
        } else {
            holder.button1.visibility = View.GONE
            holder.button1.isEnabled = false
        }

        holder.button2.text = currentItem.button2Text
        // 버튼 클릭 이벤트 처리 (필요에 따라 구현)
        holder.button2.setOnClickListener {
            when (currentItem.button2Text) {
                "시작하기" -> {
                    val intent = Intent(context, WithaiSelect::class.java)
                    context.startActivity(intent)
                }
                "확인하기" -> {
                    val intent = Intent(context, MyScriptList::class.java)
                    context.startActivity(intent)
                }
                "목록" -> {
                    val intent = Intent(context, MyDiaryMain::class.java)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount() = menuItems.size
}

data class MenuItem(
    val title: String,
    val description: String,
    val button1Text: String,
    val button2Text: String
)