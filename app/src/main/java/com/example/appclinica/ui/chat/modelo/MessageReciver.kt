package com.example.appclinica.ui.chat.modelo

class MessageReciver: DatosMensaje {

    var hora : Long = 0

    constructor(sender: String, reciver: String, msm: String, seen: Boolean, hora: Long) : super(sender, reciver, msm, seen) {
        this.hora = hora
    }

    constructor()

}