package com.deviprasaddayal.depiciat.listeners

interface OnBatteryStatusReceivedListener {
    fun onStatusChanged(level: Float)

    fun onStatusLow(level: Float)
}
