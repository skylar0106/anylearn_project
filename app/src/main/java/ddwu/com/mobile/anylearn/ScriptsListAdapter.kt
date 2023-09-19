package ddwu.com.mobile.anylearn

import android.content.Context
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
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val script = scriptList[position]
        holder.title.text = script.title
        holder.date.text = script.learningDate
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.script_list_title)
        val date: TextView = view.findViewById(R.id.script_list_date)
    }
}
