package com.example.appclinica.ui.login.controlador

import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.appclinica.HomeActivity
import com.example.appclinica.R
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider


open class Authentication : AppCompatActivity() {

    lateinit var mGoogleSignInClient: GoogleSignInClient
    var callbackManager = CallbackManager.Factory.create()
    val RC_SIGN_IN = 123
    lateinit var auth: FirebaseAuth
    lateinit var edit_correoReset: EditText
    lateinit var btn_resetPassword: Button

    //Registrar con facebook
    fun loginWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {

            }

            override fun onError(error: FacebookException) {

            }
        })
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information

                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.

                        Toast.makeText(
                                baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT
                        ).show()

                    }

                    // ...
                }
    }

    //Registrar como anonimo

    fun loginAnonimo() {
        auth.signInAnonymously()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser
                        updateUI(user)

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                        //updateUI(null)
                    }

                }
    }

    //Registrar con google

    fun initGoogleClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    fun loginWithGoogle() {
        mGoogleSignInClient.signOut()

        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                //Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)

            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                //Log.w(TAG, "Google sign in failed", e)
                // ...
            }
        }

        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        //Log.d(TAG, "signInWithCredential:success")
                        val user = auth.currentUser
                        updateUI(user)

                    } else {
                        Toast.makeText(
                                applicationContext,
                                "No se pudo iniciar sesion",
                                Toast.LENGTH_LONG
                        ).show()

                    }

                }
    }

    /*fun updateUI(currentUser: FirebaseUser?) { //send current user to next activity
        if (currentUser == null) return
        val intent = Intent(this, PrincipalActivity::class.java)
        startActivity(intent)
        finish()
    }*/

    fun updateUI(currentUser: FirebaseUser?) { //send current user to next activity
        if (currentUser == null) return
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    //Ingresar con correo


    fun showConfirmationDialog(title: Int, msg: String) {
        val dlg = AlertDialog.Builder(this)
        dlg.setMessage(msg)
        dlg.setTitle(title)
        dlg.setPositiveButton(R.string.ok, null)
        dlg.show()

    }

    fun showDialogResetPassword() {
        val dlg = AlertDialog.Builder(this)
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
                Toast.makeText(applicationContext, "Llenar el campo", Toast.LENGTH_SHORT).show()

            }
        }
    }


    fun resetPasword(emailAddress:String){
        auth.sendPasswordResetEmail(emailAddress).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(applicationContext, "Mensaje enviado", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(applicationContext, "Correo no registrado", Toast.LENGTH_SHORT).show()
            }
        }
    }


}