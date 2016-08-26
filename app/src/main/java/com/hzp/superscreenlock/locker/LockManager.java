package com.hzp.superscreenlock.locker;

import com.hzp.superscreenlock.entity.EnvironmentInfo;
import com.hzp.superscreenlock.manager.EnvironmentManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hezhipeng on 2016/8/25.
 */
public class LockManager {
    public static final String TAG = "LockManager";

    private static LockManager instance;

    private String currentSSID;
    private double currentLongitude, currentLatitude;

    private LockManager() {
    }

    public static LockManager getInstance() {
        if (instance == null) {
            synchronized (LockManager.class) {
                if (instance == null) {
                    instance = new LockManager();
                }
            }
        }
        return instance;
    }

    /**
     * 由Manager来控制启动解锁界面的类型
     */
    public void startUnlockActivity() {
        // TODO: 2016/8/25 1.滑动解锁（无锁） 2.密码解锁 3.九宫格
    }

    /**
     * 场景与当前状态进行同步
     *
     * @return 符合当前状态的场景
     */
    public EnvironmentInfo syncCurrentEnvironment() {
        List<EnvironmentInfo> list = EnvironmentManager.getInstance().getAllItems();
        List<EnvironmentInfo> checkResult = new ArrayList<>();

        for (EnvironmentInfo info : list) {
            if (checkEnvironment(info)) {
                checkResult.add(info);
            }
        }

        if (checkResult.size() >= 2) {//解决场景冲突
            EnvironmentInfo choose = null;
            int priority = EnvironmentInfo.LockType.LOCK_TYPE_NONE.getPriority();
            for (EnvironmentInfo c :
                    checkResult) {
                if (c.getLockType().getPriority() >= priority) {
                    choose = c;
                    priority = c.getLockType().getPriority();
                }
            }
            return choose;
        } else if (checkResult.size() == 1) {
            return checkResult.get(0);
        } else {
            return getSystemDefaultEnvironment();
        }
    }

    /**
     * 在用户没有指定默认场景的情况下，系统指派一个默认的场景
     *
     * @return
     */
    private EnvironmentInfo getSystemDefaultEnvironment() {
        return new EnvironmentInfo()
                .setType(EnvironmentInfo.TYPE_DEFAULT)
                .setLockType(EnvironmentInfo.LockType.LOCK_TYPE_NONE);
    }

    public boolean checkEnvironment(EnvironmentInfo info) {
        switch (info.getType()) {
            case EnvironmentInfo.TYPE_WIFI:
                return checkWifiEnvironment(info);
            case EnvironmentInfo.TYPE_GPS:
                return checkGPSEnvironment(info);
            case EnvironmentInfo.TYPE_DEFAULT:
                return !checkGPSEnvironment(info) && !checkWifiEnvironment(info);
        }
        return false;
    }

    private boolean checkWifiEnvironment(EnvironmentInfo info) {
        return currentSSID != null
                &&
                info.getWifiSSID().equals(currentSSID);
    }

    private boolean checkGPSEnvironment(EnvironmentInfo info) {
        // TODO: 2016/8/26 函数完成
//        return calculatePoints(info.getLongitude(),info.getLatitude(),
//                currentLongitude,currentLatitude);
        return false;
    }

    public void updateWifiState(String currentSSID) {
        this.currentSSID = currentSSID;
    }

    public void updateGPSState(double longitude, double latitude) {
        this.currentLatitude = latitude;
        this.currentLongitude = longitude;
    }
}
