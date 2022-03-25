package com.example.appclinica

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.appclinica.ui.autohipnosis.AutohipnosisActivity
import com.example.appclinica.ui.chat.ChatActivity
import com.example.appclinica.ui.comunidad.ComunidadActivity
import com.example.appclinica.ui.configuracion.ConfiguracionActivity
import com.example.appclinica.ui.exercise.ExerciseActivity
import com.example.appclinica.ui.exercise.VideoEjercicioActivity
import com.example.appclinica.ui.login.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 *@author David Aguinsaca
 * Menu principal de la aplicaccion con los servicios ofreccidos:
 * Ejerccios, Autohipnosis, Chat, Configuracción y Comunidad
 **/


class HomeActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var cardViewEjercicio: CardView
    lateinit var cardVieweAutohipnosis: CardView
    lateinit var cardVieweChat: CardView
    lateinit var btnTestNotification: Button

    lateinit var menConfiguracion :BottomNavigationItemView
    lateinit var menPrincial :BottomNavigationItemView
    lateinit var menSalir :BottomNavigationItemView


    //lateinit var btnnavegation: BottomNavigationView
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        elementsById()
        getDatosUser()
        
    }
    //Obtener datos del usuario en la base de datos y guardar en la clase SharedPreferences
    private fun getDatosUser() {
        val db = Firebase.firestore
        val user = FirebaseAuth.getInstance().currentUser
        val pref = applicationContext.getSharedPreferences("dateUser", MODE_PRIVATE)
        val editor = pref.edit()
        db.collection("usuarios").document(user?.uid.toString()).get()
                .addOnSuccessListener { getdatos ->
                    val nombre = getdatos.getString("nombre")
                    val descripcion = getdatos.getString("descripcion")
                    val titulo = getdatos.getString("titulo")
                    val foto = getdatos.getString("foto")
                    editor.putString("uid", user?.uid)
                    editor.putString("nombre", nombre)
                    editor.putString("descripcion", descripcion)
                    editor.putString("titulo", titulo)
                    editor.putString("foto", foto)
                    editor.apply()

                }.addOnFailureListener { exception ->
                val e = exception.message.toString()
                println("========== Error: $e")
        }
    }

    //Accion para acceder a cada servicio con la clase onClick
    override fun onClick(v: View?) {
        println("Ingreso a la vista onclick")
        when(v!!.id){
            R.id.bankcardExercice -> {
                val intent = Intent(this, VideoEjercicioActivity::class.java)
                startActivity(intent)
                Animatoo.animateSlideLeft(this)
            }
            R.id.bankcardAutohipnosis -> {
                val intent = Intent(this, AutohipnosisActivity::class.java)
                startActivity(intent)
                Animatoo.animateSlideLeft(this)

            }
            R.id.bankcardChat -> {
                val intent = Intent(this, ChatActivity::class.java)
                startActivity(intent)
                Animatoo.animateSlideLeft(this)
            }
            /*R.id.bankcardConfiguracion-> {
                val intent = Intent(this, ConfiguracionActivity::class.java)
                startActivity(intent)
                //Toast.makeText(applicationContext,"En proceso ...",Toast.LENGTH_LONG).show()
                finish()

            }*/
            R.id.bankcardForo -> {
                val intent = Intent(this, ComunidadActivity::class.java)
                startActivity(intent)
                Animatoo.animateSlideLeft(this)
            }
            /*R.id.bankcardSignUp -> {
                Firebase.auth.signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                Animatoo.animateSlideRight(this)
                finish()
            }*/
           /* R.id.testNotification ->{
                val intent = Intent(this, ConfiguracionActivity::class.java)
                startActivity(intent)
                //Toast.makeText(applicationContext,"En proceso ...",Toast.LENGTH_LONG).show()
                finish()
            }*/
            R.id.menConfiguracion ->{
                println("Menu configuracion")
                val intent = Intent(this, ConfiguracionActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.menPrincial ->{
                println("Menu principal")
                val intent = Intent(this, TestNotificationActivity::class.java)
                startActivity(intent)
            }
            R.id.menSalir ->{
                println("Menu Salir")
                Firebase.auth.signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                Animatoo.animateSlideRight(this)
                finish()
            }
        }
    }
/*private fun BottomN()
{
    btnnavegation.setOnClickListener{

    }
}*/

    private fun elementsById() {
        cardViewEjercicio = findViewById(R.id.bankcardExercice)
        cardVieweAutohipnosis = findViewById(R.id.bankcardAutohipnosis)
        cardVieweChat = findViewById(R.id.bankcardChat)
       // btnTestNotification = findViewById(R.id.testNotification)

        menConfiguracion = findViewById(R.id.menConfiguracion)
        menPrincial= findViewById(R.id.menPrincial)
        menSalir= findViewById(R.id.menSalir)

        cardViewEjercicio.setOnClickListener(this)
        cardVieweAutohipnosis.setOnClickListener(this)
        cardVieweChat.setOnClickListener(this)

        menConfiguracion.setOnClickListener(this)
        menPrincial.setOnClickListener(this)
        menSalir.setOnClickListener(this)

    }


}