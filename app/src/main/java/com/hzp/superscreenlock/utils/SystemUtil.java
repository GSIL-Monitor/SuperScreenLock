package com.hzp.superscreenlock.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.hzp.superscreenlock.entity.AppInfo;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by hezhipeng on 2016/8/23.
 */
public class SystemUtil {
    public static final String TAG = "SystemUtil";


    public static String[] weeks = {"星期六", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五",};

    /**
     * 获取系统内安装的应用信息
     *
     * @param context
     * @param callback
     * @see com.hzp.superscreenlock.entity.AppInfo
     */
    public static void queryAppsInfo(Context context, AppsInfoQueryCallback callback) {
        PackageManager pm = context.getPackageManager(); //获得PackageManager对象
        List<PackageInfo> packages = pm.getInstalledPackages(0);

        List<AppInfo> appList = new ArrayList<>();

        for (PackageInfo pkg : packages) {
            String appLabel = pkg.applicationInfo.loadLabel(pm).toString();
            String pkgName = pkg.packageName;
            Drawable icon = pkg.applicationInfo.loadIcon(pm);
            Intent launchIntent = pm.getLaunchIntentForPackage(pkg.packageName);
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

    public static AppInfo queryAppInfo(Context context, String packageName) {

        PackageManager pm = context.getPackageManager();
        AppInfo appInfo = null;
        try {
            ApplicationInfo info = pm.getApplicationInfo(packageName,
                    PackageManager.GET_META_DATA);
            String label = pm.getApplicationLabel(info).toString();
            Drawable icon = pm.getApplicationIcon(info);
            Intent launchIntent = pm.getLaunchIntentForPackage(packageName);

            appInfo = new AppInfo();
            appInfo.setPkgName(packageName)
                    .setAppLabel(label)
                    .setAppIcon(icon)
                    .setIntent(launchIntent);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appInfo;
    }


    /**
     * 获得当前连接wifi的SSID
     *
     * @param context
     * @return SSID 可能为null
     */
    public static String getCurrentWifiSSID(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            return null;
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getSSID().replace('\"', ' ').trim();//去除掉可能存在的双引号
    }

    /**
     * 获得已配置的wifi热点的SSID
     *
     * @param context
     * @return SSID
     */
    public static String[] getConfiguredWifiSSID(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> wifiList = wifiManager.getConfiguredNetworks();
        if (wifiList != null) {
            String[] re = new String[wifiList.size()];
            for (int i = 0; i < wifiList.size(); i++) {
                re[i] = wifiList.get(i).SSID.replace('\"', ' ').trim();
            }
            return re;
        }else {
            return null;
        }
    }

    public static String encryptString(String content) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(content.getBytes("UTF-8"));
            byte[] encryption = md5.digest();

            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                } else {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }

            return strBuf.toString().toUpperCase();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException(e.toString());
        }
    }

    public static String getWeek(int dayOfWeek) {
        return weeks[dayOfWeek];
    }

    public interface AppsInfoQueryCallback {
        void onQueryFinished(List<AppInfo> list);
    }
}
