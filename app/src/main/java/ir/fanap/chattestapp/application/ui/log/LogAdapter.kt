package ir.fanap.chattestapp.application.ui.log

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ir.fanap.chattestapp.R

class LogAdapter(val logs: MutableList<String>) : RecyclerView.Adapter<LogAdapter.ViewHolder>() {

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textViewLog.text = logs.get(position)
        if (position % 2 == 1) {
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))
        } else {
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#FFFAF8FD"))
        }
    }

    override fun getItemCount(): Int {
        return logs.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_log, viewGroup, false)
        return ViewHolder(view)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewLog: TextView = itemView.findViewById(R.id.textView_log)
    }


    fun additem() {

    }
}