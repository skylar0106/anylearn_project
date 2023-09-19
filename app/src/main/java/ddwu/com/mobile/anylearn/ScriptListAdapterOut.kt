package ddwu.com.mobile.anylearn

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ddwu.com.mobile.anylearn.databinding.ActivityMyScriptListBinding
import ddwu.com.mobile.anylearn.databinding.ActivityScriptListAdapterOutBinding
import org.w3c.dom.Text

class ScriptListAdapterOut (val context: Context, val scriptList: MutableList<MyScriptList.outModel>): RecyclerView.Adapter<ScriptListAdapterOut.MyViewHolder>() {

    override fun getItemCount(): Int = scriptList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding = ActivityScriptListAdapterOutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(context, itemBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val scriptItem = scriptList[position]

//        holder.itemBinding.monthText.text = scriptItem.month
        holder.bind(scriptItem)
//        holder.itemBinding.scriptListOutRecyclerview.adapter = ScriptsListAdapter(context, scriptItem.ListItem)

    }

    class MyViewHolder(val context: Context, val itemBinding: ActivityScriptListAdapterOutBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: MyScriptList.outModel) {
//            itemBinding.model = item

            val adapter = ScriptsListAdapter(context, item.ListItem)

            itemBinding.scriptListOutRecyclerview.adapter = adapter
            itemBinding.scriptListOutRecyclerview.layoutManager = LinearLayoutManager(context)
            itemBinding.monthText.text = item.month
        }
//        val outmodel : RecyclerView = item


    }
}
