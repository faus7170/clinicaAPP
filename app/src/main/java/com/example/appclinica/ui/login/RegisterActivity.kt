package com.example.appclinica.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.appclinica.HomeActivity
import com.example.appclinica.R
import com.example.appclinica.sdk.paymentez.rest.BackendService
import com.example.appclinica.sdk.paymentez.rest.RetrofitFactory
import com.example.appclinica.sdk.paymentez.rest.model.CreateChargeResponse
import com.example.appclinica.sdk.paymentez.rest.utils.Alert
import com.example.appclinica.sdk.paymentez.rest.utils.Constants
import com.example.appclinica.ui.login.model.activationCodeModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import com.paymentez.android.Paymentez
import com.paymentez.android.model.Card
import com.paymentez.android.rest.model.ErrorResponse
import com.paymentez.android.view.CardMultilineWidget
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.text.DateFormat
import java.util.*


/*import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.paymentez.android.Paymentez;
import com.paymentez.examplestore.utils.Constants;*/

class RegisterActivity : AppCompatActivity() {
    lateinit var imagenDefault: String
    lateinit var btnRegisterUser: Button
    lateinit var editEmail: EditText
    lateinit var editPass: EditText
    lateinit var editRepitPass: EditText
    lateinit var activationCode: EditText
    lateinit var backendService: BackendService
    lateinit var checkBoxMensual: CheckBox
    lateinit var checkBoxSemestral: CheckBox
    lateinit var checkBoxAnual: CheckBox
    lateinit var userList: MutableList<activationCodeModel>
    lateinit var buttonSelectPayment: LinearLayout
    var imageViewCCImage: ImageView? = null
    var textViewCCLastFour: TextView? = null
    var valorPagar = 0.00
    var codeActivation = false
    var SELECT_CARD_REQUEST = 1004
    var CARD_TOKEN = ""

    private val responseLauncher= registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ activityResult->
            if (activityResult.resultCode == RESULT_OK) {
                CARD_TOKEN = activityResult.data?.getStringExtra("CARD_TOKEN").toString()
                val CARD_TYPE = activityResult.data?.getStringExtra("CARD_TYPE")
                val CARD_LAST4 = activityResult.data?.getStringExtra("CARD_LAST4")
                if (CARD_LAST4 != null && CARD_LAST4 != "") {
                    textViewCCLastFour?.setText("XXXX.$CARD_LAST4")
                    imageViewCCImage?.setImageResource(Card.getDrawableBrand(CARD_TYPE))
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = setContentView(R.layout.activity_register)
        println("Entro activation code 888")
        Paymentez.setEnvironment(
            Constants.PAYMENTEZ_IS_TEST_MODE,
            Constants.PAYMENTEZ_CLIENT_APP_CODE,
            Constants.PAYMENTEZ_CLIENT_APP_KEY
        );
        println("Entro activation code 0000")
        elementsById()
        println("Entro activation code 2222")
        title = "Autenticación"
        activationCode.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
                println("Entro activation code 333333")
            else {
                println("Entro activation code")
                val db = Firebase.firestore
                userList = mutableListOf()
                if (!activationCode.text.toString().equals(null) || !activationCode.text.toString()
                        .equals("")
                ) {
                    println("Entro activation code 1111")
                    db.collection("activationcode").document(activationCode.text.toString()).get()
                        .addOnSuccessListener { document ->
                            val code = document.getString("code")
                            val used = document.getBoolean("used")
                            val active = document.getBoolean("active")
                            if (!code.equals(null) && used == false && active == true) {
                                buttonSelectPayment.visibility = View.INVISIBLE
                                codeActivation = true
                                showAlert("EL codigo es correcto")
                            } else {
                                codeActivation = false
                                showAlert("EL codigo no existe o es incorrecto, favor seleccione su metodo de pago")
                                buttonSelectPayment.visibility = View.VISIBLE
                            }
                        }.addOnFailureListener { exception ->
                        codeActivation = false
                        val e = exception.message.toString()
                        println("========== Error: $e")
                    }
                } else {
                    codeActivation = false
                    showAlert("EL codigo no existe o es incorrecto, favor seleccione su metodo de pago")
                    buttonSelectPayment.visibility = View.VISIBLE
                }
            }
        })

        setUp()
    }

    fun setUp() {
        println("Entro al set up")

        btnRegisterUser.setOnClickListener {
            if (codeActivation) {
                registerNewUser(
                    editEmail.text.toString(),
                    editPass.text.toString(),
                    editRepitPass.text.toString()
                )
                val db = Firebase.firestore
                db.collection("activationcode").document(activationCode.text.toString()).update(
                    hashMapOf(
                        "active" to false,
                        "dused" to Date(),
                        "used" to true
                    ) as Map<String, Any>
                )
            } else {
                println("Pago")
                payment()

                // addCard()
            }
        }
       /* buttonSelectPayment.setOnClickListener(View.OnClickListener {
            updateUICard();
        })*/
        buttonSelectPayment = findViewById<View>(R.id.buttonSelectPayment) as LinearLayout
        buttonSelectPayment.setOnClickListener {
            val intent = Intent(applicationContext, ListCardsActivity::class.java)
            responseLauncher.launch(intent)
           // startActivityForResult(intent, SELECT_CARD_REQUEST)
        }
        checkBoxMensual.setOnClickListener {
            valorPagar = 4.99
            checkBoxSemestral.isChecked = false
            checkBoxAnual.isChecked = false
        }
        checkBoxSemestral.setOnClickListener {
            valorPagar = 16.99
            checkBoxMensual.isChecked = false
            checkBoxAnual.isChecked = false
        }
        checkBoxAnual.setOnClickListener {
            valorPagar = 24.99
            checkBoxMensual.isChecked = false
            checkBoxSemestral.isChecked = false
        }
    }
 /*   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_CARD_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                CARD_TOKEN = data.getStringExtra("CARD_TOKEN").toString()
                val CARD_TYPE = data.getStringExtra("CARD_TYPE")
                val CARD_LAST4 = data.getStringExtra("CARD_LAST4")
                if (CARD_LAST4 != null && CARD_LAST4 != "") {
                    textViewCCLastFour?.setText("XXXX.$CARD_LAST4")
                    imageViewCCImage?.setImageResource(Card.getDrawableBrand(CARD_TYPE))
                }
            }
        }
    }*/
    fun registerNewUser(email: String, password: String, passwordRepit: String) {
        if (checkCredentials(email, password, passwordRepit)) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        val user = FirebaseAuth.getInstance().currentUser
                        updateUI(user)
                    } else {
                        val s = task.exception.toString()
                        println("Error: $s")
                    }
                }
        }
    }
    private fun updateUI(currentUser: FirebaseUser?) { //send current user to next activity
        if (currentUser == null) return
        val intent = Intent(this, HomeActivity::class.java).apply {
        }
        startActivity(intent)
    }
    private fun updateUICard() { //send current user to next activity
        //if (currentUser == null) return
        val intent = Intent(this, ListCardsActivity::class.java).apply {
        }
        startActivity(intent)
    }
    fun checkCredentials(email: String, password: String, passwordRepit: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(applicationContext, "Llenar los campos", Toast.LENGTH_LONG).show()
            return false
        } else if (!email.contains("@") || email.length < 6) {
            Toast.makeText(applicationContext, "Verificar que el correo", Toast.LENGTH_LONG).show()
            return false
        } else if (!password.equals(passwordRepit)) {
            Toast.makeText(applicationContext, "Claves no conciden", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }
    private fun elementsById() {
        activationCode = findViewById(R.id.txtActivationCode)
        editEmail = findViewById(R.id.editEmail)
        editPass = findViewById(R.id.txtRegisterClave)
        editRepitPass = findViewById(R.id.txtConfirmarClave)
        btnRegisterUser = findViewById(R.id.btnRegistrarUser)
        checkBoxMensual = findViewById(R.id.checkBoxMes)
        checkBoxSemestral = findViewById(R.id.checkBoxSemestral)
        checkBoxAnual = findViewById(R.id.checkBoxAnual)
        buttonSelectPayment = findViewById<View>(R.id.buttonSelectPayment) as LinearLayout
        imageViewCCImage = findViewById<View>(R.id.imageViewCCImage) as ImageView
        textViewCCLastFour = findViewById<View>(R.id.textViewCCLastFour) as TextView

        imagenDefault ="https://www.nicepng.com/png/detail/202-2022264_usuario-annimo-usuario-annimo-user-icon-png-transparent.png"
    }

    private fun payment(){
        val backendService: BackendService = RetrofitFactory.getClient().create(BackendService::class.java)
       // fun onClick(v: View?) {
            if (CARD_TOKEN == null || CARD_TOKEN == "") {
                Alert.show(
                    applicationContext,
                    "Error",
                    "You Need to Select a Credit Card!"
                )
            } else {

                /*val layout: RelativeLayout = findViewById(this)
                val pd = ProgressDialog(applicationContext)
                pd.setMessage("")
                pd.show()*/
                val ORDER_AMOUNT = 10.5
                val ORDER_ID = "" + System.currentTimeMillis()
                val ORDER_DESCRIPTION = "ORDER #$ORDER_ID"
                backendService.createCharge(
                    Constants.USER_ID, Paymentez.getSessionId(applicationContext),
                    CARD_TOKEN, ORDER_AMOUNT, ORDER_ID, ORDER_DESCRIPTION
                ).enqueue(object : Callback<CreateChargeResponse?> {
                    override fun onResponse(
                        call: Call<CreateChargeResponse?>,
                        response: Response<CreateChargeResponse?>
                    ) {
                       // pd.dismiss()
                        val createChargeResponse: CreateChargeResponse? = response.body()
                        if (response.isSuccessful() && createChargeResponse != null && createChargeResponse.getTransaction() != null) {
                            Alert.show(
                                applicationContext,
                                "Successful Charge",
                                """
                            status: ${createChargeResponse.getTransaction().getStatus().toString()}
                            status_detail: ${
                                    createChargeResponse.getTransaction().getStatusDetail()
                                        .toString()
                                }
                            message: ${
                                    createChargeResponse.getTransaction().getMessage().toString()
                                }
                            transaction_id:${createChargeResponse.getTransaction().getId()}
                            """.trimIndent()
                            )
                            //Guarda el registro del usuario
                            registerNewUser(
                                editEmail.text.toString(),
                                editPass.text.toString(),
                                editRepitPass.text.toString()
                            )

                        } else {
                            val gson = GsonBuilder().create()
                            try {
                                val errorResponse = gson.fromJson(
                                    response.errorBody()!!.string(),
                                    ErrorResponse::class.java
                                )
                                Alert.show(
                                    applicationContext,
                                    "Error",
                                    errorResponse.error.type
                                )

                               /*Toast.makeText(applicationContext,
                                    "Error"+errorResponse.error.type,
                                    Toast.LENGTH_LONG
                                ).show()*/

                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }
                    }

                    override fun onFailure(call: Call<CreateChargeResponse?>, e: Throwable) {
                       // pd.dismiss()
                        Alert.show(
                            applicationContext,
                            "Error",
                            e.localizedMessage
                        )
                    }
                })
            }
        //}
    }

    private fun showAlert(mensaje : String){
        Toast.makeText(applicationContext, mensaje, Toast.LENGTH_LONG).show()

        /*val builder= AlertDialog.Builder(this)
        builder.setTitle("!Ups!")
        //builder.
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog =builder.create()
        dialog.show()*/
    }
    private fun millisToDate(millis: Long): String? {
        return DateFormat.getDateInstance(DateFormat.SHORT).format(millis)
        //You can use DateFormat.LONG instead of SHORT
    }
}