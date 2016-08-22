package com.hzp.superscreenlock.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by hezhipeng on 2016/8/22.
 */
public class WindowUtil {

    /**
     * 获得屏幕宽度
     * @param activity
     * @return dip
     */
    public static int getDisplayWidthDip(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return  dm.widthPixels;// 屏幕宽（dip，如：320dip）

    }
}
