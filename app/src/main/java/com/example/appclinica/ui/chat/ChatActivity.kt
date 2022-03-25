package com.example.appclinica.ui.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.viewpager.widget.ViewPager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.appclinica.HomeActivity
import com.example.appclinica.R
import com.example.appclinica.ui.chat.controlador.FragmentAdapterChat
import com.google.android.material.tabs.TabLayout

/**
 *@author David Aguinsaca
 *Activity principal de de chat contiene fragmentos donde muestra a los psicologos y el historial de chat
 *
 **/

class ChatActivity : AppCompatActivity() {

    lateinit var tabLayout : TabLayout
    lateinit var viewPager : ViewPager
    lateinit var btnVolver: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        tabLayout = findViewById(R.id.idTabLayoutChat)
        viewPager = findViewById(R.id.idViewPagerChat)
        btnVolver = findViewById(R.id.btnVolverChat)

        btnVolver.setOnClickListener {
            finish()
        }

        val pageAdapterChat = FragmentAdapterChat(supportFragmentManager)
        viewPager.adapter = pageAdapterChat
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


}