package com.hardrelice.wyyparser

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.hardrelice.wyyparser.adapter.MainAdapter
import com.hardrelice.wyyparser.ui.dashboard.DashboardFragment
import com.hardrelice.wyyparser.ui.home.HomeFragment
import com.hardrelice.wyyparser.ui.notifications.NotificationsFragment
import com.hardrelice.wyyparser.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {


    private val fragments = ArrayList<Fragment>()
    private val navMenuIds = ArrayList<Int>()

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

            } else {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        println(supportActionBar)

        ContextEverywhere.initial(this)
        handler = UIHandler(this)
        DynamicPermission.get(this,1)


        fragments.add(HomeFragment())
        fragments.add(DashboardFragment())
        fragments.add(NotificationsFragment())
        vp.adapter = MainAdapter(supportFragmentManager,fragments)
        vp.offscreenPageLimit = fragments.size

        for (item in nav_view.menu) {
            navMenuIds.add(item.itemId)
        }

        vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                nav_view.selectedItemId = nav_view.menu[position].itemId
                val frg = fragments[position]
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })

        nav_view.setOnNavigationItemSelectedListener (BottomNavigationView.OnNavigationItemSelectedListener {
            item ->
            when (item.itemId) {
                else -> {
                    vp.currentItem = navMenuIds.indexOf(item.itemId)
                    return@OnNavigationItemSelectedListener true
                }
            }
        })


//        Thread {
//            Core.scanCache()
//        }.start()

    }




}