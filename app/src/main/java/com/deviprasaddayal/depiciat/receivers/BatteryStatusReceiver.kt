package com.deviprasaddayal.depiciat.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.util.Log
import com.deviprasaddayal.depiciat.listeners.OnBatteryStatusReceivedListener

class BatteryStatusReceiver(private val onBatteryStatusReceivedListener: OnBatteryStatusReceivedListener?) :
    BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != null && intent.action == Intent.ACTION_BATTERY_LOW) {
            if (onBatteryStatusReceivedListener != null)
                onBatteryStatusReceivedListener!!.onStatusLow(getBatteryPercentage(intent))
        }
        if (intent.action != null && intent.action == Intent.ACTION_BATTERY_CHANGED) {
            if (onBatteryStatusReceivedListener != null)
                onBatteryStatusReceivedListener!!.onStatusChanged(getBatteryPercentage(intent))
        }
    }

    private fun getBatteryPercentage(intent: Intent): Float {
        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)

        val batteryPct = level / scale.toFloat()

        Log.i(TAG, "getBatteryPercentage: " + batteryPct * 100)
        return batteryPct * 100
    }

    companion object {

        val TAG = BatteryStatusReceiver::class.java.simpleName
    }
}
