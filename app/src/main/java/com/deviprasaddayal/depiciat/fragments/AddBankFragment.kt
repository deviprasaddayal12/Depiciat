package com.deviprasaddayal.depiciat.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.deviprasaddayal.depiciat.R

class AddBankFragment: AddBaseFragment() {
    val TAG = AddBankFragment::class.java.canonicalName

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_bank, container, false)
    }

    override fun initialiseViews(view: View) {

    }

    override fun initialiseListeners(view: View) {

    }
}