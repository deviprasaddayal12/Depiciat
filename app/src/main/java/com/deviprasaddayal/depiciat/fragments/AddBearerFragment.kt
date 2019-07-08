package com.deviprasaddayal.depiciat.fragments

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.deviprasaddayal.depiciat.R
import com.deviprasaddayal.depiciat.listeners.AppTextWatcher

import com.google.android.material.textfield.TextInputEditText

class AddBearerFragment: AddBaseFragment() {
    val TAG = AddBearerFragment::class.java.canonicalName

    lateinit var tietBearerName: TextInputEditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_bearer, container, false)
    }

    override fun initialiseViews(view: View) {
        tietBearerName = view.findViewById(R.id.tiet_bearerName)
    }

    override fun initialiseListeners(view: View) {
        tietBearerName.addTextChangedListener(AppTextWatcher(tietBearerName, this))
    }

    override fun onTextChanged(textInputEditText: TextInputEditText, isEmpty: Boolean, text: String) {

    }
}