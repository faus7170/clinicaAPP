package com.example.appclinica.ui.exercise

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.*
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.appclinica.R

class AudioActivity : AppCompatActivity() {

    lateinit var txtCurrenTime: TextView
    lateinit var txtTotalTime: TextView
    lateinit var seekBar: SeekBar
    lateinit var imgPlayPause: ImageView
    lateinit var btnback: Button
    lateinit var imageButton: ImageButton
    var handler: Handler = Handler()
    lateinit var mediaPlayer: MediaPlayer
    lateinit var mRunnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multimedia)

        imageButton = findViewById(R.id.btnVolverAudio)

        val bundle = intent.extras
        val dato = bundle?.getString("url")

        getValores()

        imageButton.setOnClickListener {
            //Animatoo.animateSlideRight(this)
            finish()
        }

        btnback.setOnClickListener{
            Animatoo.animateSlideRight(this)
            finish()
        }


        val uri = Uri.parse(dato.toString())
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

    private fun getValores() {
        txtCurrenTime = findViewById(R.id.txtCurrentTime)
        txtTotalTime = findViewById(R.id.txtTotalTime)
        imgPlayPause = findViewById(R.id.imageView)
        seekBar = findViewById(R.id.playerseekBar)
        btnback = findViewById(R.id.btnPasoback)
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer.stop()
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