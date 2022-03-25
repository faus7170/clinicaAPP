package com.example.appclinica.ui.autohipnosis.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.appclinica.R
import com.example.appclinica.ui.exercise.AudioActivity

class ActivityAudio : AppCompatActivity(),View.OnClickListener {

    lateinit var linerPasoUno : LinearLayout
    lateinit var linerPasoDos : LinearLayout
    lateinit var linerPasoTres : LinearLayout
    lateinit var linerPasoCuatro : LinearLayout
    lateinit var linerPasoCinco : LinearLayout
    lateinit var textView: TextView
    lateinit var buttonuno_uno: Button
    lateinit var buttonuno_dos: Button
    lateinit var buttonuno_tres: Button
    val audio_url = "https://firebasestorage.googleapis.com/v0/b/clinicaapp-2a450.appspot.com/o/ejercicios%2Faudios%2Fnaturaleza.mp3?alt=media&token=c4104f37-8e40-4910-8f77-5fae8767ac25"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio)

        elementBy()

        val bundle = intent.extras
        val key = bundle!!.getString("key")
        val cont = bundle!!.getString("cont")
        textView.setText(cont)
        selecction(key.toString())

    }

    override fun onClick(p0: View?) {
        when (p0!!.id){
            R.id.btnaudiouno_uno->{
                intent(audio_url)
            }
            R.id.btnaudiouno_dos->{
                intent(audio_url)
            }
            R.id.btnaudiouno_tres->{
                intent(audio_url)
            }
        }
    }

    private fun intent(url:String){
        val intent = Intent(this,AudioActivity::class.java)
        intent.putExtra("url",url)
        startActivity(intent)

    }

    private fun selecction(paso:String){

        when(paso){
            "1"->{
                linerPasoUno.visibility = View.VISIBLE
                linerPasoDos.visibility = View.GONE
                linerPasoTres.visibility = View.GONE
                linerPasoCuatro.visibility = View.GONE
                linerPasoCinco.visibility = View.GONE
            }
            "2"->{
                linerPasoUno.visibility = View.GONE
                linerPasoDos.visibility = View.VISIBLE
            }
        }

    }



    private fun elementBy() {
        linerPasoUno = findViewById(R.id.linerAudioPasoUno)
        linerPasoDos = findViewById(R.id.linerAudioPasoDos)
        linerPasoTres = findViewById(R.id.linerAudioPasoTres)
        linerPasoCuatro = findViewById(R.id.linerAudioPasoCuatro)
        linerPasoCinco = findViewById(R.id.linerAudioPasoCinco)
        textView = findViewById(R.id.textView9)
        buttonuno_uno = findViewById(R.id.btnaudiouno_uno)
        buttonuno_dos = findViewById(R.id.btnaudiouno_dos)
        buttonuno_tres = findViewById(R.id.btnaudiouno_tres)
        buttonuno_uno.setOnClickListener(this)
        buttonuno_dos.setOnClickListener(this)
        buttonuno_tres.setOnClickListener(this)

    }


}