package ir.fanap.chattestapp.application.ui.function

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ir.fanap.chattestapp.R
import ir.fanap.chattestapp.bussines.model.Method

class ResponseAdapter(
    private val methods: MutableList<Method>
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
        return methods.size
    }

    override fun onBindViewHolder(viewHolder: ResponseAdapter.ViewHolder, position: Int) {

        if (methods[position].funcStatusList?.size!! >= 1) {

            viewHolder.textView_function.text = methods[position].funcStatusList!![position].methodName
            viewHolder.textView_item_function_state.text = methods[position].funcStatusList!![position].status
            viewHolder.txtView_error_log.text = methods[position].funcStatusList!![position].errorLog
        }
    }


}