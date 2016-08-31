package com.hzp.superscreenlock.entity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;

/**
 * Created by hezhipeng on 2016/8/22.
 */
public class AppInfo implements Comparable<AppInfo>{
    /*图标显示的方位*/
    public static final int SCREEN_SHOW_TYPE_BOTTOM = 1;//底部
    public static final int SCREEN_SHOW_TYPE_SLIDE = 2;//侧边栏

    private String appLabel;
    private Drawable appIcon;
    private Intent intent;
    private String pkgName;

    private int screenShowType = SCREEN_SHOW_TYPE_BOTTOM;
    private int showPosition = -1;

    public AppInfo() {
    }

    public String getAppLabel() {
        return appLabel;
    }

    public AppInfo setAppLabel(String appLabel) {
        this.appLabel = appLabel;
        return this;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public AppInfo setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
        return this;
    }

    public Intent getIntent() {
        return intent;
    }

    public AppInfo setIntent(Intent intent) {
        this.intent = intent;
        return this;
    }

    public String getPkgName() {
        return pkgName;
    }

    public AppInfo setPkgName(String pkgName) {
        this.pkgName = pkgName;
        return this;
    }

    public int getScreenShowType() {
        return screenShowType;
    }

    public AppInfo setScreenShowType(int screenShowType) {
        this.screenShowType = screenShowType;
        return this;
    }

    public int getShowPosition() {
        return showPosition;
    }

    public AppInfo setShowPosition(int showPosition) {
        this.showPosition = showPosition;
        return this;
    }

    public boolean isEmpty() {
        return
                TextUtils.isEmpty(appLabel) &&
                        TextUtils.isEmpty(pkgName);
    }

    @Override
    public int compareTo(@NonNull AppInfo another) {
        if(showPosition==another.showPosition){
            return 0;
        }
        return showPosition>another.showPosition?1:-1;
    }
}
