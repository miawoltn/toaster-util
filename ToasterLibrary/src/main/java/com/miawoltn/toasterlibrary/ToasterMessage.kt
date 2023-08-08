package com.miawoltn.toasterlibrary

import android.content.Context
import android.widget.Toast

class ToasterMessage {
    companion object {
        fun s(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}