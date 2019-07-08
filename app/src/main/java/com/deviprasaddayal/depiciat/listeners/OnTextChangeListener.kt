package com.deviprasaddayal.depiciat.listeners

import com.google.android.material.textfield.TextInputEditText

interface OnTextChangeListener {
    fun onTextChanged(textInputEditText: TextInputEditText, isEmpty: Boolean, text: String)
}