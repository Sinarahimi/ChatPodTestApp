package ir.fanap.chattestapp.application.ui

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import ir.fanap.chattestapp.application.ui.chat.UploadFragment
import ir.fanap.chattestapp.application.ui.function.FunctionFragment
import ir.fanap.chattestapp.application.ui.log.LogFragment

class PagerAdapter(fragmentManager: FragmentManager, private val pageTitles: Array<String>) :
    FragmentStatePagerAdapter(fragmentManager) {

    override fun getCount(): Int {
        return pageTitles.size
    }

    /*If the fragment wants to have a null value then it has
    to add a ? after the return type because ? allows to put a null value. */
    override fun getItem(position: Int): Fragment? {

        when(position){
            0 -> return FunctionFragment.newInstance()
            1 -> return LogFragment.newInstance()
            2 -> return UploadFragment.newInstance()
        }
        return null
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return pageTitles[position]
    }

}