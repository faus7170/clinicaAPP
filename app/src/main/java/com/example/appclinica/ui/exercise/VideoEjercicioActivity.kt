package com.example.appclinica.ui.exercise
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.appclinica.R
import com.example.appclinica.ui.exercise.controlador.VideoViewHolder
import com.example.appclinica.ui.exercise.model.Exercise
import com.example.appclinica.ui.exercise.model.VideoViewModelo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class VideoEjercicioActivity : AppCompatActivity() {
    lateinit var viewpager: ViewPager2
    lateinit var userList:MutableList<VideoViewModelo>
    lateinit var userListPasos: MutableList<Exercise>
    lateinit var videoViewHolder: VideoViewHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_ejercicio)
        viewpager = findViewById(R.id.viewPager)
        getDataFirestore()
    }

    /**
     * Get data of dba of firestore
     */
    private fun getDataFirestore() {
        val db = Firebase.firestore
        userListPasos = mutableListOf()
        db.collection("ejercicios").get().addOnSuccessListener { document1 ->
            userListPasos.clear()
            userList = mutableListOf()
            for (getdatos1 in document1) {
                db.collection("ejercicios").document(getdatos1.id).collection("pasos").get().addOnSuccessListener { document ->
                   // userList.clear()
                    for (getdatos in document) {
                        //Get data of secondary array
                        val ident = getdatos.getString("identificador")
                        val contenido = getdatos.getString("contenido")
                        val tipo= getdatos.getString("otro")
                        val id = getdatos.id
                        //Get data of primary array excercise
                        val nombre = getdatos1.getString("nombre")
                        val descripcion = getdatos1.getString("descripcion")
                        val testDatos = VideoViewModelo(ident!!,contenido!!,tipo!!,nombre!!, descripcion!! )
                        userList.add(testDatos)
                        }
                        videoViewHolder = VideoViewHolder(userList) {
                    }
                    viewpager.setAdapter(videoViewHolder)
                  }.addOnFailureListener { exception ->
                }
            }
          }.addOnFailureListener { exception ->
        }
    }
}