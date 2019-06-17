package com.deviprasaddayal.depiciat.listeners

import java.util.Calendar

interface OnDateSelectedListener {
    fun onDateSelected(dateAsString: String, calendar: Calendar)
}
