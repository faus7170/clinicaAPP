package com.example.appclinica.ui.login.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.appclinica.HomeActivity
import com.example.appclinica.R
//import com.example.appclinica.paymantel.BackendService
//import com.example.appclinica.paymantel.modelo.CreateChargeResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import com.paymentez.android.Paymentez
import com.paymentez.android.model.Card
import com.paymentez.android.rest.TokenCallback
import com.paymentez.android.rest.model.ErrorResponse
import com.paymentez.android.rest.model.PaymentezError
import com.paymentez.android.view.CardMultilineWidget
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.text.DateFormat

/**
 *@author David Aguinsaca
 * Fragmento de registro de un usuario
 **/

class RegisterFragment : Fragment(), View.OnClickListener {

    lateinit var imagenDefault: String
    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }

    /*lateinit var btnRegisterUser: Button
    lateinit var editEmail : EditText
    lateinit var editPass : EditText
    lateinit var editRepitPass: EditText
    lateinit var cardWidget: CardMultilineWidget
    lateinit var backendService: BackendService
    lateinit var checkBoxMensual: CheckBox
    lateinit var checkBoxSemestral: CheckBox
    lateinit var checkBoxAnual: CheckBox
    var valorPagar = 0.00


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_register, container, false)
        elementsById(view)

        return view
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btnRegistrarCorreo ->{
                registerNewUser(editEmail.text.toString(),editPass.text.toString(),editRepitPass.text.toString())
            }
            R.id.checkBoxMes -> {
                valorPagar = 6.00
                checkBoxSemestral.isChecked = false
                checkBoxAnual.isChecked = false
            }
            R.id.checkBoxSemestral ->{
                valorPagar = 12.00
                checkBoxMensual.isChecked = false
                checkBoxAnual.isChecked = false
            }
            R.id.checkBoxAnual ->{
                valorPagar = 24.00
                checkBoxMensual.isChecked = false
                checkBoxSemestral.isChecked = false
            }
        }
    }

    fun addCard(){

        val cardToSave = cardWidget.card

        if (cardToSave == null) {
            Alert.show(
                    activity,
                    "Error",
                    "Tarjeta invalida"
            )
            return
        } else {
            Paymentez.addCard(activity, Constants.USER_ID, Constants.USER_EMAIL, cardToSave, object : TokenCallback {
                override fun onSuccess(card: Card) {
                    if (card.status == "valid") {

                        pago(card.token)

                        Alert.show(
                                activity,
                                "La tarjeta fue valida",
                                "status: " + card.status + "\n" +
                                        "Card Token: " + card.token + "\n" +
                                        "transaction_reference: " + card.transactionReference
                        )

                    } else if ((card.status == "review")) {
                        Alert.show(
                                activity,
                                "Tarjeta ya esta registrada",
                                ("status: " + card.status + "\n" +
                                        "Card Token: " + card.token + "\n" +
                                        "transaction_reference: " + card.transactionReference)
                        )
                    } else {
                        Alert.show(
                                activity,
                                "Error",
                                ("status: " + card.status + "\n" +
                                        "message: " + card.message)
                        )
                    }

                    //TODO: Create charge or Save Token to your backend
                }

                override fun onError(error: PaymentezError) {
                    //Log.d("test_TokenD","token"+error.type)
                    Alert.show(
                            activity,
                            "Error",
                            ("Type: " + error.type + "\n" +
                                    "Help: " + error.help + "\n" +
                                    "Description: " + error.description)
                    )

                }
            })
        }
    }

    fun pago(CARD_TOKEN: String){
        Log.d("test_Token", "valor " + CARD_TOKEN)

        if (CARD_TOKEN == "") {
            Alert.show(
                    activity,
                    "Error",
                    "Necesitas seleccionar una tarjeta"
            )
        } else {
            val ORDER_AMOUNT = 10.5
            //val ORDER_ID = "" + System.currentTimeMillis()
            val ORDER_ID = "" + millisToDate(System.currentTimeMillis())
            val ORDER_DESCRIPTION = "ORDER #$ORDER_ID"
            backendService.createCharge(
                    Constants.USER_ID, Paymentez.getSessionId(activity),
                    CARD_TOKEN, ORDER_AMOUNT, ORDER_ID, ORDER_DESCRIPTION
            )!!.enqueue(object : Callback<CreateChargeResponse?> {
                override fun onResponse(
                        call: Call<CreateChargeResponse?>,
                        response: Response<CreateChargeResponse?>
                ) {
                    val createChargeResponse: CreateChargeResponse? = response.body()
                    if (response.isSuccessful() && createChargeResponse != null && createChargeResponse.transaction != null) {
                        /*Alert.show(
                            this@MainActivity,
                            "Successful Charge",
                            """
                            date: ${createChargeResponse.transaction.paymentDate.toString()}
                            date metodo: ${ORDER_ID}
                            status_detail: ${createChargeResponse.transaction.statusDetail.toString()}
                            message: ${createChargeResponse.transaction.message.toString()}
                            transaction_id:${createChargeResponse.transaction.id}
                            """.trimIndent()
                        )*/
                            //sendMail(createChargeResponse.transaction.id, createChargeResponse.transaction.amount,
                        //  ORDER_ID, createChargeResponse.transaction.status)
                    } else {
                        val gson = GsonBuilder().create()
                        try {
                            val errorResponse = gson.fromJson(
                                    response.errorBody()!!.string(),
                                    ErrorResponse::class.java
                            )
                            Alert.show(
                                    activity,
                                    "Error",
                                    errorResponse.error.type
                            )
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<CreateChargeResponse?>, e: Throwable) {
                    Alert.show(
                            activity,
                            "Error",
                            e.localizedMessage
                    )
                }
            })
        }
    }

    private fun millisToDate(millis: Long): String? {
        return DateFormat.getDateInstance(DateFormat.SHORT).format(millis)
        //You can use DateFormat.LONG instead of SHORT
    }

    //Verificar los campos de correo y password
    fun checkCredentials(email: String, password: String, passwordRepit: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(activity, "Llenar los campos", Toast.LENGTH_LONG).show()
            return false
        }else if (!email.contains("@") || email.length < 6) {
            Toast.makeText(activity, "Verificar que el correo", Toast.LENGTH_LONG).show()
            return false
        }else if(!password.equals(passwordRepit)){
            Toast.makeText(activity, "Claves no conciden", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    //Conexion con firebase para el registro de una nueva cuenta
    fun registerNewUser(email: String, password: String, passwordRepit: String) {
        val auth: FirebaseAuth = FirebaseAuth.getInstance()

        if (checkCredentials(email,password,passwordRepit)){
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = auth.currentUser
                            //setDatos(user.uid)
                            updateUI(user, user?.uid.toString())
                            //sendEmailVerification(user)
                        } else {
                            Toast.makeText(activity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                            //updateUI(null)
                        }
                    }
        }


    }

    //Enviar un mensaje de verificacion para activiar la cuenta
    fun sendEmailVerification(user: FirebaseUser) {
        //Log.d(TAG, "started Verification")
        user!!.sendEmailVerification()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        //showConfirmationDialog(R.string.confirm_email, getString(R.string.please_confirm_email, user.email))
                        Toast.makeText(activity, "Mensaje de autenticacion enviado al correo", Toast.LENGTH_SHORT).show()

                    }else {
                        //Log.e(TAG, "sendEmailVerification", task.exception)
                    }
                }
    }

    //Mostrar alerta
    fun showConfirmationDialog(title: Int, msg: String) {
        val dlg = AlertDialog.Builder(requireActivity())
        dlg.setMessage(msg)
        dlg.setTitle(title)
        dlg.setPositiveButton(R.string.ok, null)
        dlg.show()

    }

    //Cargar el siguiente activity para la configuracion del perfil
    fun updateUI(currentUser: FirebaseUser?, uid:String) { //send current user to next activity
        if (currentUser == null) return
        val intent = Intent(activity, HomeActivity::class.java)
        setDatos(uid,"Anonimo","default","default",imagenDefault,"default",false)
        startActivity(intent)
        requireActivity().finish()
    }

    fun setDatos(uid: String, nombre: String, descripcion: String, titulo: String, foto: String, genero:String, ispsicologo: Boolean){
        val db = Firebase.firestore
        val datos = hashMapOf(
                "nombre" to nombre,
                "descripcion" to descripcion,
                "titulo" to titulo,
                "foto" to foto,
                "genero" to genero,
                "ispsicologo" to ispsicologo
        )

        db.collection("usuarios").document(uid).set(datos)
                .addOnSuccessListener {
                    Toast.makeText(requireActivity(),"Usuario registrado con exito",Toast.LENGTH_LONG).show()
                }.addOnFailureListener {

                }
    }

    private fun elementsById(view: View) {
        editEmail = view.findViewById(R.id.editEmail)
        editPass = view.findViewById(R.id.txtRegisterClave)
        editRepitPass = view.findViewById(R.id.txtConfirmarClave)
        btnRegisterUser = view.findViewById(R.id.btnRegistrarCorreo)
        checkBoxMensual = view.findViewById(R.id.checkBoxMes)
        checkBoxSemestral = view.findViewById(R.id.checkBoxSemestral)
        checkBoxAnual = view.findViewById(R.id.checkBoxAnual)

        checkBoxMensual.setOnClickListener(this)
        checkBoxSemestral.setOnClickListener(this)
        checkBoxAnual.setOnClickListener(this)

        imagenDefault = "https://www.nicepng.com/png/detail/202-2022264_usuario-annimo-usuario-annimo-user-icon-png-transparent.png"
    }



*/

}


