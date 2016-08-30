package com.hzp.superscreenlock.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.hzp.superscreenlock.entity.AppInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by hezhipeng on 2016/8/23.
 */
public class SystemUtil {
    public static final String TAG = "SystemUtil";

    /**
     * 获取系统内安装的应用信息
     *
     * @param context
     * @param callback
     * @see com.hzp.superscreenlock.entity.AppInfo
     */
    public static void queryAppsInfo(Context context, AppsInfoQueryCallback callback) {
        PackageManager pm = context.getPackageManager(); //获得PackageManager对象
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        // 通过查询，获得所有ResolveInfo对象.
        List<ResolveInfo> resolveInfos = pm
                .queryIntentActivities(mainIntent, PackageManager.MATCH_DEFAULT_ONLY);
        // 调用系统排序 ， 根据name排序
        // 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序
        Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(pm));

        List<AppInfo> appList = new ArrayList<>();

        for (ResolveInfo reInfo : resolveInfos) {
            String activityName = reInfo.activityInfo.name; // 获得该应用程序的启动Activity的name
            String pkgName = reInfo.activityInfo.packageName; // 获得应用程序的包名
            String appLabel = (String) reInfo.loadLabel(pm); // 获得应用程序的Label
            Drawable icon = reInfo.loadIcon(pm); // 获得应用程序图标
            // 为应用程序的启动Activity 准备Intent
            Intent launchIntent = new Intent();
            launchIntent.setComponent(new ComponentName(pkgName,
                    activityName));
            // 创建一个AppInfo对象，并赋值
            AppInfo appInfo = new AppInfo()
                    .setAppLabel(appLabel)
                    .setPkgName(pkgName)
                    .setAppIcon(icon)
                    .setIntent(launchIntent);
            appList.add(appInfo);
        }
        if (callback != null) {
            callback.onQueryFinished(appList);
        }
    }


    /**
     * 获得当前连接wifi的SSID
     * @param context
     * @return SSID 可能为null
     */
    public static String getCurrentWifiSSID(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if(!wifiManager.isWifiEnabled()){
            return null;
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getSSID().replace('\"',' ').trim();//去除掉可能存在的双引号
    }

    /**
     * 获得已配置的wifi热点的SSID
     * @param context
     * @return SSID
     */
    public static String[] getConfiguredWifiSSID(Context context){
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> wifiList = wifiManager.getConfiguredNetworks();
        String[] re = new String[wifiList.size()];
        for(int i =0;i<wifiList.size();i++){
            re[i] = wifiList.get(i).SSID.replace('\"',' ').trim();
        }
        return re;
    }

    interface AppsInfoQueryCallback {
        void onQueryFinished(List<AppInfo> list);
    }
}
