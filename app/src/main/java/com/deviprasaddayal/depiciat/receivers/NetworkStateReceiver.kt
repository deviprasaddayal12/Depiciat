package com.deviprasaddayal.depiciat.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import com.deviprasaddayal.depiciat.constants.Constants
import com.deviprasaddayal.depiciat.listeners.OnNetworkStateChangeListener

class NetworkStateReceiver : BroadcastReceiver {
    companion object {
        val TAG = NetworkStateReceiver::class.java.canonicalName
    }

    private var onNetworkStateChangeListener: OnNetworkStateChangeListener? = null

    constructor() {}

    constructor(onNetworkStateChangeListener: OnNetworkStateChangeListener) {
        this.onNetworkStateChangeListener = onNetworkStateChangeListener
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != null && intent.action == Constants.IntentFilter.NETWORK_STATE_CHANGE) {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val online =
                connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo.isConnected

            Log.e(TAG, "onReceive: " + connectivityManager.activeNetworkInfo)

            if (onNetworkStateChangeListener != null)
                onNetworkStateChangeListener!!.onNetworkStateChanged(online)
        }
    }
}
