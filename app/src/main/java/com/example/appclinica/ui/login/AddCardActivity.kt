package com.example.appclinica.ui.login

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.appclinica.R
import com.example.appclinica.sdk.paymentez.rest.utils.Alert
import com.example.appclinica.sdk.paymentez.rest.utils.Constants
import com.paymentez.android.Paymentez
import com.paymentez.android.model.Card
import com.paymentez.android.rest.TokenCallback
import com.paymentez.android.rest.model.PaymentezError
import com.paymentez.android.view.CardMultilineWidget

class AddCardActivity : AppCompatActivity() {

    var buttonNext: Button? = null
    var cardWidget: CardMultilineWidget? = null
    var mContext: Context? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)
        mContext = this
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
        }


        val uid = Constants.USER_ID
        val email = Constants.USER_EMAIL

        cardWidget = findViewById<View>(R.id.card_multiline_widget) as CardMultilineWidget
        buttonNext = findViewById<View>(R.id.buttonAddCard) as Button
        buttonNext!!.setOnClickListener(View.OnClickListener {
            buttonNext!!.isEnabled = false
            val cardToSave = cardWidget!!.card
            if (cardToSave == null) {
                buttonNext!!.isEnabled = true
                Alert.show(
                    mContext,
                    "Error",
                    "Invalid Card Data"
                )
                return@OnClickListener
            } else {
                val pd = ProgressDialog(this@AddCardActivity)
                pd.setMessage("")
                pd.show()
                Paymentez.addCard(mContext, uid, email, cardToSave, object : TokenCallback {
                    override fun onSuccess(card: Card) {
                        buttonNext!!.isEnabled = true
                        pd.dismiss()
                        if (card != null) {
                            if (card.status == "valid") {
                                Alert.show(
                                    mContext,
                                    "Card Successfully Added",
                                    """
                                        status: ${card.status}
                                        Card Token: ${card.token}
                                        transaction_reference: ${card.transactionReference}
                                        """.trimIndent()
                                )
                            } else if (card.status == "review") {
                                Alert.show(
                                    mContext,
                                    "Card Under Review",
                                    """
                                        status: ${card.status}
                                        Card Token: ${card.token}
                                        transaction_reference: ${card.transactionReference}
                                        """.trimIndent()
                                )
                            } else {
                                Alert.show(
                                    mContext,
                                    "Error",
                                    """
                                        status: ${card.status}
                                        message: ${card.message}
                                        """.trimIndent()
                                )
                            }
                        }

                        //TODO: Create charge or Save Token to your backend
                    }
                    override fun onError(error: PaymentezError) {
                        println("ERROOOOOOOOOOOO =============")
                        buttonNext!!.isEnabled = true
                        pd.dismiss()
                        Alert.show(
                            mContext,
                            "Error",
                            """
                                Type: ${error.type}
                                Help: ${error.help}
                                Description: ${error.description}
                                """.trimIndent()
                        )

                        //TODO: Handle error
                    }
                })
            }
        })

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
}