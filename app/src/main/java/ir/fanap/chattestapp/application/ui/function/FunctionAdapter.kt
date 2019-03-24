package ir.fanap.chattestapp.application.ui.function

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ir.fanap.chattestapp.R
import ir.fanap.chattestapp.bussines.model.Method

class FunctionAdapter(private val methods: MutableList<Method>, private val viewHolderListener1: ViewHolderListener) :
    RecyclerView.Adapter<FunctionAdapter.ViewHolder>() {

    private val viewHolderListener: ViewHolderListener = viewHolderListener1
    private var pos: Int? = null

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.txtViewMethod.text = methods[position].methodName
        viewHolder.textViewFuncOne.text = methods[position].funcOne
        viewHolder.textViewFuncTwo.text = methods[position].funcTwo
        viewHolder.textViewFuncThree.text = methods[position].funcThree
        viewHolder.textViewFuncFour.text = methods[position].funcFour
        viewHolder.buttonRun.tag = position

        if (!viewHolder.textViewFuncOne.text.isEmpty()) {
            viewHolder.checkBoxOne.visibility = View.VISIBLE
            viewHolder.textViewFuncOne.visibility =  View.VISIBLE
        }
        if (!viewHolder.textViewFuncTwo.text.isEmpty()) {
            viewHolder.checkBoxSec.visibility = View.VISIBLE
            viewHolder.textViewFuncTwo.visibility =  View.VISIBLE
        }

        if (!viewHolder.textViewFuncThree.text.isEmpty()) {
            viewHolder.checkBoxThird.visibility = View.VISIBLE
            viewHolder.textViewFuncThree.visibility =  View.VISIBLE
        }

        if (!viewHolder.textViewFuncFour.text.isEmpty()) {
            viewHolder.checkBoxFourth.visibility = View.VISIBLE
            viewHolder.textViewFuncFour.visibility =  View.VISIBLE
        }
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    fun tik(context: Context, viewHolder: ViewHolder) {
        viewHolder.checkBox.setColorFilter(context.resources.getColor(R.color.colorPrimary))
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getItemCount(): Int {
        return methods.size
    }

    fun getPosition(): Int? {
        return pos
    }

    fun setPos(position: Int) {
        this.pos = position
    }

    final inner class ViewHolder(itemView: View, viewHolderListener: ViewHolderListener) :
        RecyclerView.ViewHolder(itemView) {
//        override fun onClick(v: View?) {
//            val position = v?.getTag() as Int
//            setPos(position)
//        }

        val textViewFuncOne: TextView = itemView.findViewById(R.id.textView_FunOne)
        val textViewFuncTwo: TextView = itemView.findViewById(R.id.textView_FunTwo)
        val textViewFuncThree: TextView = itemView.findViewById(R.id.textView_FunThree)
        val textViewFuncFour: TextView = itemView.findViewById(R.id.textView_FunFour)
        val txtViewMethod: TextView = itemView.findViewById(R.id.textView_method)
        val checkBox: AppCompatImageView = itemView.findViewById(R.id.checkBox_test)
        val checkBoxOne: AppCompatImageView = itemView.findViewById(R.id.imageView_tickFirst)
        val checkBoxSec: AppCompatImageView = itemView.findViewById(R.id.imageView_tickSec)
        val checkBoxThird: AppCompatImageView = itemView.findViewById(R.id.imageView_tickThird)
        val checkBoxFourth: AppCompatImageView = itemView.findViewById(R.id.imageView_tickFourth)
        val buttonRun: AppCompatImageView = itemView.findViewById(R.id.buttonRun)

        init {
            buttonRun.setOnClickListener(View.OnClickListener {
                viewHolderListener.onIconClicked(this)
            })
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_method, viewGroup, false)
        return ViewHolder(v, viewHolderListener)
    }

    interface ViewHolderListener {
        fun onIconClicked(clickedViewHolder: ViewHolder)
    }
}