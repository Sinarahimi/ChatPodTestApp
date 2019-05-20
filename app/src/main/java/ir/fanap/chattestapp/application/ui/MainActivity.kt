package ir.fanap.chattestapp.application.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.util.Log
import com.sendbird.android.SendBird
import ir.fanap.chattestapp.R
import kotlinx.android.synthetic.main.activity_main_bubble.*

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_bubble)

        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
//        SendBird.init()

        val titles = arrayOf("chat", "Function", "Log")
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        val pagerAdapter = PagerAdapter(supportFragmentManager, titles)

//        supportFragmentManager.beginTransaction().add(LogFragment.newInstance(), "LogFragment").commit()
        viewPager.adapter = pagerAdapter
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                top_navigation_constraint.setCurrentActiveItem(position)
            }
        })

        top_navigation_constraint.setNavigationChangeListener { view, position ->
            view_pager.setCurrentItem(position, true)
        }
        viewPager.offscreenPageLimit = 2
    }

    private fun makeRequest() {
        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.INTERNET
            , Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        ActivityCompat.requestPermissions(
            this, permissions,
            REQUEST_CODE
        )

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Log.i("Chat", "Permission has been denied by user")
                } else {
                    Log.i("Chat", "Permission has been granted by user")
                }
            }
        }
    }

    //
    override fun onBackPressed() {
//        val fragment: LogFragment = supportFragmentManager.findFragmentByTag("LogFragment") as LogFragment
//        (fragment as? IOnBackPressed)?.onBackPressed()?.not()?.let {
//            super.onBackPressed()
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }
}
