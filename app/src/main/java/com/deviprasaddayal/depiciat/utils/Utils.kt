package com.deviprasaddayal.depiciat.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.text.InputFilter
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.deviprasaddayal.depiciat.R

import java.util.ArrayList

/**
 * Created by Atul on 5/4/18.
 */

object Utils {
    val TAG = Utils::class.java.canonicalName

    val RETURN_EMPTY_IF_NULL = 0
    val RETURN_HYPHEN_IF_NULL = 1
    val REPLACE_UNDERSCORE_WITH_SPACE = 2
    val REPLACE_UNDERSCORE_WITH_HYPHEN = 3
    val RETURN_SPACE_IF_EMPTY = 4
    val RETURN_EMPTY_IF_HYPHEN = 5

    private val digitsBeforeDecimal = 8
    private val digitsAfterDecimal = 3
    private val decimal = "."

    var isNetworkAvailable = true

    /**
     * This callback generates an input filter for quantity edittext which, as a result,
     * will restrict the maximum entry into the field to 99999999.999,
     * i.e. 8 decimal digits before decimal and 3 decimal digits after the decimal
     *
     * @return inputFilter for the field with specified restriction
     */
    val floatMaxThreshold: InputFilter
        get() = InputFilter { charSequence, i, i1, spanned, i2, i3 ->
            if (spanned.length == 1 && spanned.toString().contains(decimal)) {
                "0.$charSequence"
            } else if (spanned.toString().contains(decimal)) {
                val splitAtDecimalStrings =
                    spanned.toString().split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (splitAtDecimalStrings.size <= 1)
                    charSequence
                else if (splitAtDecimalStrings[1].length < digitsAfterDecimal)
                    charSequence
                else
                    ""
            } else {
                if (spanned.length < digitsBeforeDecimal)
                    charSequence
                else if (spanned.length == digitsBeforeDecimal && charSequence.toString().contains(decimal))
                    charSequence
                else
                    ""
            }
        }

    val maxFloatFilters: Array<InputFilter>
        get() {
            val floatMaxThresholdFilter = floatMaxThreshold
            val floatMaxLengthFilter = InputFilter.LengthFilter(10)
            return arrayOf(floatMaxThresholdFilter, floatMaxLengthFilter)
        }

    fun getBackRunnable(activity: Activity): Runnable {
        return Runnable { activity.onBackPressed() }
    }

    fun isNetworkAvailable(context: Context?): Boolean {
        if (context != null) {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null)
                isNetworkAvailable = activeNetworkInfo.isAvailable
        }
        return isNetworkAvailable

    }

    fun parseInt(value: String): Int {
        try {
            return Integer.parseInt(value)
        } catch (e: Exception) {
            e.printStackTrace()
            return 0
        }
    }

    fun parseFloat(value: String): Float {
        try {
            return java.lang.Float.parseFloat(value)
        } catch (e: Exception) {
            e.printStackTrace()
            return 0.toFloat()
        }
    }

    fun parseLong(value: String): Long {
        try {
            return java.lang.Long.parseLong(value)
        } catch (e: Exception) {
            e.printStackTrace()
            return 0.toLong()
        }
    }

    fun parseDouble(value: String): Double {
        try {
            return java.lang.Double.parseDouble(value)
        } catch (e: Exception) {
            e.printStackTrace()
            return 0.toDouble()
        }
    }

    fun toValidString(type: Int, toFilter: ArrayList<String>): ArrayList<String> {
        val filteredStrings = ArrayList<String>()
        for (string in toFilter) {
            filteredStrings.add(toValidString(type, string))
        }

        return filteredStrings
    }

    fun toValidString(type: Int, toFilter: String?): String {
        var toBeFiltered = toFilter
        toBeFiltered = if (toBeFiltered == null || toBeFiltered == "null") "" else toBeFiltered

        when (type) {
            RETURN_EMPTY_IF_NULL -> return toBeFiltered
            RETURN_HYPHEN_IF_NULL -> return if (toBeFiltered.isEmpty()) "-" else toBeFiltered
            REPLACE_UNDERSCORE_WITH_SPACE -> return toBeFiltered.replace('_', ' ')
            REPLACE_UNDERSCORE_WITH_HYPHEN -> return toBeFiltered.replace('_', '-')
            RETURN_SPACE_IF_EMPTY -> return if (toBeFiltered.isEmpty()) " " else toBeFiltered
            RETURN_EMPTY_IF_HYPHEN -> return if (toBeFiltered.contains("-") && toBeFiltered.length == 1) "" else toBeFiltered
            else -> return toBeFiltered
        }
    }

    fun toHyphen(toHyphen: String): String {
        return toValidString(RETURN_HYPHEN_IF_NULL, toHyphen)
    }

    fun toBoolean(value: Int): Boolean {
        return value == 1
    }

    fun isGreaterInDoubles(s1: String, s2: String): Boolean {
        return isGreaterInDoubles(Utils.parseDouble(s1), Utils.parseDouble(s2))
    }

    fun isGreaterInDoubles(d1: Double, d2: Double): Boolean {
        return d1 > d2
    }

    fun parseVersionInt(versionCode: Array<String>, position: Int): Int {
        val code: String
        if (position >= versionCode.size)
            code = "0"
        else
            code = versionCode[position]

        return parseInt(code)
    }

    @JvmOverloads
    fun getVersionCode(context: Context, versionFromServer: String = "0.0"): String {
        var versionFromDevice = versionFromServer
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            versionFromDevice = packageInfo.versionName
            //            Log.e(TAG, "isUpdateAvailable.versionFromDevice: " + versionFromDevice);
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return versionFromDevice
    }

    fun isUpdateAvailable(context: Context, versionFromServer: String): Boolean {
        var isUpdateAvailable = false
        val versionFromDevice = getVersionCode(context, versionFromServer)
        //        Log.e(TAG, "isUpdateAvailable.versionFromServer: " + versionFromServer);

        val serverCodes = versionFromServer.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val deviceCodes = versionFromDevice.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        if (serverCodes.size > 0 /*&& deviceCodes.length > 0*/) {
            isUpdateAvailable = Utils.parseVersionInt(serverCodes, 0) > Utils.parseVersionInt(deviceCodes, 0)
            if (!isUpdateAvailable && serverCodes.size > 1) {
                isUpdateAvailable = Utils.parseVersionInt(serverCodes, 1) > Utils.parseVersionInt(deviceCodes, 1)
                if (!isUpdateAvailable && serverCodes.size > 2 /*&& deviceCodes.length > 2*/) {
                    isUpdateAvailable = Utils.parseVersionInt(serverCodes, 2) > Utils.parseVersionInt(deviceCodes, 2)
                }
            }
        }

        return isUpdateAvailable
    }
}
