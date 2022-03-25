package com.example.appclinica.ui.psicologo

import android.content.Intent
import android.os.Build
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.appclinica.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView



open class ViewPsiocologo: AppCompatActivity(){

    lateinit var txtNombre: TextView
    lateinit var txtTitulo : TextView
    lateinit var txtDescripcion: TextView
    lateinit var txtGrupos: TextView
    lateinit var imgProfile: CircleImageView
    lateinit var toolbar: Toolbar

    val db = Firebase.firestore

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun activityPerfile(valor:String, claseNombre:String){

        val docRef = db.collection("usuarios").document(valor)
        docRef.get().addOnSuccessListener { document ->
            if (document != null) {
                if (claseNombre.equals("ChatRoomActivity")){
                    toolbar.title = document.getString("nombre")
                    Glide.with(this).load(document.getString("foto")).into(imgProfile)
                }else if (claseNombre.equals("DisplayPsicoActivity")){
                    txtNombre.text = document.getString("nombre")
                    txtTitulo.text = document.getString("titulo")
                    txtDescripcion.text = document.getString("descripcion")
                    Glide.with(this).load(document.getString("foto")).into(imgProfile)
                }else{
                    //Toast.makeText(applicationContext,"Clase no encontrada "+claseNombre, Toast.LENGTH_LONG).show()
                    toolbar.title = "Chatbot"
                    Glide.with(this).load("https://i.pinimg.com/originals/fd/a1/3b/fda13b9d6d88f25a9d968901d319216a.jpg").into(imgProfile)
                }

            } else {
                Toast.makeText(applicationContext,"Error al cargar", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener { exception ->

        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun menu(iduser:String){
        toolbar.inflateMenu(R.menu.menu_chat_room)
        toolbar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_option_verperfil -> {
                    val intent = Intent(this,DisplayPsicoActivity::class.java)
                    intent.putExtra("id",iduser)
                    startActivity(intent)
                }
                R.id.action_option2 -> {
                    //Log.d("Test_menu", "option1" + informacion.id)

                }
            }
            true
        })
    }


}