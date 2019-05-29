package com.deviprasaddayal.depiciat.activities

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.deviprasaddayal.depiciat.R

class AddNewPolicy : BaseActivity() {
    val TAG = AddNewPolicy::class.java.canonicalName

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        setContentView(R.layout.activity_add_new_policy)
    }

    override fun setUpToolbar() {

    }

    override fun initialiseViews() {

    }

    override fun initialiseListeners() {

    }
}