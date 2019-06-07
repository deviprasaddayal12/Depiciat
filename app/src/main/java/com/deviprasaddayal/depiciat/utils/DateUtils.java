package com.deviprasaddayal.depiciat.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {
    public static final String TAG = DateUtils.class.getCanonicalName();

    public static final String FORMAT_DATE_N_TIME_12HRS = "dd/MM/yyyy hh:mm:ss aa";
    public static final String FORMAT_DATE_N_TIME_24HRS = "dd/MM/yyyy HH:mm:ss";
    public static final String FORMAT_DATE_N_TIME_ISO = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static final String FORMAT_DATE_N_TIME_24HRS_UTC = "dd/MM/yyyy HH:mm:ss";
    public static final String FORMAT_DATE_ONLY = "dd/MM/yyyy";
    public static final String FORMAT_TIME_ONLY_12HRS = "hh:mm:ss aa";
    public static final String FORMAT_TIME_ONLY_12HRS_NO_MERIDIAN = "hh:mm:ss";
    public static final String FORMAT_TIME_ONLY_24HRS = "HH:mm:ss";
    public static final String FORMAT_DATE_ONLY_DIESEL = "yyyy-MM-dd";
    public static final String FORMAT_DATE_N_TIME_DIESEL = FORMAT_DATE_ONLY_DIESEL + " " + FORMAT_TIME_ONLY_24HRS;

    public static long getUTCTime(long time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            String date = sdf.format(new Date(time));
            SimpleDateFormat sdf1 = new SimpleDateFormat();
            Date d = sdf1.parse(date);
            return d.getTime();
        } catch (Exception e) {
            Log.e(TAG, "getUTCTime: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * This function is used to convert date into string in the specified format
     *
     * @param millis date to be converted
     * @return @date in string
     */
    public static String getDateStringFromMillis(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return getDateInString(calendar.getTime());
    }

    public static String getDateInString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_DATE_ONLY, Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    public static String getDateForDiesel(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_DATE_ONLY_DIESEL, Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    public static String getTimeInString(Date date) {
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat(FORMAT_TIME_ONLY_12HRS, Locale.getDefault());
        return simpleTimeFormat.format(date);
    }

    public static String getTimeInStringWithoutMeridian(Date date) {
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat(FORMAT_TIME_ONLY_12HRS_NO_MERIDIAN, Locale.getDefault());
        return simpleTimeFormat.format(date);
    }

    public static Date getDateFromString(String date) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_DATE_ONLY, Locale.getDefault());
            return simpleDateFormat.parse(date);
        } catch (Exception e) {
            Log.e(TAG, "getDateFromString: " + e.getMessage());
            return null;
        }
    }

    public static Date getDateInPattern(String date, String pattern) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
            return simpleDateFormat.parse(date);
        } catch (Exception e) {
            Log.e(TAG, "getDateInPattern: " + e.getMessage());
            return null;
        }
    }

    public static String getDateInPattern(String date, String fromPattern, String toPattern) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat(fromPattern, Locale.getDefault());
            Date parsedDate = inputFormat.parse(date);
            SimpleDateFormat outputFormat = new SimpleDateFormat(toPattern, Locale.getDefault());
            return outputFormat.format(parsedDate);
        } catch (Exception e) {
            Log.e(TAG, "getDateInPattern: " + e.getMessage());
            return null;
        }
    }

    public static String getDieselDateTime(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_DATE_N_TIME_DIESEL, Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    public static Date getDateForDieselDateTime(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_DATE_N_TIME_DIESEL, Locale.getDefault());
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static String getImageTimeStamp() {
        Date now = new Date();
        String date = getDateInString(now);
        date = date.replace("/", "");
        String time = getTimeInStringWithoutMeridian(now);
        time = time.replace(":", "");

        String timeStamp = String.format("%s_%s", date, time);
        Log.i(TAG, "getImageTimeStamp: " + timeStamp);
        return timeStamp + "_";
    }

    public static String getPrevious24HrsDate(int addNoOfDaysToToday) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_DATE_ONLY_DIESEL, Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);

        calendar.add(Calendar.DAY_OF_MONTH, addNoOfDaysToToday);
        String sSelectedDate = simpleDateFormat.format(calendar.getTime());
        Log.e(TAG, "getPrevious24HrsDate: sSelectedDate" + sSelectedDate);

        return sSelectedDate;
    }

    public static String getISO8601Parsed(String dateInISO8601) {
        String formattedDate = "";
        if (dateInISO8601 == null || dateInISO8601.isEmpty()) {
            return "";
        }
        try {
            DateFormat dateFormat = new SimpleDateFormat(FORMAT_DATE_N_TIME_ISO, Locale.getDefault());
            Date date = dateFormat.parse(dateInISO8601);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_DATE_N_TIME_12HRS, Locale.getDefault());
            formattedDate = simpleDateFormat.format(date);
            return formattedDate;
        } catch (ParseException e) {
            Log.e(TAG, "getISO8601Parsed: " + e.getMessage());
            return formattedDate;
        }
    }

    public static long getCurrentTime() {
        return new Date().getTime();
    }

    public static Calendar getCalendarInstance(String previousDate) {
        Calendar calendar = Calendar.getInstance();
        try{
            Date date = getDateFromString(previousDate);
            calendar.setTime(date);
        } catch (Exception e){
            Log.e(TAG, "getCalendarInstance: " + e.getMessage());
        }
        return calendar;
    }
}
