package mansonheart.com.realmsandbox

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.util.*

/**
 * Created by alexandr on 25.10.16.
 */

class ListAdapter(var c: Context, var lists: MutableList<String> = ArrayList()) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        var v = LayoutInflater.from(c).inflate(R.layout.list_layout, parent, false)
        return Item(v)
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        (holder as Item).bindData(lists[position])
    }

    fun add(item: String) {
        lists.add(item)
        super.notifyDataSetChanged()
    }

    fun add(items: List<String>) {
        lists.addAll(items)
        super.notifyDataSetChanged()
    }

    fun clear() {
        lists.clear()
        super.notifyDataSetChanged()
    }

    class Item(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(_list: String) {
            val textView = itemView.findViewById(R.id.textView) as TextView?
            textView?.setText(_list)
        }
    }
}
