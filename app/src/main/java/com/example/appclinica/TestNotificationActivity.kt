package com.example.appclinica

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appclinica.notification.PushNotification
import com.example.appclinica.sdk.paymentez.rest.RetrofitFactory
import com.example.appclinica.sdk.paymentez.rest.model.CreateChargeResponse
import com.example.appclinica.sdk.paymentez.rest.model.DeleteCardResponse
import com.example.appclinica.sdk.paymentez.rest.model.GetCardsResponse
import com.example.appclinica.sdk.paymentez.rest.utils.Constants
import com.example.appclinica.sdk.paymentez.rest.utils.MyCardAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.paymentez.android.Paymentez
import com.paymentez.android.model.Card
import com.paymentez.android.rest.TokenCallback
import com.paymentez.android.rest.model.ErrorResponse
import com.paymentez.android.rest.model.PaymentezError
import com.paymentez.android.view.CardMultilineWidget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException


const val TOPIC = "/topics/myTopic2"

class TestNotificationActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    lateinit var button: Button
    lateinit var buttonHacerMostrarPago: Button
    lateinit var etTitle: EditText
    lateinit var etContenido: EditText
    lateinit var etToken: EditText
    lateinit var cardWidget: CardMultilineWidget
    var retrofit: Retrofit? = null
    lateinit var checkBoxM: CheckBox
    lateinit var checkBoxS: CheckBox
    lateinit var checkBoxA: CheckBox
    lateinit var recyclerView: RecyclerView
    private var monto: Double = 00.00
    lateinit var mContext: Context
    lateinit var myCardAdapter: MyCardAdapter

    lateinit var list : MutableList<Card>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_notification)

        cardWidget = findViewById(R.id.card_multiline_widget)
        checkBoxM = findViewById(R.id.checkBoxMes)
        checkBoxS = findViewById(R.id.checkBoxSemestral)
        checkBoxA = findViewById(R.id.checkBoxAnual)
        recyclerView = findViewById(R.id.recyclerViewCards)
        recyclerView.layoutManager = LinearLayoutManager(this)

        //checkValidacion()
       // getCards()
        button = findViewById(R.id.btnAddCars)
        buttonHacerMostrarPago = findViewById(R.id.btnHacerPago)
        mContext = this
        //Notifacador
       /* FirebaseService.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            FirebaseService.token = it.token
            etToken.setText(it.token)
        }
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        button = findViewById(R.id.btnEnviarNotification)
        etTitle = findViewById(R.id.etTitleTest)
        etContenido = findViewById(R.id.etContenidoTest)
        etToken = findViewById(R.id.etTokenTest)*/

        //apiservice = getCliente("https://fcm.googleapis.com/").create(APIservice::class.java)

        buttonHacerMostrarPago.setOnClickListener{
            //deleteCarad("16045479002369918941")
           // getCards()
        }

        Paymentez.setEnvironment(Constants.PAYMENTEZ_IS_TEST_MODE, Constants.PAYMENTEZ_CLIENT_APP_CODE, Constants.PAYMENTEZ_CLIENT_APP_KEY);

        button.setOnClickListener {
            /*val title = etTitle.text.toString()
            val message = etContenido.text.toString()
            val recipientToken = etToken.text.toString()
            if(title.isNotEmpty() && message.isNotEmpty() && recipientToken.isNotEmpty()) {
                PushNotification(NotificationData(title, message,"notificacion recivido"), recipientToken).also {
                    sendNotification(it)
                }
                //notidos("test",message,"test",etToken.toString())
            }*/

            val cardToSabe : Card = cardWidget.card!!

            Paymentez.addCard(this, Constants.USER_ID, Constants.USER_EMAIL, cardToSabe, object :
                TokenCallback {
                override fun onError(error: PaymentezError?) {
                    Log.d("Tarjeta_error", "Error" +
                            """
                                    Type: ${error!!.type}
                                    Help: ${error.help}
                                    Description: ${error.description}
                                    """.trimIndent())
                }

                override fun onSuccess(card: Card?) {
                    if (card != null) {
                        if (card.status == "valid") {

                            Log.d("Tarjeta_Valid", "Card Successfully Added" +
                                    "status: " + card.status + "\n" +
                                    "Card Token: " + card.token + "\n" +
                                    "Card Auth: " + card.card_auth + "\n" +
                                    "Card HolderName " + card.holderName + "\n" +
                                    "Card id " + card.id + "\n" +
                                    "Card Number " + card.number + "\n" +
                                    "Card FiscalNumber " + card.fiscal_number + "\n" +
                                    "Card CustomerId " + card.customerId + "\n" +
                                    "Card uid " + Constants.USER_ID + "\n" +
                                    "transaction_reference: " + card.transactionReference)

                          // getCards()

                            //Implementacion de pago si la tarjeta es valida
                            //pagoconpayamantel(card.token, card.type, card.last4)

                        } else if (card.status == "review") {

                            Log.d("Tarjeta_review", "Card Under Review" +
                                    "status: " + card.status + "\n" +
                                    "Card Token: " + card.token + "\n" +
                                    "transaction_reference: " + card.transactionReference)

                        } else {
                            Log.d("Tarjeta_errorCard", "Error" + card.status + "\n" +
                                    "message: " + card.message)
                        }

                    }
                }


            })
        }

            /*val cardToSabe : Card = cardWidget.card!!

            if (cardToSabe == null){
                Toast.makeText(this, "Tarjeta invalida", Toast.LENGTH_LONG).show()
            }else {



        }*/


    }

/*
    fun deleteCarad(token: String) = CoroutineScope(Dispatchers.IO).launch {
        RetrofitFactory.api.deleteCard(Constants.USER_ID, token)!!.enqueue(
                object : Callback<DeleteCardResponse?> {
                    override fun onResponse(call: Call<DeleteCardResponse?>, response: Response<DeleteCardResponse?>) {
                        val deleteCardResponse: DeleteCardResponse = response.body()!!

                        if (response.isSuccessful) {
                            getCards()
                            Log.d("Tarjeta_elimanda", "msms " + deleteCardResponse.message)
                        } else {
                            val gson = GsonBuilder().create()

                            try {
                                val errorResponse = gson.fromJson(response.errorBody()!!.string(), ErrorResponse::class.java)
                                /*Alert.show(mContext,
                                        "Error",
                                        errorResponse.error.type)*/

                                Log.d("Tarjeta_noDelate", "Error" +
                                        errorResponse.error.type)
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }

                        }
                    }

                    override fun onFailure(call: Call<DeleteCardResponse?>, t: Throwable) {
                        Log.d("Tarjeta_ErrorCard", "Error" +
                                t.localizedMessage)
                    }


                }
        )

    }

    fun getCards() = CoroutineScope(Dispatchers.IO).launch {
        list = mutableListOf()

        RetrofitFactory.api.getCards(Constants.USER_ID)!!.enqueue(
                object : Callback<GetCardsResponse?> {
                    override fun onResponse(call: Call<GetCardsResponse?>, response: Response<GetCardsResponse?>) {

                        //list.clear()

                        val getCardsResponse: GetCardsResponse = response.body()!!
                        Log.d("Tarjeta_arreglo", "array " + response.body()!!)


                        if (response.isSuccessful) {
                            list.addAll(getCardsResponse.cards)
                            Log.d("Tarjeta_arreglo", "array " + list)

                            myCardAdapter = MyCardAdapter(list) {

                                //Toast.makeText(this@TestNotificationActivity,"token "+it.token,Toast.LENGTH_LONG).show()
                                //deleteCarad(it.token)
                            }
                            recyclerView.adapter = myCardAdapter


                        } else {
                            val gson = GsonBuilder().create()
                            try {
                                val errorResponse = gson.fromJson(response.errorBody()!!.string(), ErrorResponse::class.java)
                                /*Alert.show(mContext,
                                        "Error",
                                        errorResponse.error.type)*/

                                Log.d("Tarjeta_arreglo", "Error" +
                                        errorResponse.error.type)
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }


                    }

                    override fun onFailure(call: Call<GetCardsResponse?>, t: Throwable) {
                        Log.d("Tarjeta_arraglo", "Error"
                        )
                    }

                }
        )
    }
    

    private fun pagoconpayamantel(token: String, type: String, last4: String) = CoroutineScope(Dispatchers.IO).launch {

          RetrofitFactory.api.createCharge(Constants.USER_ID,
                  Paymentez.getSessionId(mContext), token, monto, "Tipo de pago",
                  "Mes")!!.enqueue(object : Callback<CreateChargeResponse?> {
              override fun onResponse(call: Call<CreateChargeResponse?>, response: Response<CreateChargeResponse?>) {

                  val createChargeResponse: CreateChargeResponse = response.body()!!
                  if (response.isSuccessful && createChargeResponse != null && createChargeResponse.transaction != null) {


                      Log.d("Tarjeta_Pago_Successful", "Successful Charge" +
                              """
                status: ${createChargeResponse.transaction.status.toString()}
                status_detail: ${createChargeResponse.transaction.status_detail.toString()}
                message: ${createChargeResponse.transaction.message}
                transaction_id:${createChargeResponse.transaction.id}
                """.trimIndent())
                  } else {
                      val gson = GsonBuilder().create()
                      try {
                          val errorResponse = gson.fromJson(response.errorBody()!!.string(), ErrorResponse::class.java)
                          /*Alert.show(mContext,
                                "Error",
                                errorResponse.error.type)*/

                          Log.d("Tarjeta_Pago_rechazado", "Error" +
                                  errorResponse.error.type)
                      } catch (e: IOException) {
                          e.printStackTrace()
                      }
                  }
              }

              override fun onFailure(call: Call<CreateChargeResponse?>, t: Throwable) {
                  Log.d("Tarjeta_ErrorCompra", "Error" +
                          t.localizedMessage)
              }


          })

    }

    fun checkValidacion(){

        checkBoxM.setOnClickListener {
            monto = 6.00
            checkBoxS.isChecked = false
            checkBoxA.isChecked = false
        }

        checkBoxS.setOnClickListener {
            monto = 12.00
            checkBoxM.isChecked = false
            checkBoxA.isChecked = false
        }

        checkBoxA.setOnClickListener {
            monto = 24.00
            checkBoxM.isChecked = false
            checkBoxS.isChecked = false
        }

    }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful) {
                Log.d(TAG, "Response: ${Gson().toJson(response)}")
            } else {
                Log.e(TAG, response.errorBody().toString())
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }*/
}


