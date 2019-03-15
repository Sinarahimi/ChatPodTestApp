package ir.fanap.chattestapp.application.ui.util

import android.content.Context
import android.graphics.PointF
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView

class SnappingLinearLayoutManager(context: Context, orientation: Int, reverseLayout: Boolean)
    : LinearLayoutManager(context,orientation,reverseLayout)
{

    override fun smoothScrollToPosition(recyclerView: RecyclerView, state: RecyclerView.State?, position: Int) {
        super.smoothScrollToPosition(recyclerView, state, position)
        val smoothScroller:RecyclerView.SmoothScroller = TopSnappedSmoothScroller(recyclerView.context)
        smoothScroller.targetPosition = position
        startSmoothScroll(smoothScroller)
    }

    private class TopSnappedSmoothScroller(context: Context) :LinearSmoothScroller(context){

//        override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
//            return this.SnappingLinearLayoutManager.
//        }

        override fun getVerticalSnapPreference(): Int {
            return LinearSmoothScroller.SNAP_TO_START
        }

    }



}