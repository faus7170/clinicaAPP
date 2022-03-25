package com.example.appclinica.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.icu.text.CaseMap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.appclinica.R
import com.example.appclinica.ui.chat.ChatRoomActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat
import java.util.*

private const val CHANNEL_ID = "my_channel"

class FirebaseService() : FirebaseMessagingService() {

    companion object {
        var sharedPref: SharedPreferences? = null

        var token: String?
            get() {
                return sharedPref?.getString("token", "")
            }
            set(value) {
                sharedPref?.edit()?.putString("token", value)?.apply()
            }

    }


    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        token = newToken

//        updateToken(token.toString())
    }


    /*fun updateToken(refresToken: String){
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val reference : DatabaseReference = FirebaseDatabase.getInstance().getReference("tokens")
        val token = Token(refresToken)
        reference.child(firebaseUser.uid).setValue(token)

    }*/

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val user: String = message.data.get("iduser")!!

        if (!notificacionShared().equals(user)){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                noticicacionOreo(message)
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun noticicacionOreo(message: RemoteMessage){
        val user: String = message.data.get("iduser")!!
        val idNotificacion = user.replace("[^\\d]".toRegex(), "").toInt()


        val notificationLayout = RemoteViews(packageName, R.layout.notification_msm)
        notificationLayout.setTextViewText(R.id.notificacionName,message.data["title"])
        notificationLayout.setTextViewText(R.id.notificacionMsm,message.data["message"])

        val bitmap :Bitmap = Picasso.with(applicationContext).load(message.data["imagen"]).get()
        notificationLayout.setImageViewBitmap(R.id.notificacionImagen,bitmap)

        val horaActuual = SimpleDateFormat("HH:mm")
        val hora = horaActuual.format(Date())
        notificationLayout.setTextViewText(R.id.notificacionHora,hora)

        val intent = Intent(this, ChatRoomActivity::class.java)
        val bundle = Bundle()
        bundle.putString("id",user)
        intent.putExtras(bundle)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        //val notificationID = Random.nextInt()
        val notificationID = idNotificacion

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                //.setContentTitle(message.data["title"])
                //.setContentText(message.data["message"])
                .setSmallIcon(R.drawable.ic_icono_clinica_hipnosis)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setCustomContentView(notificationLayout)
                .setContent(notificationLayout)
                .build()


  //      Picasso.get().load(message.data["imagen"])
//                .into(notificationLayout, R.id.notificacionImagen,notificationID,notification)

        notificationManager.notify(notificationID, notification)

        createNotificationChannel(notificationManager)

    }

    fun notificacionShared(): String {
        val pref = applicationContext.getSharedPreferences("notification", MODE_PRIVATE)
        return pref.getString("notiuser", "none")!!
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channelName = "Mensajes"
        val channel = NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
            description = "Mensajes"
            enableLights(true)
            lightColor = Color.GREEN
        }
        notificationManager.createNotificationChannel(channel)
    }

}
