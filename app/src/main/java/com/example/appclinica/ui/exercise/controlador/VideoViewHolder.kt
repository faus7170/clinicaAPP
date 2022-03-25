package com.example.appclinica.ui.exercise.controlador

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.example.appclinica.R
import com.example.appclinica.ui.exercise.model.VideoViewModelo

class VideoViewHolder(val dataSet: MutableList<VideoViewModelo>, val listener: (VideoViewModelo) -> Unit)
    : RecyclerView.Adapter<VideoViewHolder.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.designer_item_video,parent,false)
        return Holder(view)

    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.render(dataSet[position])
        holder.itemView.setOnClickListener { listener(dataSet[position]) }

    }

    override fun getItemCount(): Int = dataSet.size

    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {

        lateinit var viewDescripcion : TextView
        lateinit var videoView : VideoView
        lateinit var progressBar: ProgressBar

        fun render (video: VideoViewModelo){

            viewDescripcion = itemView.findViewById(R.id.textView2)
            videoView = itemView.findViewById(R.id.videoView)
            progressBar = itemView.findViewById(R.id.progressBar)


            videoView.setVideoPath(video.contenido)
            viewDescripcion.text =video.nombre+"-"+video.identificador+":"+video.descripcion

            videoView.setOnPreparedListener {
                progressBar.visibility = View.GONE
                it.start()

                val videoRatio = it.videoWidth/it.videoHeight.toFloat()
                val screenRatio = videoView.width/videoView.height.toFloat()
                val scale = videoRatio/screenRatio

                if (scale>=1f){
                    videoView.scaleX = scale
                }else{
                    videoView.scaleY = 1f/scale
                }
            }
            videoView.setOnCompletionListener {
                it.start()
            }


        }

    }



}