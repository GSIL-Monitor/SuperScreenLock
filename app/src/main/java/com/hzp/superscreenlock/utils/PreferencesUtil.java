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

    public static SharedPreferences getDefaultSharePreferences(Context context){
        return context.getSharedPreferences(AppConstant.DEFAULT_SHAREPREFERENCES,Context.MODE_PRIVATE);
    }
}
