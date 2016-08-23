package com.hzp.superscreenlock.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by hezhipeng on 2016/8/22.
 */
public class WindowUtil {

    /**
     * 获得屏幕宽度、高度
     *
     * @param activity
     * @return [宽, 高]px
     */
    public static int[] getDisplayDimensionWithPx(Activity activity) {
        DisplayMetrics dm = getDisplayMetrics(activity);
        return new int[]{dm.widthPixels, dm.heightPixels};
    }

    /**
     * 获得屏幕宽度、高度
     * @param activity
     * @return [宽, 高]dip
     */
    public static int[] getDisplayDimensionWithDip(Activity activity) {
        DisplayMetrics dm = getDisplayMetrics(activity);
        return new int[]{dm.widthPixels / dm.densityDpi, dm.heightPixels / dm.densityDpi};
    }

    /**
     * 获得屏幕dip
     *
     * @param activity
     * @return dip
     */
    public static int getDisplayDip(Activity activity) {
        DisplayMetrics dm = getDisplayMetrics(activity);
        return dm.densityDpi;
    }

    public static DisplayMetrics getDisplayMetrics(Activity activity){
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }
}
