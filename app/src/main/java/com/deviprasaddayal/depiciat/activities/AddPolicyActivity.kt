package com.deviprasaddayal.depiciat.activities

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.viewpager.widget.ViewPager

import com.deviprasaddayal.depiciat.R
import com.deviprasaddayal.depiciat.adapters.ViewPagerAdapter
import com.deviprasaddayal.depiciat.fragments.AddBankFragment
import com.deviprasaddayal.depiciat.fragments.AddBearerFragment
import com.deviprasaddayal.depiciat.fragments.AddNomineeFragment
import com.deviprasaddayal.depiciat.fragments.AddPolicyFragment
import com.deviprasaddayal.depiciat.listeners.OnFileActionListener
import com.deviprasaddayal.depiciat.managers.ContentManager
import com.deviprasaddayal.depiciat.managers.FileManager
import com.deviprasaddayal.depiciat.utils.LogUtils

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

        viewPagerAdapter.addFragment(AddBearerFragment(), getAppString(R.string.name_bearer))
        viewPagerAdapter.addFragment(AddNomineeFragment(), getAppString(R.string.name_nominee))
        viewPagerAdapter.addFragment(AddPolicyFragment(), getAppString(R.string.name_policy))
        viewPagerAdapter.addFragment(AddBankFragment(), getAppString(R.string.name_bank))

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
        val descriptions = getDescriptionForPosition(position)
        findViewById<TextView>(R.id.tv_addPolicyTitle).text = descriptions[0]
        findViewById<TextView>(R.id.tv_addPolicySubTitle).text = descriptions[1]
//        toolbar.title = descriptions[0]
//        toolbar.subtitle = descriptions[1
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_goPrevious -> {

            }
            R.id.btn_goNext -> {

            }
        }
    }

    private fun getDescriptionForPosition(position: Int) : Array<String> {
        when (position) {
            0 -> return arrayOf(getAppString(R.string.ttl_details_bearer), getAppString(R.string.sttl_details_bearer))
            1 -> return arrayOf(getAppString(R.string.ttl_details_nominee), getAppString(R.string.sttl_details_nominee))
            2 -> return arrayOf(getAppString(R.string.ttl_details_policy), getAppString(R.string.sttl_details_policy))
            3 -> return arrayOf(getAppString(R.string.ttl_details_bank), getAppString(R.string.sttl_details_bank))
            else -> return arrayOf(getAppString(R.string.ttl_app), getAppString(R.string.sttl_app))
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