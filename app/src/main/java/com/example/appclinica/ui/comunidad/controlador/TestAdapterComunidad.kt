package com.example.appclinica.ui.comunidad.controlador

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appclinica.R
import com.example.appclinica.ui.comunidad.model.SetPregunt
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView


class TestAdapterComunidad(var dataSet: MutableList<SetPregunt>, var isPublicacion: Boolean,
                           var itemOnclicklister: onClickLister, var uidt: String, var testClase: String)
                            :RecyclerView.Adapter<TestAdapterComunidad.EjercHolder>() {

    //, var item: (SetPregunt)-> Unit
    //var dataSet : MutableList<SetPregunt> = mutableListOf()
    lateinit var auth: FirebaseAuth
    lateinit var uid:String
    //lateinit var itemClickListener: OnItemClickListener

    /*fun addMensaje(msm: SetPregunt){
        dataSet.add(msm)
        dataSet.reverse()
        notifyItemInserted(dataSet.size)
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EjercHolder {

        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.designer_publicacion, parent, false)
        return EjercHolder(view)

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: EjercHolder, position: Int) {
        holder.render(dataSet[position])
        //holder.txtComent.setOnClickListener { item(dataSet[position]) }
        //holder.txtLike.setOnClickListener { item(dataSet[position]) }

    }

    override fun getItemCount(): Int = dataSet.size


    inner class EjercHolder(view: View) : RecyclerView.ViewHolder(view) {

        lateinit var viewComentario :TextView
        lateinit var imagenCircle : CircleImageView
        lateinit var txtLike : ImageView
        lateinit var txtComent : ImageView
        lateinit var cantLike : TextView
        lateinit var cantComen : TextView
        lateinit var button: Button
        lateinit var buttonSi: Button
        lateinit var buttonNo: Button

        val database = Firebase.database


        lateinit var buttonCancelar: Button
        lateinit var editTextPublicacion:EditText
        lateinit var linearLayout: LinearLayout
        lateinit var linearLayoutEdit: LinearLayout
        lateinit var linearLayoutBorrar: LinearLayout
        lateinit var linearLayoutPublicacion: LinearLayout
        lateinit var toolbar: Toolbar

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun render(informacion: SetPregunt){
            
            uid = uidt

            getBy()

            getlike(informacion.id)
            getCantlike(informacion.id)

            txtLike.setOnClickListener {
                //Log.d("onlike","id "+informacion.id)
                if (txtLike.tag.equals("liked")){
                    database.getReference("likes").child(informacion.id).child(uid).setValue(true)
                }else{
                    database.getReference("likes").child(informacion.id).child(uid).removeValue()
                }
            }


            viewComentario.text = informacion.pregunta
            toolbar.setTitle(informacion.nombre)
            Glide.with(itemView.context).load(informacion.foto).into(imagenCircle)

            txtComent.setOnClickListener{itemOnclicklister.onComentar(informacion.id, informacion.nombre, informacion.pregunta, informacion.foto)}
            //txtLike.setOnClickListener { itemOnclicklister.onlike(txtLike, informacion.id,informacion.uid)}
            button.setOnClickListener {
                val readPublicacionHistorial = ReadPublicacionHistorial()
                readPublicacionHistorial.editPublicacion(informacion.id, editTextPublicacion.text.toString(),testClase,informacion.key)
                viewComentario.visibility = View.VISIBLE
                linearLayoutEdit.visibility = View.GONE
            }

            buttonCancelar.setOnClickListener {
                viewComentario.visibility = View.VISIBLE
                linearLayoutEdit.visibility = View.GONE
            }

            buttonSi.setOnClickListener {
                val readPublicacionHistorial = ReadPublicacionHistorial()
                readPublicacionHistorial.delate(informacion.id, testClase, informacion.key)
                linearLayoutBorrar.visibility = View.GONE
                linearLayoutPublicacion.visibility = View.VISIBLE
            }

            buttonNo.setOnClickListener {
                linearLayoutBorrar.visibility = View.GONE
                linearLayoutPublicacion.visibility = View.VISIBLE
            }

            if (testClase.equals("publicacion")|| testClase.equals("historial")){
                linearLayout.visibility = View.VISIBLE
            }else{
                linearLayout.visibility = View.GONE
            }

            if (isPublicacion){
                if (informacion.uid.equals(uid)) {
                    toolbar.inflateMenu(R.menu.menu_card_publicacion)
                    toolbar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.action_option1 -> {
                                viewComentario.visibility = View.GONE
                                linearLayoutEdit.visibility = View.VISIBLE
                                editTextPublicacion.setText(informacion.pregunta)

                            }
                            R.id.action_option2 -> {

                                linearLayoutBorrar.visibility = View.VISIBLE
                                linearLayoutPublicacion.visibility = View.GONE

                            }
                        }
                        true
                    })
                }
            }



        }

        private fun getlike(id: String) {
            database.getReference("likes").child(id).addValueEventListener(
                    object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.child(uid).exists()){
                                txtLike.setImageResource(R.drawable.ic_like)
                                txtLike.setTag("like")
                            }else{
                                txtLike.setImageResource(R.drawable.ic_liked)
                                txtLike.setTag("liked")
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    }
            )
        }

        private fun getCantlike(id: String) {
            database.getReference("likes").child(id).addValueEventListener(
                    object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            cantLike.setText(snapshot.childrenCount.toString())
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    }
            )
        }


        private fun getBy() {
            viewComentario = itemView.findViewById(R.id.viewComentario) as TextView
            imagenCircle = itemView.findViewById(R.id.viewCirclePersonComent) as CircleImageView
            txtComent = itemView.findViewById(R.id.viewComent)
            linearLayout = itemView.findViewById(R.id.linerLayoutLikeComentar) as LinearLayout
            linearLayoutEdit = itemView.findViewById(R.id.linerlayoutEdit) as LinearLayout
            linearLayoutBorrar = itemView.findViewById(R.id.linearLayoutBorrar) as LinearLayout
            linearLayoutPublicacion = itemView.findViewById(R.id.linearLayoutPublicacion) as LinearLayout

            toolbar = itemView.findViewById(R.id.toolbar2)
            editTextPublicacion = itemView.findViewById(R.id.editPublicacion)
            button = itemView.findViewById(R.id.btnActualizar)
            buttonSi = itemView.findViewById(R.id.btnborrarSi)
            buttonNo = itemView.findViewById(R.id.btnborrarNo)
            buttonCancelar = itemView.findViewById(R.id.btnCancelarEdit)
            txtLike = itemView.findViewById(R.id.viewLike);

            cantLike = itemView.findViewById(R.id.cantLike)
            cantComen = itemView.findViewById(R.id.cantComent)
        }

    }

    interface onClickLister{
        fun onComentar(id: String, nombre: String, pregunta: String, foto: String)
        fun onBorrar(id: String)
        fun onlike(txtLike: ImageView, id: String, uid: String)
    }


}