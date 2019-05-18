package ir.fanap.chattestapp.application.ui.function

import android.content.Context
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import ir.fanap.chattestapp.R
import ir.fanap.chattestapp.bussines.model.Method

class FunctionAdapter(
    private val context: FragmentActivity,
    private val methods: MutableList<Method>,
    private val viewHolderListener1: ViewHolderListener
) :
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
        viewHolder.buttonLog.tag = position

        if (methods[position].methodNameFlag == true) {

            context.runOnUiThread {
//                viewHolder.checkBox.setImageResource(R.drawable.ic_round_done_all_24px)
            }

//            viewHolder.checkBox.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary))
            viewHolder.buttonLog.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary))
        } else {
//            viewHolder.checkBox
//                .setImageResource(R.drawable.ic_done_black_24dp)
//            viewHolder.checkBox.setColorFilter(ContextCompat.getColor(context, R.color.grey_light))

            viewHolder.buttonLog.setColorFilter(ContextCompat.getColor(context, R.color.grey_log_color))
        }


        if (methods[position].funcOneFlag == true) {
            viewHolder.checkBoxOne
                .setImageResource(R.drawable.ic_round_done_all_24px)

            viewHolder.checkBoxOne
                .setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary))
        } else {
            viewHolder.checkBoxOne
                .setImageResource(R.drawable.ic_done_black_24dp)
        }


        if (methods[position].funcTwoFlag == true) {
            viewHolder.checkBoxSec
                .setImageResource(R.drawable.ic_round_done_all_24px)

            viewHolder.checkBoxSec
                .setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary))
        } else {
            viewHolder.checkBoxSec
                .setImageResource(R.drawable.ic_done_black_24dp)
        }

        if (methods[position].funcThreeFlag == true) {
            viewHolder.checkBoxThird
                .setImageResource(R.drawable.ic_round_done_all_24px)

            viewHolder.checkBoxThird
                .setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary))
        } else {
            viewHolder.checkBoxThird
                .setImageResource(R.drawable.ic_done_black_24dp)
        }

        if (methods[position].funcFourFlag == true) {
            viewHolder.checkBoxFourth
                .setImageResource(R.drawable.ic_round_done_all_24px)

            viewHolder.checkBoxFourth
                .setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary))
        } else {
            viewHolder.checkBoxFourth
                .setImageResource(R.drawable.ic_done_black_24dp)
        }

        if (!viewHolder.textViewFuncOne.text.isEmpty()) {
            viewHolder.checkBoxOne.visibility = View.VISIBLE
            viewHolder.textViewFuncOne.visibility = View.VISIBLE
        } else {
            viewHolder.checkBoxOne.visibility = View.GONE
            viewHolder.textViewFuncOne.visibility = View.GONE
        }
        if (!viewHolder.textViewFuncTwo.text.isEmpty()) {
            viewHolder.checkBoxSec.visibility = View.VISIBLE
            viewHolder.textViewFuncTwo.visibility = View.VISIBLE
        } else {
            viewHolder.checkBoxSec.visibility = View.GONE
            viewHolder.textViewFuncTwo.visibility = View.GONE
        }

        if (!viewHolder.textViewFuncThree.text.isEmpty()) {
            viewHolder.checkBoxThird.visibility = View.VISIBLE
            viewHolder.textViewFuncThree.visibility = View.VISIBLE
        } else {
            viewHolder.checkBoxThird.visibility = View.GONE
            viewHolder.textViewFuncThree.visibility = View.GONE
        }

        if (!viewHolder.textViewFuncFour.text.isEmpty()) {
            viewHolder.checkBoxFourth.visibility = View.VISIBLE
            viewHolder.textViewFuncFour.visibility = View.VISIBLE
        } else {
            viewHolder.checkBoxFourth.visibility = View.GONE
            viewHolder.textViewFuncFour.visibility = View.GONE
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
        val buttonLog :  AppCompatImageView = itemView.findViewById(R.id.imgView_log)
        val buttonRun: AppCompatImageView = itemView.findViewById(R.id.buttonRun)

        val progress_method = itemView.findViewById(R.id.progress_method) as ProgressBar

        init {
            buttonRun.setOnClickListener(View.OnClickListener {
                viewHolderListener.onIconClicked(this)
            })

            buttonLog.setOnClickListener {
                viewHolderListener.onLogClicked(this)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_method, viewGroup, false)
        return ViewHolder(v, viewHolderListener)
    }

    interface ViewHolderListener {
        fun onIconClicked(clickedViewHolder: ViewHolder)
        fun onLogClicked(clickedViewHolder: ViewHolder)
    }

    interface ICheckChangeListener {
        fun onItemMethodChecked(position: Int, value: Boolean)
    }
}