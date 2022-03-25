package com.example.appclinica.ui.autohipnosis.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import com.example.appclinica.R

class ActivityPlantilla : AppCompatActivity(), View.OnClickListener {

    lateinit var linerplantilla:LinearLayout
    //lateinit var lineraudios:LinearLayout
    lateinit var buttonf: Button
    lateinit var buttonauno: Button
    lateinit var buttonados: Button
    lateinit var buttonatres: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plantilla)

        elementby()
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.btnFinalizarHH ->{
                linerplantilla.visibility = View.GONE
                //lineraudios.visibility = View.VISIBLE
            }
        }
    }

    private fun elementby() {
        linerplantilla = findViewById(R.id.linerPlantilla)
        //lineraudios = findViewById(R.id.linerAudioPasoUno)
        buttonf = findViewById(R.id.btnFinalizarHH)
        buttonauno = findViewById(R.id.btnaudiouno_uno)
        buttonados = findViewById(R.id.btnaudiouno_dos)
        buttonatres = findViewById(R.id.btnaudiouno_tres)
    }


}