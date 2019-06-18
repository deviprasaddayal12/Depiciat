package com.deviprasaddayal.depiciat.fragments

import android.os.Bundle
import android.view.View
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
}