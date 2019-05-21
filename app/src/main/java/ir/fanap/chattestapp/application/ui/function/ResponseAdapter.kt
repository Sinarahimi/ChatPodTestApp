package ir.fanap.chattestapp.application.ui.function

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ir.fanap.chattestapp.R
import ir.fanap.chattestapp.bussines.model.FunctionStatus

class ResponseAdapter(
    private val subMethods: MutableList<FunctionStatus>
) :
    RecyclerView.Adapter<ResponseAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_function, viewGroup, false)
        return ViewHolder(v)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView_function: TextView = itemView.findViewById(R.id.textView_function)
        val textView_item_function_state: TextView = itemView.findViewById(R.id.textView_item_function_state)
        val txtView_error_log: TextView = itemView.findViewById(R.id.txtView_error_log)
    }

    override fun getItemCount(): Int {
        return subMethods.size
    }

    override fun onBindViewHolder(viewHolder: ResponseAdapter.ViewHolder, position: Int) {

        viewHolder.textView_function.text = subMethods[position].methodName
        viewHolder.textView_item_function_state.text = subMethods[position].status
        if (!subMethods[position].errorLog.isBlank()) {
            viewHolder.txtView_error_log.text = subMethods[position].errorLog
            viewHolder.txtView_error_log.visibility = View.VISIBLE
        }else{
            viewHolder.txtView_error_log.visibility = View.GONE
        }
    }
}