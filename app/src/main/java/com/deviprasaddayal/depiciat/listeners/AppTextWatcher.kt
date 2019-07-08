package com.deviprasaddayal.depiciat.listeners

import android.text.Editable
import android.text.TextWatcher

import com.google.android.material.textfield.TextInputEditText

class AppTextWatcher (var textInputEditText: TextInputEditText, var onTextChangeListener: OnTextChangeListener) : TextWatcher {

    override fun afterTextChanged(s: Editable?) {
        TODO("created new callback: not implemented")
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        TODO("created new callback: not implemented")
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        onTextChangeListener.onTextChanged(textInputEditText, textInputEditText.text.isNullOrEmpty(), textInputEditText.text.toString())
    }
}