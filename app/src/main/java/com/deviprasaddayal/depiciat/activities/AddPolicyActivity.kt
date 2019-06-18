package com.deviprasaddayal.depiciat.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deviprasaddayal.depiciat.R
import com.deviprasaddayal.depiciat.adapters.ListPolicyAdapter
import com.deviprasaddayal.depiciat.adapters.StateFlowAdapter
import com.deviprasaddayal.depiciat.listeners.OnFileActionListener
import com.deviprasaddayal.depiciat.managers.FileManager
import com.deviprasaddayal.depiciat.models.StateFlowModel
import com.deviprasaddayal.depiciat.utils.StateFlowUtils
import com.deviprasaddayal.depiciat.utils.Utils
import java.io.File

class AddPolicyActivity : BaseActivity(), OnFileActionListener {
    val TAG = AddPolicyActivity::class.java.canonicalName

    lateinit var toolbar: Toolbar

    lateinit var recyclerView: RecyclerView
    lateinit var stateFlowAdapter: StateFlowAdapter
    lateinit var stateFlowModels: ArrayList<StateFlowModel>

    lateinit var fileManager: FileManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fileManager = FileManager(this, this)
        setContentView(R.layout.activity_add_policy)
    }

    override fun setUpToolbar() {
        toolbar = findViewById(R.id.toolbar)
        toolbar.title = "Hello, Dayal"
        toolbar.subtitle = "Please fill the details to create new policy"
    }

    override fun initialiseViews() {

    }

    override fun initialiseListeners() {

    }

    override fun setDataToViews() {

    }

    override fun setUpRecycler() {
        stateFlowModels = StateFlowUtils.getStateList(
                arrayOf("Bearer Details",
                        "Policy Details",
                        "Nominee Details",
                        "Bank Details"))

        recyclerView = findViewById(R.id.rv_stateAddPolicy)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        stateFlowAdapter = StateFlowAdapter(this, stateFlowModels)

        recyclerView.adapter = stateFlowAdapter
    }

    override fun onClick(v: View?) {

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            FileManager.Requests.CAMERA -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    fileManager.gotoCamera()
                else
                    Utils.loge(TAG, "Camera permission denied.");
            }
            FileManager.Requests.GALLERY -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    fileManager.gotoGallery()
                else
                    Utils.loge(TAG, "Gallery permission denied.");
            }
            FileManager.Requests.BROWSER -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    fileManager.gotoFileBrowser()
                else
                    Utils.loge(TAG, "Browser permission denied.");
            }
            else -> Utils.loge(TAG, "Permission result is out of scope: ${permissions[0]}")
        }
    }

    override fun onFilePathCreatedForCamera(imagePath: String, imageFile: File) {
        Utils.logi(TAG, "onFilePathCreatedForCamera: $imagePath")
    }

    override fun onFileAddRequest() {
        Utils.logi(TAG, "onFileAddRequest: ")
    }

    override fun onFileViewRequest(position: Int) {
        Utils.logi(TAG, "onFileViewRequest: ")
    }

    override fun onFileDeleted(position: Int, removedFileName: String) {
        Utils.logi(TAG, "onFileDeleted: $removedFileName")
    }
}