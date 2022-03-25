package com.example.appclinica.ui.chat

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appclinica.R
import com.example.appclinica.notification.NotificationData
import com.example.appclinica.notification.PushNotification
import com.example.appclinica.notification.Token
import com.example.appclinica.ui.chat.controlador.MessageAdapter
import com.example.appclinica.ui.chat.controlador.TestAdapterChatBot
import com.example.appclinica.ui.chat.modelo.MessageBot
import com.example.appclinica.ui.chat.modelo.MessageReciver
import com.example.appclinica.ui.chat.modelo.MessageSender
import com.example.appclinica.ui.chat.utils.BotResponse
import com.example.appclinica.ui.chat.utils.Constants.OPEN_GOOGLE
import com.example.appclinica.ui.chat.utils.Constants.OPEN_SEARCH
import com.example.appclinica.ui.chat.utils.Constants.RECEIVE_ID
import com.example.appclinica.ui.chat.utils.Constants.SEND_ID
import com.example.appclinica.ui.chat.utils.Time
import com.example.appclinica.ui.psicologo.ViewPsiocologo

import com.example.appclinica.notification.RetrofitInstance

import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*


class ChatRoomActivity : ViewPsiocologo(), View.OnClickListener {

    lateinit var messageAdapter: MessageAdapter
    lateinit var btnenviar: ImageButton
    lateinit var btnVolver:ImageButton
    lateinit var txt_mensaje: EditText
    lateinit var recyclerView: RecyclerView
    lateinit var iduser:String
    lateinit var uid :String
    val database = Firebase.database
    //var notify = false
    val TAG = "MainActivity"
    val messagesList = mutableListOf<MessageBot>()
    private lateinit var adapter: TestAdapterChatBot
    lateinit var seenListener :ValueEventListener


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sala_de_chat)

        getfindViewBy()

        iduser = intent.extras!!.getString("id").toString()

        if (iduser.equals("chatBot")){
            activityPerfile(iduser, "ChatBot")
            customBotMessage("Hola ${nombreShared()} soy una inteligencia programada para ayudarte")
        }else{
            activityPerfile(iduser, "ChatRoomActivity")
            uid = uidShared()
            readMessege(uid, iduser)
            menu(iduser)
        }


        seenMessege()

    }

    //Mensaje con chatbot
    private fun sendMessage(message: String) {
        val timeStamp = Time.timeStamp()

        if (message.isNotEmpty()) {
            //Adds it to our local list
            messagesList.add(MessageBot(message, SEND_ID, timeStamp))

            //adapter.insertMessage(Message(message, SEND_ID, timeStamp))
            //rv_messages.scrollToPosition(adapter.itemCount - 1)
            adapter = TestAdapterChatBot(messagesList)
            recyclerView.adapter = adapter

            botResponse(message)
        }
    }

    private fun botResponse(message: String) {
        val timeStamp = Time.timeStamp()

        GlobalScope.launch {
            //Fake response delay
            delay(1000)

            withContext(Dispatchers.Main) {
                //Gets the response
                val response = BotResponse.basicResponses(message)


                //Adds it to our local list
                messagesList.add(MessageBot(response, RECEIVE_ID, timeStamp))

                //Inserts our message into the adapter
                //adapter.insertMessage(Message(response, RECEIVE_ID, timeStamp))
                adapter = TestAdapterChatBot(messagesList)
                recyclerView.adapter = adapter

                //Scrolls us to the position of the latest message
                //rv_messages.scrollToPosition(adapter.itemCount - 1)

                //Starts Google
                when (response) {
                    OPEN_GOOGLE -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        site.data = Uri.parse("https://www.google.com/")
                        startActivity(site)
                    }
                    OPEN_SEARCH -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        val searchTerm: String? = message.substringAfterLast("search")
                        site.data = Uri.parse("https://www.google.com/search?&q=$searchTerm")
                        startActivity(site)
                    }

                }
            }
        }
    }

    private fun customBotMessage(message: String) {

        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                val timeStamp = Time.timeStamp()
                messagesList.add(MessageBot(message, RECEIVE_ID, timeStamp))
                adapter = TestAdapterChatBot(messagesList)
                recyclerView.adapter = adapter
                //rv_messages.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    //Mensajes con persona

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btnSendMensaje -> {
                if (!txt_mensaje.text.toString().isEmpty()) {
                    if (iduser.equals("chatBot")) {
                        sendMessage(txt_mensaje.text.toString())

                    } else {
                        sendMessege(uid, iduser, txt_mensaje.text.toString(), ServerValue.TIMESTAMP)
                    }

                    txt_mensaje.setText("")
                }
            }
            R.id.btnVolverSalaChat -> {
                finish()
            }
        }

    }

    fun sendMessege(sender: String, reciver: String, msm: String, timestamp: MutableMap<String, String>){

        val myRefprueba = database.getReference("chats")

        myRefprueba.push().setValue(MessageSender(sender, reciver, msm, false, timestamp))

        contactSender(database)
        contactrReciver(database)

        val msg = msm

        senderNotification(reciver, nombreShared(), msg, timestamp )
        /*if(notify){
            senderNotification(reciver, nombreShared(),msg)
        }
        notify = false*/
    }

    fun seenMessege(){

        //val database = Firebase.database
        val myRef = database.getReference("chats")
        //reference = FirebaseDatabase.getInstance().getReference("chats")

        seenListener = myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val post = postSnapshot.getValue<MessageReciver>()
                    if (post!!.reciver.equals(uidShared()) && post.sender.equals(iduser)) {
                        val hashMap: HashMap<String, Any> = HashMap()
                        hashMap["seen"] = true
                        postSnapshot.ref.updateChildren(hashMap)
                    }

                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

    }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful) {
                //Log.d(TAG, "Response: ${Gson().toJson(response)}")
                Log.d("sendNotification", "Correcto")
            } else {
                //Log.e(TAG, response.errorBody().toString())
                Log.d("sendNotification", "Error")
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    fun senderNotification(reciver: String, nombre: String, msm: String, timestamp: MutableMap<String, String>){

        /*PushNotification(NotificationData(nombre, msm,reciver),myTokenPhone).also {
            sendNotification(it)
        }*/

        val tokens = database.getReference("tokens")

        val query:Query = tokens.orderByKey().equalTo(reciver)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (postSnapshot in snapshot.children) {
                    val post = postSnapshot.getValue<Token>()

                    Log.d("testKey", "key: " + post!!.token)

                    PushNotification(NotificationData(nombreShared(), msm, uidShared(), fotoShared()), post.token).also {
                        sendNotification(it)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }

    private fun contactSender(database: FirebaseDatabase) {
        val myRefpruebalist = database.getReference("chatslist").child(uid).child(iduser)

        myRefpruebalist.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                if (!snapshot.exists()) {
                    myRefpruebalist.child("id").setValue(iduser)
                }

            }

        })


    }

    private fun contactrReciver(database: FirebaseDatabase) {
        val myRefpruebalist = database.getReference("chatslist").child(iduser).child(uid)

        myRefpruebalist.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                if (!snapshot.exists()) {
                    myRefpruebalist.child("id").setValue(uid)
                }

            }

        })
    }

    fun readMessege(myid: String, userid: String){

        //var mutableList: MutableList<DatosMensaje>
        val mutableList: MutableList<MessageReciver>

        mutableList = mutableListOf()

        //val database = Firebase.database
        val myRef = database.getReference("chats")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mutableList.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val post = postSnapshot.getValue<MessageReciver>()

                    if (post!!.reciver.equals(myid) && post!!.sender.equals(userid) ||
                            post!!.reciver.equals(userid) && post!!.sender.equals(myid)) {

                        mutableList.add(post)
                    }

                    messageAdapter = MessageAdapter(mutableList)
                    recyclerView.adapter = messageAdapter

                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

    }



    fun currentUser(userid: String){
        val pref = applicationContext.getSharedPreferences("notification", MODE_PRIVATE)
        val edit = pref.edit()
        edit.putString("notiuser", userid)
        edit.apply()
    }

    override fun onResume() {
        super.onResume()
        currentUser(iduser)
        //Log.d("onTestResume", iduser)
    }

    override fun onPause() {
        super.onPause()
        currentUser("none")
        database.reference.child("chats").removeEventListener(seenListener)
        //reference!!.removeEventListener(seenListener);
        //Log.d("onTestPause", "none")

    }


    fun uidShared(): String {
        val pref = applicationContext.getSharedPreferences("dateUser", MODE_PRIVATE)
        return pref.getString("uid", "default")!!
    }

    fun nombreShared(): String {
        val pref = applicationContext.getSharedPreferences("dateUser", MODE_PRIVATE)
        return pref.getString("nombre", "default")!!
    }

    fun fotoShared(): String {
        val pref = applicationContext.getSharedPreferences("dateUser", MODE_PRIVATE)
        return pref.getString("foto", "default")!!
    }


    override fun onBackPressed() {
        super.onBackPressed()
        //finish()
    }

    private fun getfindViewBy() {
        txt_mensaje = findViewById(R.id.txtSendMensaje)
        btnenviar = findViewById(R.id.btnSendMensaje)
        recyclerView = findViewById(R.id.recyclerSalaChat)
        imgProfile = findViewById(R.id.imgCircleSalaChat)
        btnVolver = findViewById(R.id.btnVolverSalaChat)
        toolbar = findViewById(R.id.toolbar4)

        btnVolver.setOnClickListener(this)
        btnenviar.setOnClickListener(this)

        recyclerView.setHasFixedSize(true)
        val linerLinearLayoutManager = LinearLayoutManager(this)
        linerLinearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linerLinearLayoutManager

    }


}

