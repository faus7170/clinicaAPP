package com.example.appclinica.ui.chat.controlador

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appclinica.R
import com.example.appclinica.ui.chat.modelo.MessageBot
import com.example.appclinica.ui.chat.utils.Constants.SEND_ID
import com.google.firebase.auth.FirebaseAuth


class TestAdapterChatBot(val dataSet: MutableList<MessageBot>) : RecyclerView.Adapter<TestAdapterChatBot.EjercHolder>() {


    var type_left :Int = 0
    var type_right :Int = 1

    lateinit var auth : FirebaseAuth
    lateinit var uid :String


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EjercHolder {

        if (viewType == type_right){
            val layout = LayoutInflater.from(parent.context)
            return EjercHolder(layout.inflate(R.layout.chat_right,parent,false))
        }else{
            val layout = LayoutInflater.from(parent.context)
            return EjercHolder(layout.inflate(R.layout.chat_left,parent,false))
        }

    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: EjercHolder, position: Int) {
        holder.render(dataSet[position])
        holder.showvisto.visibility = View.GONE
    }


    override fun getItemCount(): Int = dataSet.size

    override fun getItemViewType(position: Int): Int {

        //val uid = Firebase.auth.currentUser

        if (dataSet.get(position).id.equals(SEND_ID)){
            return type_right
        }else {
            return type_left
        }


    }


    inner class EjercHolder(view: View) : RecyclerView.ViewHolder(view) {

        var showmsm = view.findViewById(R.id.showMessege) as TextView
        var txt_date = view.findViewById(R.id.viewHora) as TextView
        var showvisto = view.findViewById(R.id.txt_visto) as TextView

        fun render (informacion: MessageBot){
            showmsm.text = informacion.message
            txt_date.text = informacion.time
        }


        }

    }
