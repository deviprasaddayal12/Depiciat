package com.deviprasaddayal.depiciat.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment

abstract class BaseFragment: Fragment(), View.OnClickListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar(view)
        initialiseViews(view)
        initialiseListeners(view)

        setUpViewPager(view)
        setUpRecycler(view)
    }

    open fun setUpToolbar(view: View){}

    abstract fun initialiseViews(view: View)

    abstract fun initialiseListeners(view: View)

    open fun setUpViewPager(view: View){}

    open fun setUpRecycler(view: View){}

    override fun onClick(v: View?) {
        // to be implemented in extending classes
    }

    fun hideSoftKeyboard() {
        val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity?.currentFocus!!.windowToken, 0)
    }

    fun showSoftKeyBoard(editText: EditText) {
        val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInputFromWindow(editText.applicationWindowToken, InputMethodManager.SHOW_FORCED, 0)
    }
}