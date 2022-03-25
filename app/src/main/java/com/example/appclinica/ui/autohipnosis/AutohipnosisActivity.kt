

package com.example.appclinica.ui.autohipnosis

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appclinica.R
import com.example.appclinica.ui.autohipnosis.activity.ActivityAudio
import com.example.appclinica.ui.autohipnosis.activity.ActivityPasosHipnoHacking
import com.example.appclinica.ui.autohipnosis.activity.ActivityPlantilla
import com.example.appclinica.ui.autohipnosis.controlador.AdapterHipnoHacking
import com.example.appclinica.ui.autohipnosis.modelo.GetHipnoHacking
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class AutohipnosisActivity : AppCompatActivity(){

    lateinit var adapter: AdapterHipnoHacking
    lateinit var mRecyclerView: RecyclerView
    lateinit var userList: MutableList<GetHipnoHacking>
    val database = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_autohipnosis)

        mRecyclerView = findViewById(R.id.recyclerViewHipnoHacking)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        conexionFirestore()


    }

    private fun conexionFirestore() {
        userList = mutableListOf()

        database.collection("hipnohacking").get().addOnSuccessListener { document ->
            userList.clear()
            for (getdatos in document) {
                val paso = getdatos.getString("paso")
                val contenido = getdatos.getString("contenido")
                val key = getdatos.id
                val testDatos = GetHipnoHacking(paso!!,contenido!!,"null",key)

                userList.add(testDatos)
            }

            adapter = AdapterHipnoHacking(userList) {
                //Toast.makeText(this, "key: " + it.key, Toast.LENGTH_LONG).show()
                /*val bundle = Bundle()
                bundle.putString("key",it.key)
                bundle.putString("cont",it.contenido)*/
                val intent = Intent(this,ActivityPasosHipnoHacking::class.java)
                intent.putExtra("key",it.key)
                startActivity(intent)
            }
            mRecyclerView.adapter = adapter

        }.addOnFailureListener { exception ->

        }
    }


}