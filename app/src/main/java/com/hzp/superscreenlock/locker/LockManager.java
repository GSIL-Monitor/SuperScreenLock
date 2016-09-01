package com.hzp.superscreenlock.locker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.WindowManager;

import com.hzp.superscreenlock.entity.EnvironmentInfo;
import com.hzp.superscreenlock.fragment.UnlockFragment;
import com.hzp.superscreenlock.manager.EnvironmentManager;
import com.hzp.superscreenlock.service.BaseService;
import com.hzp.superscreenlock.utils.LogUtil;
import com.hzp.superscreenlock.utils.SystemUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hezhipeng on 2016/8/25.
 */
public class LockManager {
    public static final String TAG = "LockManager";

    private static LockManager instance;
    private Context context;

    List<Activity> activityList = new ArrayList<>();

    private EnvironmentInfo currentEnvironment;
    private String currentSSID;
    private Location currentLocation;

    private Intent startIntent;

    private float locationError = 500f;//距离判定最大误差范围 单位(米)

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

    public void init(Context context){
        activityList.clear();
        this.context  = context;
    }

    /**
     * 由Manager来控制启动解锁界面的类型
     */
    public void startUnlockView(Activity invoker, FragmentManager fm,Intent startIntent) {
        if (currentEnvironment == null) {
            return;
        }
        this.startIntent = startIntent;

        EnvironmentInfo.LockType lockType = currentEnvironment.getLockType();

        LogUtil.i(TAG, "start unlock: lockType =" + lockType);
        switch (lockType) {
            case LOCK_TYPE_NONE://直接解锁
                unlockScreen();
                break;
            case LOCK_TYPE_PASSWORD://启动密码锁界面
                if (invoker!=null && invoker instanceof LockManagerControl) {
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.replace(((LockManagerControl) invoker).getFragmentContainerResId(),
                            UnlockFragment.newInstance(UnlockFragment.DISPLAY_TYPE_PASSWORD));
                    transaction.commit();
                    ((LockManagerControl) invoker).hideDrawer();
                }
                break;
            case LOCK_TYPE_PATTERN://启动手势锁界面
                if (invoker!=null && invoker instanceof LockManagerControl) {
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.replace(((LockManagerControl) invoker).getFragmentContainerResId(),
                            UnlockFragment.newInstance(UnlockFragment.DISPLAY_TYPE_PATTERN));
                    transaction.commit();
                    ((LockManagerControl) invoker).hideDrawer();
                }
                break;
        }

    }

    /**
     * 解锁屏幕
     * 结束所有activity
     */
    public void unlockScreen() {
        LogUtil.i(TAG,"screen unlock success!");

        if(startIntent!=null){
            LogUtil.i(TAG,"start intent = "+startIntent.toString());
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(startIntent);
            startIntent = null;
        }
        for(Activity activity:activityList){
            activity.finish();
        }

    }

    public void registerLockActivity(Activity  activity){
        if(activity!=null && activity instanceof LockManagerControl)
        activityList.add(activity);
    }

    /**
     * 用户设置场景与当前场景进行同步
     * 当前场景状态改变时进行同步
     *
     * @return 符合当前状态的场景
     */
    public void syncCurrentEnvironment() {
        LogUtil.d(TAG, "Start syncCurrentEnvironment...");

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

            if (choose != null) {
                LogUtil.i(TAG, "current Environment set to" + choose.toString());
                setCurrentEnvironment(choose);
            }
        } else if (checkResult.size() == 1) {
            LogUtil.i(TAG, "current Environment set to:" + checkResult.get(0).toString());
            setCurrentEnvironment(checkResult.get(0));
        } else {
            LogUtil.i(TAG, "current Environment set to: SystemDefault");
            setCurrentEnvironment(getSystemDefaultEnvironment());
        }
    }

    /**
     * 在用户没有指定默认场景的情况下，系统指派一个默认的场景
     *
     * @return
     */
    private EnvironmentInfo getSystemDefaultEnvironment() {
        return new EnvironmentInfo()
                .setHint("Default")
                .setType(EnvironmentInfo.TYPE_DEFAULT)
                .setLockType(EnvironmentInfo.LockType.LOCK_TYPE_NONE);
    }

    private boolean checkEnvironment(EnvironmentInfo info) {
        switch (info.getType()) {
            case EnvironmentInfo.TYPE_WIFI:
                return checkWifiEnvironment(info);
            case EnvironmentInfo.TYPE_LOCATION:
                return checkLocationEnvironment(info);
            case EnvironmentInfo.TYPE_DEFAULT:
                return !checkLocationEnvironment(info) && !checkWifiEnvironment(info);
        }
        return false;
    }

    private boolean checkWifiEnvironment(EnvironmentInfo info) {
        return currentSSID != null
                &&
                info.getWifiSSID()!=null
                &&
                info.getWifiSSID().equals(currentSSID);
    }

    private boolean checkLocationEnvironment(EnvironmentInfo info) {
        return currentLocation!=null
                &&
                info.getLongitude()>0.001f
                &&
                info.getLatitude()>0.001f
                &&
                computeLocation(info.getLongitude(), info.getLatitude());
    }

    // TODO: 2016/8/26 可能需要异步处理，计算量较大
    private boolean computeLocation(double longitude, double latitude) {

        if (currentLocation == null) {
            LogUtil.e(TAG, "computeLocation: currentLocation is NULL !");
            return false;
        }

        LogUtil.d(TAG, "computeLocation: startLatitude=" + latitude + " startLongitude=" + longitude);
        LogUtil.d(TAG, "computeLocation: endLatitude=" + currentLocation.getLatitude()
                + " endLongitude=" + currentLocation.getLongitude());

        float[] result = new float[3];
        Location.distanceBetween(
                latitude,
                longitude,
                currentLocation.getLatitude(),
                currentLocation.getLongitude(),
                result);

        if (result.length < 1) {
            LogUtil.d(TAG, "computeLocation: wrong result !");
            return false;
        } else {
            if (result[0] <= locationError) {
                return true;
            }
        }
        return false;
    }

    public LockManager setCurrentEnvironment(EnvironmentInfo currentEnvironment) {
        this.currentEnvironment = currentEnvironment;
        return this;
    }

    public EnvironmentInfo getCurrentEnvironment() {
        return currentEnvironment;
    }

    public void updateWifiState(String currentSSID) {
        this.currentSSID = currentSSID;
        if (currentSSID != null) {
            LogUtil.i(TAG, "current wifi update to " + currentSSID);
        }
    }

    public void updateLocationState(Location location) {
        this.currentLocation = location;
        if (location != null) {

            LogUtil.i(TAG, "current location update to " + location.toString());
        }
    }


    /**
     * 验证数字密码
     *
     * @param password
     * @return
     */
    public boolean verifyPassword(String password) {
        if (currentEnvironment == null ||
                currentEnvironment.getLockType() == null ||
                currentEnvironment.getLockType() != EnvironmentInfo.LockType.LOCK_TYPE_PASSWORD) {
            return false;
        }
        return SystemUtil.encryptString(password).equals(currentEnvironment.getPassword());
    }


    /**
     * 验证手势密码
     *
     * @param password
     * @return
     */
    public boolean verifyPatternPassword(String password) {
        if (currentEnvironment == null ||
                currentEnvironment.getLockType() == null ||
                currentEnvironment.getLockType() != EnvironmentInfo.LockType.LOCK_TYPE_PATTERN) {
            return false;
        }
        return SystemUtil.encryptString(password).equals(currentEnvironment.getPatternPassword());
    }


    public interface LockManagerControl {
        int getFragmentContainerResId();

        void showDrawer();

        void hideDrawer();

    }
}
