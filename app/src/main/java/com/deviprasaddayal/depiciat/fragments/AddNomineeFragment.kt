package com.deviprasaddayal.depiciat.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.deviprasaddayal.depiciat.R

class AddNomineeFragment: AddBaseFragment() {
    val TAG = AddNomineeFragment::class.java.canonicalName

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_nominee, container, false)
    }

    override fun initialiseViews(view: View) {

    }

    override fun initialiseListeners(view: View) {

    }

    override fun viewPreviousDetails() {

    }

    override fun saveAndProceedToNext() {

    }
}