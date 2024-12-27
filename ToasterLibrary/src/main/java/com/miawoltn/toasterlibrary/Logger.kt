package com.miawoltn.toasterlibrary

import android.util.Log

class Logger(private val tag: String) {
        fun debug(message: String) {
            Log.d(tag, message)
        }
        fun error(message: String) {
            Log.e(tag, message)
        }
        fun info(message: String) {
            Log.i(tag, message)
        }
        fun warn(message: String) {
            Log.w(tag, message)
        }
        fun verbose(message: String) {
            Log.v(tag, message)
        }
}