package com.hzp.superscreenlock.entity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

/**
 * Created by hezhipeng on 2016/8/22.
 */
public class AppInfo {
    private String appLabel;
    private Drawable appIcon;
    private Intent intent;
    private String pkgName;

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

    public boolean isEmpty() {
        return
                TextUtils.isEmpty(appLabel) &&
                        TextUtils.isEmpty(pkgName);
    }
}
