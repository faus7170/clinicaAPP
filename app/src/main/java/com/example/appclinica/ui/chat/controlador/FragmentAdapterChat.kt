package com.example.appclinica.ui.chat.controlador

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.appclinica.ui.chat.fragment.FragmentContact
import com.example.appclinica.ui.chat.fragment.FragmentUser

class FragmentAdapterChat (fm: FragmentManager): FragmentPagerAdapter(fm) {


    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {

        when(position){
            0 -> {
                return FragmentUser()
            }
            else -> {
                return FragmentContact()
            }

        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position){
            0 -> {
                "Psicologos"
            }
            else -> {
                return "Chats"
            }

        }
    }
}