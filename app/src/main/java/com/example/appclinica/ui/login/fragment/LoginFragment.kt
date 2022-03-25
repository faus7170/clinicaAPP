package com.example.appclinica.ui.login.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.appclinica.R
import com.example.appclinica.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 *@author David Aguinsaca
 * Fragmento de autenticacion de usuario con un correo y clave
 **/

class LoginFragment : Fragment(), View.OnClickListener{

    lateinit var auth: FirebaseAuth
    lateinit var btnLoginEmail: Button
    lateinit var editEmail : EditText
    lateinit var editPassword : EditText
    lateinit var recoverPassword : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        elementsById(view)
        return view
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btnSignUp ->{
                loginUser(editEmail.text.toString(),editPassword.text.toString())
            }
            R.id.textRecoverPassword ->{
                showDialogResetPassword()
            }
        }
    }


    private fun elementsById(view: View) {
        editEmail = view.findViewById(R.id.editEmail)
        editPassword = view.findViewById(R.id.editPass)
        recoverPassword = view.findViewById(R.id.textRecoverPassword)
        btnLoginEmail = view.findViewById(R.id.btnSignUp)

        btnLoginEmail.setOnClickListener(this)
        recoverPassword.setOnClickListener(this)
    }

    //Verificar los campos de correo y password

    private fun checkCredentials(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(activity, "Llenar los campos", Toast.LENGTH_LONG).show()
            return false
        }else if (!email.contains("@") || email.length < 6) {
            Toast.makeText(activity, "Verificar que el correo contenga @", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }
    //Conexion con firebase para validar si la cuenta existe en caso de que si verifica que el correo y la clave concidadan
    private fun loginUser(email: String, password: String) {
        if (checkCredentials(email, password)) {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = auth.currentUser
                            updateUI(user)
                            /*if (!user.isEmailVerified) {
                                Toast.makeText(activity, "En la espera de confirmacion del correo", Toast.LENGTH_SHORT).show()
                            } else {
                                updateUI(user)
                            }*/
                        } else {
                            Toast.makeText(activity, "Error al cargar", Toast.LENGTH_SHORT).show()
                        }
                    }

        }

    }

    //Cargar el siguiente activity

    private fun updateUI(currentUser: FirebaseUser?) { //send current user to next activity
        if (currentUser == null) return
        val intent = Intent(activity, HomeActivity::class.java)
        startActivity(intent)
        //savePrefsData()
        requireActivity().finish()
    }

    private fun showDialogResetPassword() {

        var edit_correoReset: EditText
        val btn_resetPassword: Button

        val dlg = AlertDialog.Builder(requireActivity())
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.dialog_resetpassword, null)
        dlg.setView(view)

        val alertDialog = dlg.create()
        alertDialog.show()

        btn_resetPassword = view.findViewById(R.id.btnSendRecover)
        edit_correoReset = view.findViewById(R.id.editRecoverEmail)


        btn_resetPassword.setOnClickListener {

            if(!edit_correoReset.text.isEmpty()){
                resetPasword(edit_correoReset.text.toString())
            }else{
                Toast.makeText(activity, "Llenar el campo", Toast.LENGTH_SHORT).show()

            }
        }
    }


    private fun resetPasword(emailAddress:String){
        auth.sendPasswordResetEmail(emailAddress).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(activity, "Mensaje enviado", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(activity, "Correo no registrado", Toast.LENGTH_SHORT).show()
            }
        }
    }

}