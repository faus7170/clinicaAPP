package com.example.appclinica.ui.exercise

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.MediaController
import android.widget.VideoView
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.appclinica.R

class VideoActivity : AppCompatActivity() {

    lateinit var videoView: VideoView
    lateinit var imageButton: ImageButton
    lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        videoView = findViewById(R.id.videoView)
        imageButton = findViewById(R.id.btnVolverVideo)
        button = findViewById(R.id.btnVideoFinsh)

        imageButton.setOnClickListener {
            finish()
        }

        button.setOnClickListener{
            finish()
        }


        val bundle = intent.extras
        val dato = bundle?.getString("url")

        val uri = Uri.parse(dato.toString())

        videoView.setMediaController(
            MediaController(this)
        )

        videoView.setVideoURI(uri)
        videoView.requestFocus()
        videoView.start()
    }
}