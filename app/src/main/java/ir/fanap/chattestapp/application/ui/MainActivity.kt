package ir.fanap.chattestapp.application.ui

import android.Manifest
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.util.Log
import android.widget.Toast
import ir.fanap.chattestapp.BuildConfig
import ir.fanap.chattestapp.R
import kotlinx.android.synthetic.main.activity_main_bubble.*

class MainActivity : AppCompatActivity(), TestListener  {

    private val REQUEST_CODE: Int = 1
    private var doubleBackToExitPressedOnce: Boolean = false
    private lateinit var mainViewModel: MainViewModel
    private val TOKEN = "5fb88da4c6914d07a501a76d68a62363"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_bubble)

        mainViewModel  =ViewModelProviders.of(this).get(MainViewModel::class.java)

        setupViewPager()

        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    override fun connectActivity() {
        super.connectActivity()
        mainViewModel.connect(
            BuildConfig.SOCKET_ADDRESS, BuildConfig.APP_ID, BuildConfig.SERVER_NAME
            , TOKEN, BuildConfig.SSO_HOST, BuildConfig.PLATFORM_HOST, BuildConfig.FILE_SERVER, null
        )
    }

    private fun setupViewPager() {
        val titles = arrayOf("chat", "Function", "Log")
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        val pagerAdapter = PagerAdapter(supportFragmentManager, titles)

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
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }
}
