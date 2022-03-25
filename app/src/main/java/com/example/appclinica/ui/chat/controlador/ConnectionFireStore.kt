package com.example.appclinica.ui.chat.controlador

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.appclinica.ui.chat.ChatRoomActivity
import com.example.appclinica.ui.chat.modelo.ChatsList
import com.example.appclinica.notification.Token
import com.example.appclinica.ui.psicologo.DisplayPsicoActivity
import com.example.appclinica.ui.psicologo.GetDatosPsicologo
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

open class ConnectionFireStore: Fragment() {

    lateinit var mUser : MutableList<GetDatosPsicologo>
    lateinit var mUserSearch : MutableList<GetDatosPsicologo>
    lateinit var adapter: FirestoreAdapterUser
    lateinit var mRecyclerView: RecyclerView
    val db = Firebase.firestore
    lateinit var search: SearchView
    lateinit var userList: MutableList<ChatsList>

    lateinit var cardtest: CardView


    fun obtenerDatos(){

        mUser = mutableListOf()

        db.collection("usuarios").get().addOnSuccessListener { document ->
            for (getdatos in document) {

                if (getdatos.getBoolean("ispsicologo") as Boolean){

                    val nombre = getdatos.getString("nombre")
                    val descripcion = getdatos.getString("descripcion")
                    val titulo = getdatos.getString("titulo")
                    val foto = getdatos.getString("foto")
                    val id = getdatos.id

                    val getDatos = GetDatosPsicologo(nombre!!,titulo!!,descripcion!!,foto!!,id!!)

                    mUser.add(getDatos)
                }

            }


            adapter = FirestoreAdapterUser(mUser,{
                /*val intent = Intent(activity, SalaDeChatActivity::class.java)
                intent.putExtra("id", it.id)
                startActivity(intent)*/

                option(it.id)

            },false)
            mRecyclerView.adapter = adapter

        }.addOnFailureListener { exception ->

        }
    }

    fun obtenerContactos(uid:String){

        userList = mutableListOf()

        val database = Firebase.database
        val myRef = database.getReference("chatslist").child(uid)

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()

                for (postSnapshot in snapshot.children) {
                    val post = postSnapshot.getValue<ChatsList>()
                    userList.add(post!!)
                }
                chatlist()
            }

        })


    }

    fun updateToken(token: Task<String>){

        val firebaseUser  = FirebaseAuth.getInstance().currentUser
        val reference : DatabaseReference = FirebaseDatabase.getInstance().getReference("tokens")
        val tokenl = Token(token.toString())
        reference.child(firebaseUser?.uid.toString()).setValue(tokenl)

    }

    fun chatlist(){

        //val myRef = database.getReference("user")
        mUser = mutableListOf()

        db.collection("usuarios").get().addOnSuccessListener { document ->
            mUser.clear()

            for (getdatos in document) {
                val nombre = getdatos.getString("nombre")
                val descripcion = getdatos.getString("descripcion")
                val titulo = getdatos.getString("titulo")
                val foto = getdatos.getString("foto")
                val id = getdatos.id

                for (chatList:ChatsList in userList)
                    if (id.equals(chatList.id)){
                        val getdatos = GetDatosPsicologo(nombre!!,titulo!!,descripcion!!,foto!!,id!!)
                        mUser.add(getdatos)
                    }
                adapter = FirestoreAdapterUser(mUser,{
                    val intent = Intent(activity, ChatRoomActivity::class.java)
                    intent.putExtra("id", it.id)
                    startActivity(intent)
                },true)
                mRecyclerView.adapter = adapter
            }


        }.addOnFailureListener { exception ->

        }


    }

    private fun option(id: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        //builder.setTitle("Opciones")
        //builder.setMessage("Te interesa: ")

        builder.setPositiveButton(
                "Enviar mensaje", DialogInterface.OnClickListener { dialog, which ->

            /*Pasar un valor de una fragment A a un fragment B
            val bundle = Bundle()
            bundle.putString("id",id)
            parentFragmentManager.setFragmentResult("key",bundle)*/

            val intent = Intent(activity, ChatRoomActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        })

        builder.setNegativeButton("Ver perfil", DialogInterface.OnClickListener { dialog, which ->
            val intent = Intent(activity, DisplayPsicoActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)

        })
        builder.setNeutralButton("Cancelar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun buscarUser(s: String){
        mUserSearch = mutableListOf()
        for (test: GetDatosPsicologo in mUser){
            //userListSearch.clear()
            if (test.nombre.toLowerCase().contains(s.toLowerCase())){
                val getDatos = GetDatosPsicologo(test.nombre,test.titulo,test.descripcion,test.foto,test.id)
                mUserSearch.add(getDatos)
            }
        }
         adapter = FirestoreAdapterUser(mUserSearch,{
             option(it.id)
         },false)
         mRecyclerView.adapter = adapter
    }
    fun cardChatBots(){

    }


}


