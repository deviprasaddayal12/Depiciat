package com.deviprasaddayal.depiciat.fragments

import android.view.View

abstract class AddBaseFragment: BaseFragment() {

    abstract fun viewPreviousDetails()

    abstract fun saveAndProceedToNext()
}