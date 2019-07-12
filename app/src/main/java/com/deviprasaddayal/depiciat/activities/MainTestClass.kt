package com.deviprasaddayal.depiciat.activities

import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import com.deviprasaddayal.depiciat.R

class MainTestClass : BaseActivity() {
    companion object {
        val TAG = MainTestClass::class.java.canonicalName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_test_class)
    }
}