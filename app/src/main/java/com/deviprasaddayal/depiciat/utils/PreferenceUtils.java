package com.deviprasaddayal.depiciat.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


/**
 * Created by Atul on 5/4/18.
 */

public class PreferenceUtils {

    public static final String TAG = PreferenceUtils.class.getCanonicalName();

    public interface Keys {
        String PREF_NAME = "NKC_ERP";
        String KEY_USER_MODEL = "KEY_USER_MODEL";

        String KEY_USERNAME = "KEY_USERNAME";
        String KEY_PASSWORD = "KEY_PASSWORD";
        String KEY_STAY_SIGNED_IN = "KEY_STAY_SIGNED_IN";
        String KEY_LOGIN_STATUS = "KEY_LOGIN_STATUS";
        String KEY_LAST_LATITUDE = "KEY_LATITUDE";
        String KEY_LAST_LONGITUDE = "KEY_LONGITUDE";

        String KEY_DIESEL_RESPONSE = "diesel_local_response";
        String KEY_LOCAL_OPEN_DIESEL_LIST = "diesel_local_open_list";
        String KEY_LOCAL_CLOSED_DIESEL_LIST = "diesel_local_closed_list";
        String KEY_LOCAL_REQUISITION_IDS = "diesel_local_requisition_ids";
        String KEY_DIESEL_TANKER_MODEL = "diesel_local_diesel_tanker";
        String KEY_REJECT_MODELS = "diesel_reject_models";

        String KEY_FLEET_DEFAULT_SITE = "fleet_default_site";
        String KEY_FLEET_DEFAULT_USER = "fleet_default_user";
        String KEY_FLEET_DEFAULT_AUTH = "fleet_default_auth";
        String KEY_FLEET_AUTH_USERNAME = "fleet_auth_username";
        String KEY_FLEET_AUTH_PASSWORD = "fleet_auth_password";
        String KEY_SHOULD_SHOW_FLEET = "fleet_show_as_menu";

        String LAST_UPDATE_GEOFENCE = "LAST_UPDATE_GEOFENCE";
        String KEY_SITE_ID_SET = "KEY_SITE_ID_SET";
    }

    public static void saveString(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Keys.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void deleteString(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Keys.PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(key).apply();
    }

    public static String getString(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Keys.PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }

    public static void saveBoolean(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Keys.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Keys.PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    public static void clearAllSharedPreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Keys.PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }

    public static void saveInt(Context context, String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Keys.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getInt(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Keys.PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, 0);
    }

    /*public static void saveUserModel(Context context, UserModel userModel) {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context.getSharedPreferences(Keys.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Keys.KEY_USER_MODEL, gson.toJson(userModel, UserModel.class));
        editor.apply();
    }

    public static UserModel getUserModel(Context context) {
        try {
            Gson gson = new Gson();
            SharedPreferences sharedPreferences = context.getSharedPreferences(Keys.PREF_NAME, Context.MODE_PRIVATE);
            return gson.fromJson(sharedPreferences.getString(Keys.KEY_USER_MODEL, ""), UserModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new UserModel();
    }

    public static void deleteUserCredentials(Context context) {
        try {
            PreferenceUtils.deleteString(context, PreferenceUtils.Keys.KEY_USERNAME);
            PreferenceUtils.deleteString(context, PreferenceUtils.Keys.KEY_PASSWORD);
            PreferenceUtils.saveBoolean(context, PreferenceUtils.Keys.KEY_STAY_SIGNED_IN, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateLastKnownLocation(Context context, String latitude, String longitude) {
        try {
            PreferenceUtils.saveString(context, Keys.KEY_LAST_LATITUDE, latitude);
            PreferenceUtils.saveString(context, Keys.KEY_LAST_LONGITUDE, longitude);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ///////////////////////////////////////////////////////////////////////////////// Diesel Helpers

    public static double[] getLatLng(Context context){
        double latitude = Utils.parseDouble(getString(context, PreferenceUtils.Keys.KEY_LAST_LATITUDE));
        double longitude = Utils.parseDouble(getString(context, PreferenceUtils.Keys.KEY_LAST_LONGITUDE));

        return new double[]{latitude, longitude};
    }

    public static DieselTankerModel getDieselTankerModel(Context context) {
        try {
            Gson gson = new Gson();
            SharedPreferences sharedPreferences = context.getSharedPreferences(Keys.PREF_NAME, Context.MODE_PRIVATE);

            String savedString = sharedPreferences.getString(Keys.KEY_DIESEL_TANKER_MODEL, "");
            Log.e(TAG, "getDieselTankerModel: " + savedString);

            return gson.fromJson(savedString, DieselTankerModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveDieselTankerModel(Context context, DieselTankerModel dieselTankerModel) {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context.getSharedPreferences(Keys.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Type type = new TypeToken<DieselTankerModel>() {}.getType();

        editor.remove(Keys.KEY_DIESEL_TANKER_MODEL);
        editor.putString(Keys.KEY_DIESEL_TANKER_MODEL, gson.toJson(dieselTankerModel, type));
        editor.apply();
    }

    public static HashMap<String, DieselDataModel> getLocalOpenDieselList(Context context) {
        try {
            Gson gson = new Gson();
            SharedPreferences sharedPreferences = context.getSharedPreferences(Keys.PREF_NAME, Context.MODE_PRIVATE);
            Type type = new TypeToken<HashMap<String, DieselDataModel>>() {}.getType();

            String savedString = sharedPreferences.getString(Keys.KEY_LOCAL_OPEN_DIESEL_LIST, "");
//            Log.i(TAG, "getLocalOpenDieselList: " + savedString);

            HashMap<String, DieselDataModel> hashMap = gson.fromJson(savedString, type);
            if (hashMap == null) hashMap = new HashMap<>();
            return hashMap;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public static void saveLocalOpenDieselList(Context context, HashMap<String, DieselDataModel> dieselDataModels) {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context.getSharedPreferences(Keys.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Type type = new TypeToken<HashMap<String, DieselDataModel>>() {}.getType();

        editor.remove(Keys.KEY_LOCAL_OPEN_DIESEL_LIST);
        editor.putString(Keys.KEY_LOCAL_OPEN_DIESEL_LIST, gson.toJson(dieselDataModels, type));
        editor.apply();
    }

    public static HashMap<String, ArrayList<DieselDataModel>> getLocalClosedDieselList(Context context) {
        try {
            Gson gson = new Gson();
            SharedPreferences sharedPreferences = context.getSharedPreferences(Keys.PREF_NAME, Context.MODE_PRIVATE);
            Type type = new TypeToken<HashMap<String, ArrayList<DieselDataModel>>>() {}.getType();

            String savedString = sharedPreferences.getString(Keys.KEY_LOCAL_CLOSED_DIESEL_LIST, "");
//            Log.i(TAG, "getLocalClosedDieselList: " + savedString);

            HashMap<String, ArrayList<DieselDataModel>> hashMap = gson.fromJson(savedString, type);
            if (hashMap == null) hashMap = new HashMap<>();
            return hashMap;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public static void saveLocalClosedDieselList(Context context, HashMap<String, ArrayList<DieselDataModel>> dieselDataModels) {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context.getSharedPreferences(Keys.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Type type = new TypeToken<HashMap<String, ArrayList<DieselDataModel>>>() {}.getType();

        editor.remove(Keys.KEY_LOCAL_CLOSED_DIESEL_LIST);
        editor.putString(Keys.KEY_LOCAL_CLOSED_DIESEL_LIST, gson.toJson(dieselDataModels, type));
        editor.apply();
    }

    public static ArrayList<Integer> getLocalOpenRequisitionIds(Context context) {
        try {
            Gson gson = new Gson();
            SharedPreferences sharedPreferences = context.getSharedPreferences(Keys.PREF_NAME, Context.MODE_PRIVATE);
            Type type = new TypeToken<ArrayList<Integer>>() {}.getType();

            String savedString = sharedPreferences.getString(Keys.KEY_LOCAL_REQUISITION_IDS, "[]");
//            Log.i(TAG, "getLocalOpenRequisitionIds: " + savedString);

            ArrayList<Integer> requisitionIds = gson.fromJson(savedString, type);
            if (requisitionIds == null) requisitionIds = new ArrayList<>();
            return requisitionIds;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void saveLocalOpenRequisitionIds(Context context, ArrayList<Integer> requisitionIds) {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context.getSharedPreferences(Keys.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Type type = new TypeToken<ArrayList<Integer>>(){}.getType();

        editor.remove(Keys.KEY_LOCAL_REQUISITION_IDS);
        editor.putString(Keys.KEY_LOCAL_REQUISITION_IDS, gson.toJson(requisitionIds, type));
        editor.apply();
    }

    public static ArrayList<DieselRejectModel> getRejectModels(Context context) {
        try {
            Gson gson = new Gson();
            SharedPreferences sharedPreferences = context.getSharedPreferences(Keys.PREF_NAME, Context.MODE_PRIVATE);
            Type type = new TypeToken<ArrayList<DieselRejectModel>>(){}.getType();

            String savedString = sharedPreferences.getString(Keys.KEY_REJECT_MODELS, "[]");
//            Log.i(TAG, "getRejectModels: " + savedString);

            return gson.fromJson(savedString, type);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void saveRejectModels(Context context, ArrayList<DieselRejectModel> dieselRejectModels) {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context.getSharedPreferences(Keys.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Type type = new TypeToken<ArrayList<DieselRejectModel>>(){}.getType();

        editor.remove(Keys.KEY_REJECT_MODELS);
        editor.putString(Keys.KEY_REJECT_MODELS, gson.toJson(dieselRejectModels, type));
        editor.apply();
    }

    public static JSONObject getLocalDieselResponse(Context context) {
        try {
            return new JSONObject(getString(context, Keys.KEY_DIESEL_RESPONSE));
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    public static void saveLocalDieselResponse(Context context, JSONObject jsonObject) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Keys.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove(Keys.KEY_DIESEL_RESPONSE);
        editor.putString(Keys.KEY_DIESEL_RESPONSE, jsonObject.toString());
        editor.apply();
    }

    ///////////////////////////////////////////////////////////////////////////////// Diesel Helpers

    *//*
     * Fleet Notifications
     *//*
    public static void setSiteIdsSet(Context context, String key, HashSet<String> hashSet) {
        try {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putStringSet(key, hashSet).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HashSet<String> getSiteIdsSet(Context context, String key) {
        try {
            return (HashSet<String>) PreferenceManager.getDefaultSharedPreferences(context).getStringSet(key, new HashSet<String>());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Fleet_DeviceDetailsModel> getDeviceList(Context context, String KEY_PREF) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(KEY_PREF, "");
        Type type = new TypeToken<ArrayList<Fleet_DeviceDetailsModel>>() {
        }.getType();
        ArrayList<Fleet_DeviceDetailsModel> deiceList = gson.fromJson(json, type);
        if (deiceList == null)
            deiceList = new ArrayList<>();
        return deiceList;
    }

    public static void saveDeviceList(Context context, ArrayList<Fleet_DeviceDetailsModel> deviceList, String KEY_PREF) {
        if (context != null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = prefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(deviceList);
            editor.putString(KEY_PREF, json);
            editor.apply();
        }
    }

    public static int getPreferredSiteForFleet(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(Keys.KEY_FLEET_DEFAULT_SITE, -1);
    }

    public static void savePreferredSiteForFleet(Context context, int siteId){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(Keys.KEY_FLEET_DEFAULT_SITE, siteId).apply();
    }*/
}
