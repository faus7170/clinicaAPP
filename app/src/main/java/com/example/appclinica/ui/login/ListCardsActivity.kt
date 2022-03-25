package com.example.appclinica.ui.login

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appclinica.R
import com.google.gson.GsonBuilder
import com.example.appclinica.sdk.paymentez.rest.BackendService
import com.example.appclinica.sdk.paymentez.rest.RetrofitFactory
import com.example.appclinica.sdk.paymentez.rest.model.DeleteCardResponse
import com.example.appclinica.sdk.paymentez.rest.model.GetCardsResponse
import com.example.appclinica.sdk.paymentez.rest.utils.Alert
import com.example.appclinica.sdk.paymentez.rest.utils.Constants
import com.example.appclinica.sdk.paymentez.rest.utils.MyCardAdapter
import com.paymentez.android.model.Card
import com.paymentez.android.rest.model.ErrorResponse

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ListCardsActivity : AppCompatActivity() {
    var listCard: ArrayList<Card>? = null
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    private var buttonAddCard: Button? = null

    var mContext: Context? = null
    var backendService: BackendService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_cards)
        mContext = this
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
        }
        buttonAddCard = findViewById<View>(R.id.buttonAddCard) as Button
        buttonAddCard!!.setOnClickListener {
            val intent = Intent(mContext, AddCardActivity::class.java)
            startActivity(intent)
        }
        mRecyclerView = findViewById<View>(R.id.my_recycler_view) as RecyclerView
        mRecyclerView!!.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(this)
        mRecyclerView!!.layoutManager = mLayoutManager
        backendService = RetrofitFactory.getClient().create(BackendService::class.java)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        getCards()
    }

    fun getCards() {
        val pd = ProgressDialog(this@ListCardsActivity)
        pd.setMessage("")
        pd.show()
        backendService!!.getCards(Constants.USER_ID)
            .enqueue(object : Callback<GetCardsResponse?> {
                override fun onResponse(
                    call: Call<GetCardsResponse?>,
                    response: Response<GetCardsResponse?>
                ) {
                    pd.dismiss()
                    val getCardsResponse = response.body()
                    if (response.isSuccessful) {
                        listCard = getCardsResponse!!.cards as ArrayList<Card>
                        mAdapter = MyCardAdapter(listCard,
                            MyCardAdapter.OnCardSelectedListener { card ->
                                val returnIntent = Intent()
                                returnIntent.putExtra("CARD_TOKEN", card.token)
                                returnIntent.putExtra("CARD_TYPE", card.type)
                                returnIntent.putExtra("CARD_LAST4", card.last4)
                                setResult(RESULT_OK, returnIntent)
                                finish()
                            },
                            MyCardAdapter.OnCardDeletedClickListener { card -> deleteCard(card) })
                        mRecyclerView!!.adapter = mAdapter
                    } else {
                        val gson = GsonBuilder().create()
                        try {
                            val errorResponse = gson.fromJson(
                                response.errorBody()!!.string(),
                                ErrorResponse::class.java
                            )
                            Alert.show(
                                mContext,
                                "Error",
                                errorResponse.error.type
                            )
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<GetCardsResponse?>, e: Throwable) {
                    pd.dismiss()
                    Alert.show(
                        mContext,
                        "Error",
                        e.localizedMessage
                    )
                }
            })
    }


    fun deleteCard(card: Card) {
        val pd = ProgressDialog(this@ListCardsActivity)
        pd.setMessage("")
        pd.show()
        backendService!!.deleteCard(Constants.USER_ID, card.token)
            .enqueue(object : Callback<DeleteCardResponse?> {
                override fun onResponse(
                    call: Call<DeleteCardResponse?>,
                    response: Response<DeleteCardResponse?>
                ) {
                    pd.dismiss()
                    val deleteCardResponse = response.body()
                    if (response.isSuccessful) {
                        getCards()
                        Alert.show(
                            mContext,
                            "Successfully Deleted Card",
                            deleteCardResponse!!.message
                        )
                    } else {
                        val gson = GsonBuilder().create()
                        try {
                            val errorResponse = gson.fromJson(
                                response.errorBody()!!.string(),
                                ErrorResponse::class.java
                            )
                            Alert.show(
                                mContext,
                                "Error",
                                errorResponse.error.type
                            )
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<DeleteCardResponse?>, e: Throwable) {
                    pd.dismiss()
                    Alert.show(
                        mContext,
                        "Error",
                        e.localizedMessage
                    )
                }
            })
    }


}

