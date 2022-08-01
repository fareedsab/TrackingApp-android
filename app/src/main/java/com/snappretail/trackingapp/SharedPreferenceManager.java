package com.snappretail.trackingapp;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceManager {

    private static final String APP_SETTINGS = "MySharedPref";


    // properties
//    private static final String SOME_STRING_VALUE = "SOME_STRING_VALUE";
    // other properties...


    private SharedPreferenceManager() {}

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);
    }

    public static String getSomeStringValue(Context context,String SOME_STRING_KEY) {
        return getSharedPreferences(context).getString(SOME_STRING_KEY , null);
    }

    public static void setSomeStringValue(Context context,String SOME_STRING_VALUE,String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(SOME_STRING_VALUE , newValue);
        editor.commit();
    }
    public static void RemoveAllShared(Context context) {
        SharedPreferenceManager.getSharedPreferences(context).edit().clear().commit();
//        Boolean isClear = preferences.edit().clear().commit();
    }
    // other getters/setters
}

