package com.deviprasaddayal.depiciat.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deviprasaddayal.depiciat.R
import com.deviprasaddayal.depiciat.adapters.ListPolicyAdapter
import com.deviprasaddayal.depiciat.models.ItemPolicyModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListPolicyActivity : BaseActivity() {
    val TAG = ListPolicyActivity::class.java.canonicalName

    private val REQUEST_ADD_NEW_POLICY = 123

    lateinit var toolbar: Toolbar

    lateinit var recyclerView: RecyclerView
    lateinit var adapterPolicyList: ListPolicyAdapter
    lateinit var policyModels: ArrayList<ItemPolicyModel>

    lateinit var fabNewPolicy: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_policy)
    }

    override fun setUpToolbar() {
        toolbar = findViewById(R.id.toolbar)
        toolbar.title = "Hello, Dayal"
    }

    override fun initialiseViews() {
        fabNewPolicy = findViewById(R.id.fab_addNewPolicy)
    }

    override fun initialiseListeners() {
        fabNewPolicy.setOnClickListener(this)
    }

    override fun setDataToViews() {

    }

    override fun setUpRecycler() {
        policyModels = ArrayList()

        recyclerView = findViewById(R.id.rv_listPolicies)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapterPolicyList = ListPolicyAdapter(this, policyModels)

        recyclerView.adapter = adapterPolicyList
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_ADD_NEW_POLICY && resultCode == Activity.RESULT_OK) {

        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_addNewPolicy -> gotoCreateNewPolicy()
        }
    }

    private fun gotoCreateNewPolicy() {
        val addNewPolicyIntent = Intent(this, AddPolicyActivity::class.java)
        startActivityForResult(addNewPolicyIntent, REQUEST_ADD_NEW_POLICY)
    }
}
