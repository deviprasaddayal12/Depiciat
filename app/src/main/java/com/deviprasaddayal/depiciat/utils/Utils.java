package com.deviprasaddayal.depiciat.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.deviprasaddayal.depiciat.R;

import java.util.ArrayList;

/**
 * Created by Atul on 5/4/18.
 */

public class Utils {
    public static final String TAG = Utils.class.getCanonicalName();

    private static final int digitsBeforeDecimal = 8, digitsAfterDecimal = 3;
    private static final String decimal = ".";

    private static final boolean shouldLog = true;

    public static void loge(String tag, String message) {
        if (shouldLog)
            Log.e(tag, message);
    }

    public static void logi(String tag, String message) {
        if (shouldLog)
            Log.i(tag, message);
    }

    public static void logd(String tag, String message) {
        if (shouldLog)
            Log.d(tag, message);
    }

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
    public static InputFilter getFloatMaxThreshold() {
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

    public static InputFilter[] getMaxFloatFilters() {
        InputFilter floatMaxThresholdFilter = getFloatMaxThreshold();
        InputFilter floatMaxLengthFilter = new InputFilter.LengthFilter(10);
        return new InputFilter[]{floatMaxThresholdFilter, floatMaxLengthFilter};
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

    public static final int RETURN_EMPTY_IF_NULL = 0;
    public static final int RETURN_HYPHEN_IF_NULL = 1;
    public static final int REPLACE_UNDERSCORE_WITH_SPACE = 2;
    public static final int REPLACE_UNDERSCORE_WITH_HYPHEN = 3;
    public static final int RETURN_SPACE_IF_EMPTY = 4;
    public static final int RETURN_EMPTY_IF_HYPHEN = 5;

    public static String[] toValidString(int type, String... toBeFiltered) {
        ArrayList<String> filteredStrings = new ArrayList<>();
        for (String string : toBeFiltered) {
            filteredStrings.add(toValidString(type, string));
        }

        String[] filtered = new String[filteredStrings.size()];
        for (int i = 0; i < filteredStrings.size(); i++)
            filtered[i] = filteredStrings.get(i);

        return filtered;
    }

    public static String toValidString(int type, String toBeFiltered) {
        toBeFiltered = (toBeFiltered == null || toBeFiltered.equals("null")) ? "" : toBeFiltered;

        switch (type) {
            case RETURN_EMPTY_IF_NULL:
                return toBeFiltered;
            case RETURN_HYPHEN_IF_NULL:
                return toBeFiltered.isEmpty() ? "-" : toBeFiltered;
            case REPLACE_UNDERSCORE_WITH_SPACE:
                return toBeFiltered.replace('_', ' ');
            case REPLACE_UNDERSCORE_WITH_HYPHEN:
                return toBeFiltered.replace('_', '-');
            case RETURN_SPACE_IF_EMPTY:
                return toBeFiltered.isEmpty() ? " " : toBeFiltered;
            case RETURN_EMPTY_IF_HYPHEN:
                return toBeFiltered.contains("-") && toBeFiltered.length() == 1 ? "" : toBeFiltered;
            default:
                return toBeFiltered;
        }
    }

    public static String toHyphen(String toBeChanged) {
        return toValidString(RETURN_HYPHEN_IF_NULL, toBeChanged);
    }

    public static boolean toBoolean(int value) {
        return value == 1;
    }

    public static boolean isGreaterInDoubles(String s1, String s2) {
        return isGreaterInDoubles(Utils.parseDouble(s1), Utils.parseDouble(s2));
    }

    public static boolean isGreaterInDoubles(double d1, double d2) {
        return (d1 > d2);
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
}
