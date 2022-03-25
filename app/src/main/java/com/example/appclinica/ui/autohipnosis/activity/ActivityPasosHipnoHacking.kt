package com.example.appclinica.ui.autohipnosis.activity

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appclinica.R
import com.example.appclinica.ui.autohipnosis.controlador.AdapterHipnoHacking
import com.example.appclinica.ui.autohipnosis.modelo.GetHipnoHacking
import com.example.appclinica.ui.exercise.AudioActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ActivityPasosHipnoHacking : AppCompatActivity() {

    lateinit var adapter: AdapterHipnoHacking
    lateinit var userList: MutableList<GetHipnoHacking>
    lateinit var mRecyclerView: RecyclerView
    lateinit var linerlayout: LinearLayout
    val database = Firebase.firestore
    lateinit var button: Button

    var handler: Handler = Handler()
    lateinit var mediaPlayer: MediaPlayer
    lateinit var mRunnable: Runnable
    lateinit var txtCurrenTime: TextView
    lateinit var txtTotalTime: TextView
    lateinit var seekBar: SeekBar
    lateinit var imgPlayPause: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pasos_hipno_hacking)
        val bundle = intent.extras
        mRecyclerView = findViewById(R.id.recyclerViewPasosHipnoHacking)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        linerlayout = findViewById(R.id.linerAudioHH)
        txtCurrenTime = findViewById(R.id.txtCurrentTimeHH)
        txtTotalTime = findViewById(R.id.txtTotalTimeHH)
        seekBar = findViewById(R.id.playerseekBarHH)
        imgPlayPause = findViewById(R.id.imageViewHH)
        button = findViewById(R.id.button3)

        button.setOnClickListener {
            mRecyclerView.visibility = View.VISIBLE
            linerlayout.visibility = View.GONE
            mediaPlayer.stop()
        }
        val key = bundle!!.getString("key")
        conexionFirestore(key.toString())
    }

    private fun conexionFirestore(keyFB:String) {
        userList = mutableListOf()
        database.collection("hipnohacking").document(keyFB).collection("pasos").get().addOnSuccessListener { document ->
            userList.clear()
            for (getdatos in document) {
                val paso = getdatos.getString("paso")
                val contenido = getdatos.getString("contenido")
                val audio = getdatos.getString("audio")
                val key = getdatos.id
                val testDatos = GetHipnoHacking(paso!!,contenido!!,audio!!,key)

                userList.add(testDatos)
            }
            adapter = AdapterHipnoHacking(userList) {
                val intent = Intent(this, AudioActivity::class.java)
                intent.putExtra("url", it.audio)
                startActivity(intent)
            }
            mRecyclerView.adapter = adapter

        }.addOnFailureListener { exception ->

        }
    }

    private fun reproductor(audio: String) {
        linerlayout.visibility = View.VISIBLE
        val uri = Uri.parse(audio)
        mediaPlayer = MediaPlayer.create(this,uri)
        mRunnable = object : Runnable {
            override fun run() {
                seekBar.setProgress(mediaPlayer.currentPosition)
                handler.postDelayed(this,500)
            }
        }
        val duration = mediaPlayer.duration
        val sDuration = miliSecondToTimer(duration)
        txtTotalTime.text = sDuration
        imgPlayPause.setOnClickListener {
            if (mediaPlayer.isPlaying){
                mediaPlayer.pause()
                handler.removeCallbacks(mRunnable)
                imgPlayPause.setImageResource(R.drawable.ic_play_circle_outline)
            }else{
                mediaPlayer.start()
                seekBar.max = mediaPlayer.duration
                handler.postDelayed(mRunnable,0)
                imgPlayPause.setImageResource(R.drawable.ic_pause_circle)
            }
        }
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser){
                    mediaPlayer.seekTo(progress)
                }
                txtCurrenTime.text = miliSecondToTimer(mediaPlayer.currentPosition)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })
        mediaPlayer.setOnCompletionListener{
            imgPlayPause.setImageResource(R.drawable.ic_play_circle_outline)
            mediaPlayer.seekTo(0)
        }
    }

    fun miliSecondToTimer(milesecond: Int):String{

        var timeString = ""
        var secondString = ""

        val hora = milesecond/(1000*60*60) as Int
        val minute = (milesecond%(1000*60*60))/(1000*60) as Int
        val second = (milesecond%(1000*60*60))%(1000*60)/1000 as Int

        if (hora>0){
            timeString = hora as String + ":"
        }
        if (second<10){
            secondString = "0"+ second
        }else{
            secondString = ""+ second
        }

        timeString = timeString + minute + ":" + secondString
        return timeString
    }
}