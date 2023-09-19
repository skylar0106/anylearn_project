package ddwu.com.mobile.anylearn

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ScriptsListAdapter(val context: Context, val scriptList: ArrayList<MyScriptList.Item>) :
    RecyclerView.Adapter<ScriptsListAdapter.MyViewHolder>() {

    override fun getItemCount(): Int = scriptList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_scripts_list_adapter, parent, false)
        return MyViewHolder(context, itemView, scriptList)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val script = scriptList[position]
        holder.title.text = script.title
        holder.date.text = script.learningDate
    }

    class MyViewHolder(context: Context, view: View, scriptList: List<MyScriptList.Item>) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.script_list_title)
        val date: TextView = view.findViewById(R.id.script_list_date)

        init {
            view.setOnClickListener {
                val script = scriptList[adapterPosition]
                val intent = Intent(context, MyScript::class.java)
                intent.putExtra("script_title", script.title)
                intent.putExtra("script_learningDate", script.learningDate)
                intent.putExtra("script_contents", script.contents)
                intent.putExtra("script_addDiary", script.addDiary)
                intent.putExtra("script_hashtag", script.hashtag)
                Log.d("script내용 확인", "title: ${script.title}, learningDate: ${script.learningDate}")
                context.startActivity(intent)
            }
        }

    }
}

