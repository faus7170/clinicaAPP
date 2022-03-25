package com.example.appclinica.ui.chat.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appclinica.R
import com.example.appclinica.ui.chat.ChatRoomActivity
import com.example.appclinica.ui.chat.controlador.ConnectionFireStore
import com.google.firebase.messaging.FirebaseMessaging

class FragmentUser :ConnectionFireStore()  {

    //lateinit var adapterPsicologo: FirestoreAdapterUser
    //lateinit var mRecyclerView: RecyclerView
    //lateinit var userList: MutableList<GetDatosPsicologo>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)

        mRecyclerView = view!!.findViewById(R.id.recyclerViewUser)
        mRecyclerView.layoutManager = LinearLayoutManager(activity)
        cardtest = view!!.findViewById(R.id.cardChatBot)
        cardtest.setOnClickListener {
            val intent = Intent(activity, ChatRoomActivity::class.java)
            intent.putExtra("id", "chatBot")
            startActivity(intent)
        }
        search = view!!.findViewById(R.id.searchUser)
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(qString: String): Boolean {
                buscarUser(qString)
                return true
            }
            override fun onQueryTextSubmit(qString: String): Boolean {
                return false
            }
        })
        obtenerDatos()
        updateToken(FirebaseMessaging.getInstance().token)
        return view
    }
}