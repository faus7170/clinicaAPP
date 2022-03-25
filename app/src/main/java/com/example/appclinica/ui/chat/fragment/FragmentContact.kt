package com.example.appclinica.ui.chat.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appclinica.R
import com.example.appclinica.ui.chat.controlador.ConnectionFireStore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FragmentContact : ConnectionFireStore() {

    //lateinit var adapterUser: FirestoreAdapterUser
    //lateinit var mRecyclerView: RecyclerView
    //lateinit var mUser: MutableList<GetDatosUser>
    //lateinit var mUser: MutableList<GetDatosPsicologo>
    //lateinit var userList: MutableList<ChatsList>
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_contact, container, false)

        val uid = Firebase.auth.currentUser


        mRecyclerView = view!!.findViewById(R.id.recyclerViewContact)

        mRecyclerView.setHasFixedSize(true)
        val linerLinearLayoutManager = LinearLayoutManager(activity)
        //linerLinearLayoutManager.stackFromEnd = true
        mRecyclerView.layoutManager = linerLinearLayoutManager

        //adapterUser = RecyclerAdapterUser()


        obtenerContactos(uid!!.uid)


        return view
    }




}

