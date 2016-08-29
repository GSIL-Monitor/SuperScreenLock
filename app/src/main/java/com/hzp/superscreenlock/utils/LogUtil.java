package com.hzp.superscreenlock.utils;

import android.util.Log;

import com.hzp.superscreenlock.AppConstant;

/**
 * Created by hezhipeng on 2016/8/25.
 */
public class LogUtil {
    private static final boolean logEnable = AppConstant.env.isLogEnable();

    public static void e(String TAG, String content) {
        if (logEnable) {
            Log.e(TAG, content);
        }
    }

    public static void i(String TAG, String content) {
        if (logEnable) {
            Log.i(TAG, content);
        }
    }


    public static void d(String TAG, String content) {
        if (logEnable) {
            Log.i(TAG, content);
        }
    }
}
