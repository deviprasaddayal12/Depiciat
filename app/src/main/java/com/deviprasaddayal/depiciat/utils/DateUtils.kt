package com.deviprasaddayal.depiciat.utils

import android.util.Log

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateUtils {
    val TAG = DateUtils::class.java.canonicalName

    val FORMAT_DATE_N_TIME_12HRS = "dd/MM/yyyy hh:mm:ss aa"
    val FORMAT_DATE_N_TIME_24HRS = "dd/MM/yyyy HH:mm:ss"
    val FORMAT_DATE_N_TIME_ISO = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
    val FORMAT_DATE_N_TIME_24HRS_UTC = "dd/MM/yyyy HH:mm:ss"
    val FORMAT_DATE_ONLY = "dd/MM/yyyy"
    val FORMAT_TIME_ONLY_12HRS = "hh:mm:ss aa"
    val FORMAT_TIME_ONLY_12HRS_NO_MERIDIAN = "hh:mm:ss"
    val FORMAT_TIME_ONLY_24HRS = "HH:mm:ss"
    val FORMAT_DATE_ONLY_DIESEL = "yyyy-MM-dd"
    val FORMAT_DATE_N_TIME_DIESEL = "$FORMAT_DATE_ONLY_DIESEL $FORMAT_TIME_ONLY_24HRS"

    val imageTimeStamp: String
        get() {
            val now = Date()
            var date = getDateInString(now)
            date = date.replace("/", "")
            var time = getTimeInStringWithoutMeridian(now)
            time = time.replace(":", "")

            val timeStamp = String.format("%s_%s", date, time)
            Log.i(TAG, "getImageTimeStamp: $timeStamp")
            return timeStamp + "_"
        }

    val currentTime: Long
        get() = Date().time

    fun getUTCTime(time: Long): Long {
        try {
            val sdf = SimpleDateFormat()
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val date = sdf.format(Date(time))
            val sdf1 = SimpleDateFormat()
            val d = sdf1.parse(date)
            return d.time
        } catch (e: Exception) {
            Log.e(TAG, "getUTCTime: " + e.message)
            e.printStackTrace()
        }

        return 0
    }

    /**
     * This function is used to convert date into string in the specified format
     *
     * @param millis date to be converted
     * @return @date in string
     */
    fun getDateStringFromMillis(millis: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        return getDateInString(calendar.time)
    }

    fun getDateInString(date: Date): String {
        val simpleDateFormat = SimpleDateFormat(FORMAT_DATE_ONLY, Locale.getDefault())
        return simpleDateFormat.format(date)
    }

    fun getDateForDiesel(date: Date): String {
        val simpleDateFormat = SimpleDateFormat(FORMAT_DATE_ONLY_DIESEL, Locale.getDefault())
        return simpleDateFormat.format(date)
    }

    fun getTimeInString(date: Date): String {
        val simpleTimeFormat = SimpleDateFormat(FORMAT_TIME_ONLY_12HRS, Locale.getDefault())
        return simpleTimeFormat.format(date)
    }

    fun getTimeInStringWithoutMeridian(date: Date): String {
        val simpleTimeFormat = SimpleDateFormat(FORMAT_TIME_ONLY_12HRS_NO_MERIDIAN, Locale.getDefault())
        return simpleTimeFormat.format(date)
    }

    fun getDateFromString(date: String?): Date? {
        try {
            val simpleDateFormat = SimpleDateFormat(FORMAT_DATE_ONLY, Locale.getDefault())
            return simpleDateFormat.parse(date)
        } catch (e: Exception) {
            Log.e(TAG, "getDateFromString: " + e.message)
            return null
        }

    }

    fun getDateInPattern(date: String, pattern: String): Date? {
        try {
            val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
            return simpleDateFormat.parse(date)
        } catch (e: Exception) {
            Log.e(TAG, "getDateInPattern: " + e.message)
            return null
        }

    }

    fun getDateInPattern(date: String, fromPattern: String, toPattern: String): String? {
        try {
            val inputFormat = SimpleDateFormat(fromPattern, Locale.getDefault())
            val parsedDate = inputFormat.parse(date)
            val outputFormat = SimpleDateFormat(toPattern, Locale.getDefault())
            return outputFormat.format(parsedDate)
        } catch (e: Exception) {
            Log.e(TAG, "getDateInPattern: " + e.message)
            return null
        }

    }

    fun getDieselDateTime(date: Date): String {
        val simpleDateFormat = SimpleDateFormat(FORMAT_DATE_N_TIME_DIESEL, Locale.getDefault())
        return simpleDateFormat.format(date)
    }

    fun getDateForDieselDateTime(date: String): Date {
        val simpleDateFormat = SimpleDateFormat(FORMAT_DATE_N_TIME_DIESEL, Locale.getDefault())
        try {
            return simpleDateFormat.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            return Date()
        }

    }

    fun getPrevious24HrsDate(addNoOfDaysToToday: Int): String {
        val simpleDateFormat = SimpleDateFormat(FORMAT_DATE_ONLY_DIESEL, Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)

        calendar.add(Calendar.DAY_OF_MONTH, addNoOfDaysToToday)
        val sSelectedDate = simpleDateFormat.format(calendar.time)
        Log.e(TAG, "getPrevious24HrsDate: sSelectedDate$sSelectedDate")

        return sSelectedDate
    }

    fun getISO8601Parsed(dateInISO8601: String?): String {
        var formattedDate = ""
        if (dateInISO8601 == null || dateInISO8601.isEmpty()) {
            return ""
        }
        try {
            val dateFormat = SimpleDateFormat(FORMAT_DATE_N_TIME_ISO, Locale.getDefault())
            val date = dateFormat.parse(dateInISO8601)
            val simpleDateFormat = SimpleDateFormat(FORMAT_DATE_N_TIME_12HRS, Locale.getDefault())
            formattedDate = simpleDateFormat.format(date)
            return formattedDate
        } catch (e: ParseException) {
            Log.e(TAG, "getISO8601Parsed: " + e.message)
            return formattedDate
        }

    }

    fun getCalendarInstance(previousDate: String?): Calendar {
        val calendar = Calendar.getInstance()
        try {
            val date = getDateFromString(previousDate)
            calendar.time = date
        } catch (e: Exception) {
            Log.e(TAG, "getCalendarInstance: " + e.message)
        }

        return calendar
    }
}
