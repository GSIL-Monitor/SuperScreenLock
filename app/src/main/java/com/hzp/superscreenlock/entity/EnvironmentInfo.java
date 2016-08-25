package com.hzp.superscreenlock.entity;

/**
 * Created by hezhipeng on 2016/8/25.
 */
public class EnvironmentInfo {

    public static final String LOCK_TYPE_WIFI = "LOCK_TYPE_WIFI";
    public static final String LOCK_TYPE_GPS = "LOCK_TYPE_GPS";
    public static final String LOCK_TYPE_UNKNOWN = "LOCK_TYPE_UNKNOWN";

    public static final String LOCK_NONE ="LOCK_NONE";//不加锁
    public static final String LOCK_PASSWORD = "LOCK_PASSWORD";//密码锁
    public static final String LOCK_PATTERN = "LOCK_PATTERN";//九宫格锁

    private String title;
    private String hint;
    private String type;
    private String lockType;

    /* LOCK_TYPE_WIFI */
    private String wifiSSID;

    /* LOCK_TYPE_GPS*/
    private long longitude/*经度*/,latitude/*纬度*/;

    public String getType() {
        return type;
    }

    public EnvironmentInfo setType(String type) {
        this.type = type;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public EnvironmentInfo setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getHint() {
        return hint;
    }

    public EnvironmentInfo setHint(String hint) {
        this.hint = hint;
        return this;
    }

    public long getLatitude() {
        return latitude;
    }

    public EnvironmentInfo setLatitude(long latitude) {
        this.latitude = latitude;
        return this;
    }

    public String getWifiSSID() {
        return wifiSSID;
    }

    public EnvironmentInfo setWifiSSID(String wifiSSID) {
        this.wifiSSID = wifiSSID;
        return this;
    }

    public long getLongitude() {
        return longitude;
    }

    public EnvironmentInfo setLongitude(long longitude) {
        this.longitude = longitude;
        return this;
    }

    public String getLockType() {
        return lockType;
    }

    public EnvironmentInfo setLockType(String lockType) {
        this.lockType = lockType;
        return this;
    }
}
