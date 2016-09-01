package com.hzp.superscreenlock.entity;

/**
 * Created by hezhipeng on 2016/8/25.
 */
public class EnvironmentInfo{

    public static final String TYPE_DEFAULT = "TYPE_DEFAULT";
    public static final String TYPE_WIFI = "TYPE_WIFI";
    public static final String TYPE_LOCATION = "TYPE_LOCATION";

    public enum LockType {
        LOCK_TYPE_NONE("LOCK_TYPE_NONE",0),//不加锁
        LOCK_TYPE_PASSWORD("LOCK_TYPE_PASSWORD",1),//密码锁
        LOCK_TYPE_PATTERN("LOCK_TYPE_PATTERN",2);//九宫格锁

        private String name;
        private int priority;//优先程度从0开始依次增加
        LockType(String name, int priority){
            this.name = name;
            this.priority = priority;
        }

        public int getPriority() {
            return priority;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    private String title;
    private String hint="";//默认为空 需求暂时不需要显示
    private String type;
    private LockType lockType;
    private String password;
    private String patternPassword;

    /* TYPE_WIFI */
    private String wifiSSID;

    /* TYPE_LOCATION*/
    private double longitude/*经度*/, latitude/*纬度*/;

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

    public double getLatitude() {
        return latitude;
    }

    public EnvironmentInfo setLatitude(double latitude) {
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

    public double getLongitude() {
        return longitude;
    }

    public EnvironmentInfo setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public LockType getLockType() {
        return lockType;
    }

    public EnvironmentInfo setLockType(LockType lockType) {
        this.lockType = lockType;
        return this;
    }

    public EnvironmentInfo setLockType(String lockTypeName) {
        if(lockTypeName==null){
            return this;
        }
        switch (lockTypeName){
            case "LOCK_TYPE_NONE":
                setLockType(LockType.LOCK_TYPE_NONE);
                break;
            case "LOCK_TYPE_PASSWORD":
                setLockType(LockType.LOCK_TYPE_PASSWORD);
                break;
            case "LOCK_TYPE_PATTERN":
                setLockType(LockType.LOCK_TYPE_PATTERN);
                break;
            default:
                throw new IllegalArgumentException("wrong lock type name!");
        }
        return this;
    }

    public String getPatternPassword() {
        return patternPassword;
    }

    public EnvironmentInfo setPatternPassword(String patternPassword) {
        this.patternPassword = patternPassword;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public EnvironmentInfo setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public String toString() {
        return "{title ="+title+" hint="+hint+" type="+type+" lockType="+lockType+"}";
    }
}
