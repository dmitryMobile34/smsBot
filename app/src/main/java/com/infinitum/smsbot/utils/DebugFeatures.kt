package com.example.projectd.utils

import android.widget.Toast
import com.infinitum.smsbot.utils.APP_ACTIVITY

fun showToast(message:String){
    Toast.makeText(APP_ACTIVITY, message, Toast.LENGTH_SHORT).show()
}

fun getRandomString(length: Int) : String { //DEBUG
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}
