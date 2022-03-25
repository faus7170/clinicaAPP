package com.example.appclinica.ui.comunidad.controlador

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.appclinica.ui.comunidad.fragment.FragmentHistorial
import com.example.appclinica.ui.comunidad.fragment.FragmentPublicacion

class PageAdapterComunidad(fm: FragmentManager): FragmentPagerAdapter(fm) {


    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {

        when(position){
            0 -> {
                return FragmentPublicacion()
            }
            else -> {
                return FragmentHistorial()
            }

        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position){
            0 -> {
                "Comunidad"
            }
            else -> {
                return "Historial"
            }

        }
    }
}