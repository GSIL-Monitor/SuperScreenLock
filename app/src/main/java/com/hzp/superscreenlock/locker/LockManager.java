package com.hzp.superscreenlock.locker;

import com.hzp.superscreenlock.entity.EnvironmentInfo;

/**
 * Created by hezhipeng on 2016/8/25.
 */
public class LockManager {
    public static final String TAG = "LockManager";

    private static LockManager instance;

    private String currentSSID;
    private long currentLongitude,currentLatitude;

    private LockManager(){}

    public static LockManager getInstance(){
        if(instance==null){
            synchronized (LockManager.class){
                if(instance==null){
                    instance=new LockManager();
                }
            }
        }
        return instance;
    }

    /**
     * 由Manager来控制启动解锁界面的类型
     */
    public void startUnlockActivity(){
        // TODO: 2016/8/25 1.滑动解锁（无锁） 2.密码解锁 3.九宫格
    }

    /**
     * 场景与当前状态进行同步
     * @return 符合当前状态的场景，包含TYPE_UNKNOWN
     */
    public EnvironmentInfo syncCurrentEnvironment(){

    }

    public boolean checkEnvironment(EnvironmentInfo info){
        checkWifiEnvironment(info);
        checkGPSEnvironment(info);
    }

    private boolean checkWifiEnvironment(EnvironmentInfo info){
        return info.getType().equals(EnvironmentInfo.TYPE_WIFI)
                &&
                info.getWifiSSID().equals(currentSSID);
    }

    private boolean checkGPSEnvironment(EnvironmentInfo info){
        return calculatePoints(info.getLongitude(),info.getLatitude(),
                currentLongitude,currentLatitude);
    }

    public void updateWifiState(String currentSSID){
        this.currentSSID = currentSSID;
    }

    public void updateGPSState(long longitude,long latitude ){
        this.currentLatitude = latitude;
        this.currentLongitude = longitude;
    }
}
