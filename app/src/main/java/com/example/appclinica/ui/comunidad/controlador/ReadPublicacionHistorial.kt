package com.example.appclinica.ui.comunidad.controlador

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.appclinica.R
import com.example.appclinica.ui.comunidad.ComentActivity
import com.example.appclinica.ui.comunidad.model.SetPregunt
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

open class ReadPublicacionHistorial: Fragment(), TestAdapterComunidad.onClickLister {

    lateinit var textPregunta: EditText
    lateinit var btnPublicar: Button
    val database = Firebase.database
    lateinit var adapter: TestAdapterComunidad
    lateinit var recyclerView: RecyclerView

    //val TOPIC = "/topics/myTopic2"

    fun readPublicaciones(fragmen:String, uid:String) {
        val mutableList: MutableList<SetPregunt> = mutableListOf()

        val myRef = database.getReference("publicacion")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mutableList.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val post = postSnapshot.getValue<SetPregunt>()

                    if (fragmen.equals("publicacion")){
                        val pregunta = post!!.pregunta
                        val uid = post.uid
                        val nombre = post.nombre
                        val foto = post.foto
                        val id = postSnapshot.key.toString()

                        mutableList.add(SetPregunt(pregunta, nombre, foto, "2021", id,uid))
                    }else if (fragmen.equals("historial")){
                        if(uid.equals(post!!.uid)){
                            val pregunta = post!!.pregunta
                            val uid = post.uid
                            val nombre = post.nombre
                            val foto = post.foto
                            val id = postSnapshot.key.toString()
                            mutableList.add(SetPregunt(pregunta, nombre, foto, "2021", id,uid))
                        }


                    }else{
                        Log.d("Error","Clase no encontrada")
                    }

                }

                mutableList.reverse()
                adapter = TestAdapterComunidad(mutableList, true, this@ReadPublicacionHistorial,
                        uid,fragmen)
                recyclerView.adapter = adapter

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }


    /*fun uidShared(): String {
        val pref = applicationContext.getSharedPreferences("dateUser", AppCompatActivity.MODE_PRIVATE)
        return pref.getString("uid", "default")!!
    }*/



    override fun onComentar(id: String, nombre: String, pregunta: String, foto: String) {
        val extras = Bundle()
        extras.putString("id", id)
        extras.putString("pregunta",pregunta)
        extras.putString("nombre", nombre)
        extras.putString("foto", foto)
        val intent = Intent(activity, ComentActivity::class.java)
        intent.putExtras(extras)
        startActivity(intent)
        Animatoo.animateSlideUp(activity)
    }

    override fun onBorrar(id: String) {

    }

    override fun onlike(txtLike: ImageView, id: String, uid: String) {
        database.getReference("like").child(id).child(id).addValueEventListener(
                object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {


                        /*if (snapshot.child(uid).exists()){
                            txtLike.setImageResource(R.drawable.ic_like)
                            txtLike.setTag("like")
                        }else{
                            txtLike.setImageResource(R.drawable.ic_liked)
                            txtLike.setTag("liked")
                        }*/
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                }
        )
    }

    fun delate(id:String,testClase: String, key: String){

        /*val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton("Si",DialogInterface.OnClickListener { dialog, id ->
                            // User clicked OK button
                        })
                setNegativeButton("No",
                        DialogInterface.OnClickListener { dialog, id ->
                            // User cancelled the dialog
                        })
            }
            // Create the AlertDialog
            builder.create()
        }*/

        if (testClase.equals("publicacion") || testClase.equals("historial")){
            database.getReference("publicacion").child(id).removeValue()
        }else{
            database.getReference("publicacion").child(key).child("comentarios").child(id).removeValue()
        }

    }

    fun editPublicacion(id: String, newValor: String, testClase: String, key: String){

        //Log.d("testEdit","id "+id+" "+testClase)
        if (testClase.equals("publicacion") || testClase.equals("historial")){
            database.getReference("publicacion").child(id).child("pregunta").setValue(newValor)
        }else{
            database.getReference("publicacion").child(key).child("comentarios").child(id).child("pregunta").setValue(newValor)
        }

    }



}