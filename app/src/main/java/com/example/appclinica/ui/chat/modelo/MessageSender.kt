package com.example.appclinica.ui.chat.modelo

class MessageSender: DatosMensaje {

    lateinit var hora: Map<String,String>

    constructor(sender: String, reciver: String, msm: String, seen: Boolean, hora: Map<String, String>) : super(sender, reciver, msm, seen) {
        this.hora = hora
    }

    constructor()

}