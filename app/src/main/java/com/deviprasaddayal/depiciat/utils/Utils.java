package com.deviprasaddayal.depiciat.utils;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.deviprasaddayal.depiciat.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Atul on 5/4/18.
 */

public class Utils {
    public static final String TAG = Utils.class.getCanonicalName();

    private static final int digitsBeforeDecimal = 8, digitsAfterDecimal = 3;
    private static final String decimal = ".";

    public static Runnable getBackRunnable(final Activity activity){
        return new Runnable() {
            @Override
            public void run() {
                activity.onBackPressed();
            }
        };
    }

    public static void handleError(Context context/*, VolleyError error*/) {
        String errorMessage = "An unknown error occurred.";
        int errorDrawable = R.drawable.new_info_wrong_icon;

        if (!Utils.isNetworkAvailable(context)) {
            errorMessage = "We couldn't connect to internet. Please check your connection and retry.";
            errorDrawable = R.drawable.new_info_network_icon;

        } /*else if (error instanceof AuthFailureError) {
            errorMessage = "We have failed to authenticate. Please login again and retry.";

        } else if (error instanceof NoConnectionError) {
            errorMessage = "We couldn't connect to server. Please try after sometime.";
            errorDrawable = R.drawable.new_info_wrong_icon;

        } else if (error instanceof NetworkError) {
            errorMessage = "Our server might be running down. Please retry after sometime.";

        } else if (error instanceof ParseError) {
            errorMessage = "We couldn't read response.";

        } else if (error instanceof ServerError) {
            errorMessage = "You might have entered wrong or forgot to add some information. Please check and retry.";

        } else if (error instanceof TimeoutError) {
            errorMessage = "We're facing a very slow network condition. Please check your connection and retry.";
            errorDrawable = R.drawable.new_info_sorry_icon;
        }*/

        handleError(context, errorMessage, errorDrawable);
    }

    public static void handleError(Context context, String error, int errorDrawable) {
        DialogUtils.showFailureDialog(context, error, errorDrawable, null);
    }

    public static void handleError(Context context, String error) {
        DialogUtils.showFailureDialog(context, error);
    }

    /**
     * This callback generates an input filter for quantity edittext which, as a result,
     * will restrict the maximum entry into the field to 99999999.999,
     * i.e. 8 decimal digits before decimal and 3 decimal digits after the decimal
     *
     * @return inputFilter for the field with specified restriction
     */
    public static InputFilter getQuantityThresholdFilter() {
        return new InputFilter() {
            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                if (spanned.length() == 1 && spanned.toString().contains(decimal)) {
                    return "0." + charSequence.toString();
                } else if (spanned.toString().contains(decimal)) {
                    String[] splitAtDecimalStrings = (spanned.toString()).split("\\.");
                    if (splitAtDecimalStrings.length <= 1)
                        return charSequence;
                    else if (splitAtDecimalStrings[1].length() < digitsAfterDecimal)
                        return charSequence;
                    else
                        return "";
                } else {
                    if (spanned.length() < digitsBeforeDecimal)
                        return charSequence;
                    else if (spanned.length() == digitsBeforeDecimal && charSequence.toString().contains(decimal))
                        return charSequence;
                    else
                        return "";
                }
            }
        };
    }

    public static InputFilter[] getQuantityFilters() {
        InputFilter quantityThresholdFilter = getQuantityThresholdFilter();
        InputFilter quantityLengthFilter = new InputFilter.LengthFilter(10);
        return new InputFilter[]{quantityThresholdFilter, quantityLengthFilter};
    }

    public static void hideSoftKeyboard(Context context) {
        if (context != null) {
            if (((Activity) context).getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    public static void showSoftKeyBoard(Context context, EditText etSearch) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(etSearch.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
    }

    public static boolean isNetworkAvailable(final Context context) {
        boolean status = false;
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null)
                status = activeNetworkInfo.isAvailable();
        }
        return status;

    }

    public static String convertMilliSecondsToDate(long milliseconds) {
        String dateString = new SimpleDateFormat("dd MMM yyyy").format(new Date(milliseconds));
        return dateString;
    }

    public static String checkNullValues(String msg) {
        if (msg.equalsIgnoreCase("null"))
            return "";
        else
            return msg;
    }

    public static long convertDateToMilliseconds(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dateObj = sdf.parse(date);
            long millis = dateObj.getTime();
            return millis;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static float parseFloat(String value) {
        try {
            return Float.parseFloat(value);
        } catch (Exception e) {
            e.printStackTrace();
            return (float) 0;
        }
    }

    public static long parseLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            e.printStackTrace();
            return (long) 0;
        }
    }

    public static double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            e.printStackTrace();
            return (double) 0;
        }
    }

    /**
     * This callback is used to block invalid string like: null, "null" or simply nothing
     *
     * @param stringToBeModified the string as parameter to be checked for validity
     * @return returns the same as parameter if it is valid else a hyphen
     */
    public static String getValidString(String stringToBeModified) {
        if (stringToBeModified == null || stringToBeModified.equals("null") || stringToBeModified.equals("")) {
            return "-";
        } else
            return stringToBeModified;
    }

    /**
     * This callback is used to block invalid string like: null and "null"
     *
     * @param stringToBeModified the string as parameter to be checked for validity
     * @return returns the same as parameter if it is valid else nothing, i.e. a blank string and avoids hyphen
     */
    public static String getValidStringWithoutHyphen(String stringToBeModified) {
        if (stringToBeModified == null || stringToBeModified.equals("null")) {
            return "";
        } else
            return stringToBeModified;
    }

    /**
     * This callback is used to block invalid string like: null and "null"
     *
     * @param stringToBeEdited the string as parameter to be checked for validity
     * @return returns the same as parameter if it is valid else nothing, i.e. a blank string and avoids hyphen
     */
    public static String getUnderScoreRemovedString(String stringToBeEdited) {
        if (stringToBeEdited == null || stringToBeEdited.equals("null")) {
            stringToBeEdited = "-";
            return stringToBeEdited;
        } else {
            return stringToBeEdited.replace('_', ' ');
        }
    }

    public static final int RETURN_EMPTY_STRING_IF_NULL = 0;
    public static final int RETURN_HYPHEN_STRING_IF_NULL = 1;
    public static final int REPLACE_UNDERSCORE_WITH_SPACE = 2;
    public static final int REPLACE_UNDERSCORE_WITH_HYPHEN = 3;
    public static final int ADD_SPACE_IF_EMPTY = 4;
    public static final int RETURN_EMPTY_IF_HYPHEN = 5;

    public static String[] filterString(int type, String... toBeFiltered) {
        ArrayList<String> filteredStrings = new ArrayList<>();
        for (String string : toBeFiltered) {
            filteredStrings.add(filterString(type, string));
        }

        String[] filtered = new String[filteredStrings.size()];
        for (int i = 0; i < filteredStrings.size(); i++)
            filtered[i] = filteredStrings.get(i);

        return filtered;
    }

    public static String filterString(int type, String toBeFiltered) {
        toBeFiltered = (toBeFiltered == null || toBeFiltered.equals("null")) ? "" : toBeFiltered;

        switch (type) {
            case RETURN_EMPTY_STRING_IF_NULL:
                return toBeFiltered;
            case RETURN_HYPHEN_STRING_IF_NULL:
                return toBeFiltered.isEmpty() ? "-" : toBeFiltered;
            case REPLACE_UNDERSCORE_WITH_SPACE:
                return toBeFiltered.replace('_', ' ');
            case REPLACE_UNDERSCORE_WITH_HYPHEN:
                return toBeFiltered.replace('_', '-');
            case ADD_SPACE_IF_EMPTY:
                return toBeFiltered.isEmpty() ? " " : toBeFiltered;
            case RETURN_EMPTY_IF_HYPHEN:
                return toBeFiltered.contains("-") && toBeFiltered.length() == 1 ? "" : toBeFiltered;
            default:
                return toBeFiltered;
        }
    }

    public static boolean getBoolean(int value) {
        return value == 1;
    }

    public static void animateLayoutChanges(View view) {
        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
        ((ViewGroup) view).setLayoutTransition(layoutTransition);
    }

    public static String replaceUnderscoreWithHyphen(String text) {
        if (text.contains("_"))
            return text.replace('_', '-');
        else
            return text;
    }

    public static boolean isGreaterInDoubles(String s1, String s2) {
        return isGreaterInDoubles(Utils.parseDouble(s1), Utils.parseDouble(s2));
    }

    public static boolean isGreaterInDoubles(double d1, double d2) {
        return (d1 > d2);
    }

    public static boolean isInvalidString(EditText editText) {
        String text = editText.getText().toString();
        if (editText.getError() != null || text.length() == 0)
            return true;
        return false;
    }

    public static boolean isInvalidString(String string) {
        return string == null || string.length() == 0;
    }

    public static int parseVersionInt(String[] versionCode, int position) {
        String code;
        if (position >= versionCode.length)
            code = "0";
        else
            code = versionCode[position];

        return parseInt(code);
    }

    public static String getVersionCode(Context context) {
        return getVersionCode(context, "0.0");
    }

    public static String getVersionCode(Context context, String versionFromServer) {
        String versionFromDevice = versionFromServer;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionFromDevice = packageInfo.versionName;
//            Log.e(TAG, "isUpdateAvailable.versionFromDevice: " + versionFromDevice);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionFromDevice;
    }

    public static boolean isUpdateAvailable(Context context, String versionFromServer) {
        boolean isUpdateAvailable = false;
        String versionFromDevice = getVersionCode(context, versionFromServer);
//        Log.e(TAG, "isUpdateAvailable.versionFromServer: " + versionFromServer);

        String[] serverCodes = versionFromServer.split("\\.");
        String[] deviceCodes = versionFromDevice.split("\\.");

        if (serverCodes.length > 0 /*&& deviceCodes.length > 0*/) {
            isUpdateAvailable = Utils.parseVersionInt(serverCodes, 0) > Utils.parseVersionInt(deviceCodes, 0);
            if (!isUpdateAvailable && serverCodes.length > 1) {
                isUpdateAvailable = Utils.parseVersionInt(serverCodes, 1) > Utils.parseVersionInt(deviceCodes, 1);
                if (!isUpdateAvailable && serverCodes.length > 2 /*&& deviceCodes.length > 2*/) {
                    isUpdateAvailable = Utils.parseVersionInt(serverCodes, 2) > Utils.parseVersionInt(deviceCodes, 2);
                }
            }
        }

        return isUpdateAvailable;
    }

    public static double getNonNanDouble(Double doubleValue) {
        if (doubleValue.isNaN())
            return 0;
        else
            return doubleValue;
    }
}
