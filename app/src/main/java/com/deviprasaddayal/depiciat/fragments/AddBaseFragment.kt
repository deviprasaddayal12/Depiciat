package com.deviprasaddayal.depiciat.fragments

import android.view.View
import com.deviprasaddayal.depiciat.listeners.OnTextChangeListener
import com.google.android.material.textfield.TextInputEditText

abstract class AddBaseFragment: BaseFragment(), OnTextChangeListener {
    override fun onTextChanged(textInputEditText: TextInputEditText, isEmpty: Boolean, text: String) {
        TODO("created new callback: not implemented")
    }
}