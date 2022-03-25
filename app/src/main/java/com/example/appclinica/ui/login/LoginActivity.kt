package com.example.appclinica.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.appclinica.HomeActivity
import com.example.appclinica.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.*
//import com.google.firebase.database.DatabaseReference
/**
 *@author David Aguinsaca
 *@author Modificado 22/01/2022 FL
 * Autenticacion de la aplicacion, se elimina tabs y se modifica template
 **/

class LoginActivity :  AppCompatActivity()  {
    //private lateinit var database: DatabaseReference
    lateinit var btnLogin: Button
    lateinit var btnRegister: Button
    lateinit var btnLoginFacebook: ImageButton
    lateinit var btnLoginGoogle: ImageButton
    lateinit var txtEmail : EditText
    lateinit var txtPassword : EditText
   /* lateinit var btnIngresarGoogle: ImageButton
    lateinit var btnIngresarFacebook: ImageButton*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        elementsById()
        title="Autenticación"
        setup()
    }

    //Verificar si ya hay una cuenta que haya iniciado sesion previamente
   override fun onStart() {
        super.onStart()
        val currentUser = FirebaseAuth.getInstance().currentUser
        updateUI(currentUser)
   }
   private fun elementsById() {
       btnLogin = findViewById(R.id.btnLogin)
       btnRegister = findViewById(R.id.btnRegister)
       //btnLoginFacebook = findViewById(R.id.btnLoginFacebook)
       //btnLoginGoogle = findViewById(R.id.btnLoginGoogle)
       txtEmail =  findViewById(R.id.txtEmail)
       txtPassword = findViewById(R.id.txtPassword)
   }
    private fun setup (){
        title="Login"
       btnLogin.setOnClickListener{
        if(txtEmail.text.isNotEmpty() && txtPassword.text.isNotEmpty()){
            FirebaseAuth.getInstance().
            signInWithEmailAndPassword(txtEmail.text.toString(),
                txtPassword.text.toString()).addOnCompleteListener{ task ->
                    if(task.isSuccessful){
                        val user =FirebaseAuth.getInstance().currentUser
                        updateUI(user)
                    }else{
                        showAlert("No se pudo autenticar el usuario, revise sus credenciales.")
                    }
            }
        }else{
            showAlert("Llena todos los campos.")
        }
       }

      btnRegister.setOnClickListener{
          println("Entro al boton")
            updateUIReg()
        }
    }
    private fun updateUI(currentUser: FirebaseUser?) { //send current user to next activity
        if (currentUser == null) return
        val intent = Intent(this, HomeActivity::class.java).apply{
        }
        startActivity(intent)
       // savePrefsData()
       // requireActivity().finish()
    }
    private fun updateUIReg() { //send current user to next activity
        val intent = Intent(this, RegisterActivity::class.java).apply{
        }
        startActivity(intent)
        // savePrefsData()
        // requireActivity().finish()
    }
    private fun showAlert(mensaje : String){
        val builder=AlertDialog.Builder(this)
        builder.setTitle("!Ups!")
        //builder.
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar",null)
        val dialog:AlertDialog=builder.create()
        dialog.show()
    }
}
