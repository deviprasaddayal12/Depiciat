package com.deviprasaddayal.depiciat.activities

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.TextureView
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.deviprasaddayal.depiciat.R
import com.deviprasaddayal.depiciat.adapters.StateFlowAdapter
import com.deviprasaddayal.depiciat.adapters.ViewPagerAdapter
import com.deviprasaddayal.depiciat.fragments.AddBankFragment
import com.deviprasaddayal.depiciat.fragments.AddBearerFragment
import com.deviprasaddayal.depiciat.fragments.AddNomineeFragment
import com.deviprasaddayal.depiciat.fragments.AddPolicyFragment
import com.deviprasaddayal.depiciat.listeners.OnFileActionListener
import com.deviprasaddayal.depiciat.managers.ContentManager
import com.deviprasaddayal.depiciat.managers.FileManager
import com.deviprasaddayal.depiciat.models.StateFlowModel
import com.deviprasaddayal.depiciat.utils.LogUtils
import com.deviprasaddayal.depiciat.utils.StateFlowUtils
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import java.io.File

class AddPolicyActivity : BaseActivity(), OnFileActionListener, ViewPager.OnPageChangeListener {
    companion object {
        val TAG = AddPolicyActivity::class.java.canonicalName
    }

    lateinit var tabLayout : TabLayout
    lateinit var viewPager : ViewPager
    lateinit var viewPagerAdapter : ViewPagerAdapter

    lateinit var btnPrevious : MaterialButton
    lateinit var btnNext : MaterialButton

    lateinit var fileManager : FileManager
    lateinit var contentManager : ContentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_policy)

        fileManager = FileManager(this, this)
        contentManager = ContentManager(findViewById(R.id.root))
    }

    override fun initialiseViews() {
        btnPrevious = findViewById(R.id.btn_goPrevious)
        btnNext = findViewById(R.id.btn_goNext)
    }

    override fun initialiseListeners() {
        btnPrevious.setOnClickListener(this)
        btnNext.setOnClickListener(this)
    }

    override fun setUpViewPager() {
        tabLayout = findViewById(R.id.tl_add_policy)
        viewPager = findViewById(R.id.vp_container)

        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)

        viewPagerAdapter.addFragment(AddBearerFragment(), "Bearer")
        viewPagerAdapter.addFragment(AddNomineeFragment(), "Nominee")
        viewPagerAdapter.addFragment(AddPolicyFragment(), "Policy")
        viewPagerAdapter.addFragment(AddBankFragment(), "Bank")

        viewPager.adapter = viewPagerAdapter
        tabLayout.setupWithViewPager(viewPager)

        viewPager.addOnPageChangeListener(this)
    }

    override fun onStart() {
        super.onStart()
        contentManager.onLoadingFinished()
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        findViewById<TextView>(R.id.tv_addPolicyTitle).text = getDescriptionForPosition(position)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_goPrevious -> {

            }
            R.id.btn_goNext -> {

            }
        }
    }

    private fun getDescriptionForPosition(position: Int) : String {
        when (position) {
            0 -> return "Bearer Details"
            1 -> return "Nominee Details"
            2 -> return "Policy Details"
            3 -> return "Bank Details"
            else -> return "Fragment Title"
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            FileManager.Requests.CAMERA -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    fileManager.gotoCamera()
                else
                    LogUtils.loge(TAG, "Camera permission denied.");
            }
            FileManager.Requests.GALLERY -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    fileManager.gotoGallery()
                else
                    LogUtils.loge(TAG, "Gallery permission denied.");
            }
            FileManager.Requests.BROWSER -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    fileManager.gotoFileBrowser()
                else
                    LogUtils.loge(TAG, "Browser permission denied.");
            }
            else -> LogUtils.loge(TAG, "Permission result is out of scope: ${permissions[0]}")
        }
    }

    override fun onFilePathCreatedForCamera(imagePath: String, imageFile: File) {
        LogUtils.logi(TAG, "onFilePathCreatedForCamera: $imagePath")
    }

    override fun onFileAddRequest() {
        LogUtils.logi(TAG, "onFileAddRequest: ")
    }

    override fun onFileViewRequest(position: Int) {
        LogUtils.logi(TAG, "onFileViewRequest: ")
    }

    override fun onFileDeleted(position: Int, removedFileName: String) {
        LogUtils.logi(TAG, "onFileDeleted: $removedFileName")
    }
}