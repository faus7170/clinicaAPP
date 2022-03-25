package com.example.appclinica.ui.autohipnosis.controlador
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appclinica.R
import com.example.appclinica.ui.autohipnosis.modelo.GetHipnoHacking
import com.example.appclinica.ui.exercise.model.Exercise
class AdapterHipnoHacking(val dataSet: MutableList<GetHipnoHacking>, val listener: (GetHipnoHacking) -> Unit) : RecyclerView.Adapter<AdapterHipnoHacking.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.designer_autohacking,parent,false)
        return Holder(view)
    }
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.render(dataSet[position])
        holder.itemView.setOnClickListener { listener(dataSet[position]) }
    }
    override fun getItemCount(): Int = dataSet.size
    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var viewPaso : TextView
        lateinit var viewContenido : TextView
        fun render (informacion : GetHipnoHacking){
            viewPaso = itemView.findViewById(R.id.viewHhPaso)
            viewContenido = itemView.findViewById(R.id.viewHhcontenido)
            viewPaso.text = informacion.paso
            viewContenido.text = informacion.contenido
        }
    }
}