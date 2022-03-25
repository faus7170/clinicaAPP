package com.example.appclinica.ui.chat.utils

import android.util.Log
import com.example.appclinica.ui.chat.utils.Constants.OPEN_GOOGLE
import com.example.appclinica.ui.chat.utils.Constants.OPEN_SEARCH
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

object BotResponse {

    fun basicResponses(_message: String): String {

        val random = (0..2).random()
        val message =_message.toLowerCase()

        return when {

            //Flips a coin
            message.contains("flip") && message.contains("coin") -> {
                val r = (0..1).random()
                val result = if (r == 0) "heads" else "tails"

                "I flipped a coin and it landed on $result"
            }

            //Math calculations
            /*message.contains("solve") -> {
                val equation: String? = message.substringAfterLast("solve")
                return try {
                    val answer = SolveMath.solveMath(equation ?: "0")
                    "$answer"

                } catch (e: Exception) {
                    "Sorry, I can't solve that."
                }
            }*/

            message.contains("hola") -> {
                when (random) {
                    0 -> "¿Hola como estas?"
                    1 -> "¿Hi en que te puedo ayudar?"
                    2 -> "¿Hola como te sientes hoy?"
                    else -> "error" }
            }

            message.contains("bien") -> {
                when (random) {
                    0 -> "Genial y en que puedo ayudarte"
                    1 -> "¿Y que hiciste hoy?"
                    2 -> "¿Conociste alguien nuevo hoy?"
                    else -> "error"
                }
            }

            message.contains("problema") -> {
                when (random) {
                    0 -> "¿Me puedes contar que te paso?"
                    1 -> "Recuerda que estoy aqui contigo"
                    2 -> "Sigue adelante, siempre busca una solucción"
                    else -> "error"
                }
            }
            

            //What time is it?
            message.contains("time") && message.contains("?")-> {
                val timeStamp = Timestamp(System.currentTimeMillis())
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
                val date = sdf.format(Date(timeStamp.time))

                date.toString()
            }

            //Open Google
            message.contains("open") && message.contains("google")-> {
                OPEN_GOOGLE
            }

            //Search on the internet
            message.contains("search")-> {
                OPEN_SEARCH
            }

            //When the programme doesn't understand...
            else -> {
                when (random) {
                    0 -> "Puedes darme un poco mas de informacion"
                    1 -> "Intenta con otra pregunta"
                    2 -> "Prodias volver repetir pero con otras palabras"
                    else -> "error"
                }
            }
        }
    }
}