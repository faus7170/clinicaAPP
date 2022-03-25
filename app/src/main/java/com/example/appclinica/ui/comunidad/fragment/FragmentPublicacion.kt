package com.example.appclinica.ui.comunidad.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appclinica.R
import com.example.appclinica.ui.comunidad.controlador.ReadPublicacionHistorial
import com.example.appclinica.ui.comunidad.model.SetPregunt
//import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FragmentPublicacion : ReadPublicacionHistorial() {

    /*lateinit var textPregunta: EditText
    lateinit var btnPublicar: Button
    val database = Firebase.database
    lateinit var adapter: TestAdapterComunidad
    lateinit var recyclerView: RecyclerView*/
    //tools:context=".ui.comunidad.ComunidadActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_publicacion, container, false)

        val pref = requireActivity().getSharedPreferences("dateUser", AppCompatActivity.MODE_PRIVATE)
        val uid = pref.getString("uid", "default")!!
        val nombre = pref.getString("nombre", "default")!!
        val foto = pref.getString("foto", "default")!!

        findByid(view)

        //readPublicacion()

        btnPublicar.setOnClickListener {
            if(!textPregunta.text.toString().isEmpty()){
                sendPublicacion(uid, textPregunta.text.toString(),nombre,foto)
                textPregunta.setText("")
            }

        }

        readPublicaciones("publicacion",uid)

        return view

    }

    fun sendPublicacion(uid: String, question: String,nombre: String,foto: String){

        val myRefprueba = database.getReference("publicacion")

        val hashMap: HashMap<String, String> = hashMapOf()
        hashMap.put("uid", uid)
        hashMap.put("pregunta", question)
        hashMap.put("nombre", nombre)
        hashMap.put("foto", foto)

        //myRefprueba.push().setValue(hashMap)
        myRefprueba.push().setValue(SetPregunt(question,nombre,foto,"2021","",uid))
        //SetPregunt(pregunta, nombre, foto, "2021", id,uid)


    }

    private fun findByid(view: View) {
        textPregunta = view.findViewById(R.id.txtPregunta)
        btnPublicar = view.findViewById(R.id.btnPublicarPregunta)
        recyclerView = view.findViewById(R.id.recyclerViewPreguntas)
        recyclerView.setHasFixedSize(true)
        val linerLinearLayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = linerLinearLayoutManager

    }

}