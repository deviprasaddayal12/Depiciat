package com.deviprasaddayal.depiciat.utils

import android.util.Log

object LogUtils {
    val TAG = LogUtils::class.java.canonicalName

    private val shouldLog = true

    fun loge(tag: String, message: String) {
        if (shouldLog)
            Log.e(tag, message)
    }

    fun logi(tag: String, message: String) {
        if (shouldLog)
            Log.i(tag, message)
    }

    fun logd(tag: String, message: String) {
        if (shouldLog)
            Log.d(tag, message)
    }
}