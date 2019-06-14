package com.deviprasaddayal.depiciat.activities

import android.os.Bundle
import android.os.PersistableBundle
import com.deviprasaddayal.depiciat.R

class AddPolicyActivity : BaseActivity() {
    val TAG = AddPolicyActivity::class.java.canonicalName

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        setContentView(R.layout.activity_add_policy)
    }

    override fun setUpToolbar() {

    }

    override fun initialiseViews() {

    }

    override fun initialiseListeners() {

    }
}