package com.deviprasaddayal.depiciat.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.deviprasaddayal.depiciat.R
import com.deviprasaddayal.depiciat.adapters.ListPolicyAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListPolicyActivity : BaseActivity(), View.OnClickListener {
    val TAG = ListPolicyActivity::class.java.canonicalName

    private val REQUEST_ADD_NEW_POLICY = 123

    lateinit var recyclerView: RecyclerView
    lateinit var adapterPolicyList: ListPolicyAdapter
    lateinit var fabNewPolicy: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_policy)
    }

    override fun setUpToolbar() {

    }

    override fun initialiseViews() {
        recyclerView = findViewById(R.id.recycler_list_policies)
        fabNewPolicy = findViewById(R.id.fab_add_new_policy)
    }

    override fun initialiseListeners() {
        fabNewPolicy.setOnClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_ADD_NEW_POLICY && resultCode == Activity.RESULT_OK) {

        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fab_add_new_policy -> gotoCreateNewPolicy()
        }
    }

    private fun gotoCreateNewPolicy() {
        Toast.makeText(this, "Implementing soon...", Toast.LENGTH_SHORT).show()
        val addNewPolicyIntent = Intent(this, AddNewPolicy::class.java)
        startActivityForResult(addNewPolicyIntent, REQUEST_ADD_NEW_POLICY)
    }
}
