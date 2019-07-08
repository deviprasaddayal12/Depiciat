package com.deviprasaddayal.depiciat

import android.app.Application
import android.content.res.Resources
import com.google.firebase.FirebaseApp

class DecipiatApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
    }
}