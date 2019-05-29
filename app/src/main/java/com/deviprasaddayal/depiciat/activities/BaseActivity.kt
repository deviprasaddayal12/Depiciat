package com.deviprasaddayal.depiciat.activities

import android.view.View
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity(), View.OnClickListener{

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)

        setUpToolbar()
        initialiseViews()
        initialiseListeners()

        setUpViewPager()
        setUpRecycler()
    }

    abstract fun setUpToolbar()

    abstract fun initialiseViews()

    abstract fun initialiseListeners()

    open fun setUpViewPager(){}

    open fun setUpRecycler(){}

    override fun onClick(v: View?) {
        // to be implemented in extending classes
    }
}