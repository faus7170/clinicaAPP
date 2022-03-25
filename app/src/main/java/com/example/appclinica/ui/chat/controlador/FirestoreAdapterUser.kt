package com.example.appclinica.ui.chat.controlador

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appclinica.R
import com.example.appclinica.ui.chat.modelo.DatosMensaje
import com.example.appclinica.ui.psicologo.GetDatosPsicologo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView

class FirestoreAdapterUser(val dataSet: MutableList<GetDatosPsicologo>, val listener: (GetDatosPsicologo) -> Unit, val isChat:Boolean) : RecyclerView.Adapter<FirestoreAdapterUser.EjercHolder>() {

    lateinit var thelastmessage: String
    lateinit var auth: FirebaseAuth

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EjercHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.designer_user_contact,parent,false)

        return EjercHolder(view)
    }

    override fun onBindViewHolder(holder: EjercHolder, position: Int) {
        holder.render(dataSet[position])
        holder.itemView.setOnClickListener { listener(dataSet[position]) }

        if (isChat){
            lastMsm(dataSet[position].id,holder.viewLastmsm)

        }

    }

    override fun getItemCount(): Int = dataSet.size

    inner class EjercHolder(view: View) : RecyclerView.ViewHolder(view) {

        var txtNombreEjerccio = view.findViewById(R.id.viewNameUserMsm) as TextView
        var txtDescripcion = view.findViewById(R.id.viewLastMsm) as TextView
        var viewLastmsm = view.findViewById(R.id.viewLastMsm) as TextView
        var imgPerfil = view.findViewById(R.id.imgCircleFragmentUser) as CircleImageView

        fun render (informacion : GetDatosPsicologo){

            Glide.with(itemView.context).load(informacion.foto).into(imgPerfil)
            txtNombreEjerccio.text = informacion.nombre
            txtDescripcion.text = informacion.descripcion
            //txtComenzarEjercicio.text = informacion.titulo
        }

    }

    fun lastMsm(userid:String, lastMsm: TextView){


        val uid = Firebase.auth.currentUser?.uid

        thelastmessage = "default"

        val database = Firebase.database
        val myRef = database.getReference("chats")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val post = postSnapshot.getValue<DatosMensaje>()
                    if (post!!.reciver.equals(uid) && post!!.sender.equals(userid) ||
                        post!!.reciver.equals(userid) && post!!.sender.equals(uid) ){
                        thelastmessage = post.msm
                    }

                }

                when(thelastmessage){
                    "default"->{
                        lastMsm.text = "No hay mensaje"
                    }else ->

                    lastMsm.text = thelastmessage

                }

                thelastmessage = "default"

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

    }



}