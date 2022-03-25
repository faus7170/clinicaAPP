package com.example.appclinica.ui.login.controlador

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.appclinica.ui.login.fragment.LoginFragment
import com.example.appclinica.ui.login.fragment.RegisterFragment

class PagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> {
                return LoginFragment()
            }
            else -> {
                return RegisterFragment()
            }

        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position){
            0 -> {
                "Iniciar sesión"
            }
            else -> {
                return "Registrar"
            }

        }
    }
}