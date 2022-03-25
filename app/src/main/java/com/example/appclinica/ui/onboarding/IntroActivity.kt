package com.example.appclinica.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.appclinica.R
import com.example.appclinica.ui.login.LoginActivity
import com.google.android.material.tabs.TabLayout


/**
 *@author David Aguinsaca
 *El usuario descarga la aplicacion por primera vez muestra
 *travez de un ViewPager y Fragment los servicios que ofrece la aplicacion
 *
 **/

class IntroActivity : AppCompatActivity() {

    lateinit var screenPager: ViewPager
    lateinit var introViewPagerAdapter: IntroViewPagerAdapter
    lateinit var tabIndicator: TabLayout
    lateinit var btnNext: Button
    var position = 0
    lateinit var btnGetStarted: Button
    lateinit var btnAnim: Animation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (restorePrefData()) {
            val mainActivity = Intent(this, LoginActivity::class.java)
            startActivity(mainActivity)
            finish()
        }
        setContentView(R.layout.activity_intro)
        btnNext = findViewById(R.id.btn_next)
        btnGetStarted = findViewById(R.id.btn_get_started)
        tabIndicator = findViewById(R.id.tab_indicator)
        btnAnim = AnimationUtils.loadAnimation(applicationContext, R.anim.button_animation)
        // fill list screen
        val mList: MutableList<ScreenItem> = ArrayList()
        mList.add(ScreenItem("Chat", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua, consectetur  consectetur adipiscing elit", R.drawable.img_chat))
        mList.add(ScreenItem("Yoga", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua, consectetur  consectetur adipiscing elit", R.drawable.img_yoga))
        mList.add(ScreenItem("Inteligencia emocional", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua, consectetur  consectetur adipiscing elit", R.drawable.img_emocional))
        screenPager = findViewById(R.id.screen_viewpager)
        introViewPagerAdapter = IntroViewPagerAdapter(this, mList)
        screenPager.setAdapter(introViewPagerAdapter)
        tabIndicator.setupWithViewPager(screenPager)
        btnNext.setOnClickListener(View.OnClickListener {
            position = screenPager.getCurrentItem()
            if (position < mList.size) {
                position++
                screenPager.setCurrentItem(position)
            }
            if (position == mList.size - 1) {
                loaddLastScreen()
            }
        })
        tabIndicator.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.getPosition() == (mList.size) - 1) {
                    loaddLastScreen()
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {
            }
            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
        btnGetStarted.setOnClickListener(View.OnClickListener {
            val mainActivity = Intent(this, LoginActivity::class.java)
            startActivity(mainActivity)
            savePrefsData()
            finish()
        })
    }
    private fun restorePrefData(): Boolean {
        val pref = applicationContext.getSharedPreferences("myPrefs", MODE_PRIVATE)
        return pref.getBoolean("isIntroOpnend", false)
    }
    private fun savePrefsData() {
        val pref = applicationContext.getSharedPreferences("myPrefs", MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean("isIntroOpnend", true)
        editor.apply()
    }

    private fun loaddLastScreen() {
        btnNext.visibility = View.INVISIBLE
        btnGetStarted.visibility = View.VISIBLE
        tabIndicator.visibility = View.INVISIBLE
        btnGetStarted.animation = btnAnim
    }
}