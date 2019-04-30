package com.deviprasaddayal.depiciat.activities

import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity(){

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
}