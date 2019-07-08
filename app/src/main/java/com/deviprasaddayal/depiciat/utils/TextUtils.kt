package com.deviprasaddayal.depiciat.utils

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText

object TextUtils {
    val TAG = TextUtils::class.java.canonicalName

    interface OnTextChangeListener {
        fun onTextChanged(isEmpty: Boolean, text: String)
    }

    operator fun get(onTextChangeListener: OnTextChangeListener): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                onTextChangeListener.onTextChanged(s.toString().isEmpty(), s.toString())
            }
        }
    }

    fun setHint(textInputEditText: TextInputEditText, hint: String) {
        if (textInputEditText.text.isNullOrBlank())
            textInputEditText.hint = hint
        else
            textInputEditText.hint = ""
    }
}
