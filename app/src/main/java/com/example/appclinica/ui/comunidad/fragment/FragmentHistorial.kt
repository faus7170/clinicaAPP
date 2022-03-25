package com.example.appclinica.ui.comunidad.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appclinica.R
import com.example.appclinica.ui.comunidad.controlador.ReadPublicacionHistorial

class FragmentHistorial : ReadPublicacionHistorial() {

    /*lateinit var textPregunta: EditText
    lateinit var btnPublicar: Button
    val database = Firebase.database
    lateinit var adapter: TestAdapterComunidad
    lateinit var recyclerView: RecyclerView*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_mis_preguntas, container, false)

        val pref = requireActivity().getSharedPreferences("dateUser", AppCompatActivity.MODE_PRIVATE)
        val uid = pref.getString("uid", "default")!!

        recyclerView = view.findViewById(R.id.recyclerViewHistorial)
        recyclerView.setHasFixedSize(true)
        val linerLinearLayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = linerLinearLayoutManager

        readPublicaciones("historial",uid)
        return view
    }


}