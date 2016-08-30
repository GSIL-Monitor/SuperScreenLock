package com.hzp.superscreenlock.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.hzp.superscreenlock.AppConstant;

/**
 * Created by hezhipeng on 2016/8/29.
 */
public class PreferencesUtil {

    public static final String KEY_ENV_TITLE = "KEY_ENV_TITLE";
    public static final String KEY_ENV_HINT = "KEY_ENV_HINT";
    public static final String KEY_ENV_LOCK_TYPE = "KEY_ENV_LOCK_TYPE";
    public static final String KEY_ENV_TYPE = "KEY_ENV_TYPE";
    public static final String KEY_ENV_WIFI_SSID = "KEY_ENV_WIFI_SSID";
    public static final String KEY_ENV_LOCATION_LONGITUDE = "KEY_ENV_LOCATION_LONGITUDE";
    public static final String KEY_ENV_LOCATION_LATITUDE = "KEY_ENV_LOCATION_LATITUDE";

    public static SharedPreferences getDefaultSharePreferences(Context context) {
        return context.getSharedPreferences(AppConstant.DEFAULT_SHAREPREFERENCES, Context.MODE_PRIVATE);
    }

    public static void putString(Context context, String key, String value) {
        getDefaultSharePreferences(context).edit()
                .putString(key, value)
                .apply();
    }

    public static String getString(Context context, String key) {
        return getDefaultSharePreferences(context).getString(key, null);
    }

    public static void putDouble(Context context, String key, double value) {
        getDefaultSharePreferences(context).edit()
                .putFloat(key, (float) value)
                .apply();
    }

    public static Double getDouble(Context context, String key) {
        return (double) getDefaultSharePreferences(context).getFloat(key, 0f);
    }
}
