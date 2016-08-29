package com.hzp.superscreenlock.locker;

import android.app.Activity;
import android.location.Location;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.hzp.superscreenlock.entity.EnvironmentInfo;
import com.hzp.superscreenlock.fragment.UnlockFragment;
import com.hzp.superscreenlock.manager.EnvironmentManager;
import com.hzp.superscreenlock.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hezhipeng on 2016/8/25.
 */
public class LockManager {
    public static final String TAG = "LockManager";

    private static LockManager instance;

    private EnvironmentInfo currentEnvironment;
    private String currentSSID;
    private Location currentLocation;

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

    /**
     * 由Manager来控制启动解锁界面的类型
     */
    public void startUnlockView(Activity invoker, FragmentManager fm) {
        if(currentEnvironment==null){
            return;
        }
        String hint = currentEnvironment.getHint();
        EnvironmentInfo.LockType lockType = currentEnvironment.getLockType();

        LogUtil.i(TAG,"start unlock: hint="+hint+" lockType="+lockType);
        // TODO: 2016/8/25 1.滑动解锁（无锁） 2.密码解锁 3.九宫格
        switch (lockType){
            case LOCK_TYPE_NONE:
            case LOCK_TYPE_PASSWORD:
            case LOCK_TYPE_PATTERN:
                if(invoker instanceof LockManagerControl){
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.replace(((LockManagerControl) invoker).getFragmentContainerResId(),
                            UnlockFragment.newInstance(UnlockFragment.DISPLAY_TYPE_NONE));
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
    public void unlockScreen(){

    }

    /**
     * 用户设置场景与当前场景进行同步
     * 当前场景状态改变时进行同步
     *
     * @return 符合当前状态的场景
     */
    public void syncCurrentEnvironment() {
        LogUtil.d(TAG,"Start syncCurrentEnvironment...");

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

            if(choose!=null){
                LogUtil.i(TAG,"current Environment set to"+choose.toString());
                setCurrentEnvironment(choose);
            }
        } else if (checkResult.size() == 1) {
            LogUtil.i(TAG,"current Environment set to:"+checkResult.get(0).toString());
            setCurrentEnvironment(checkResult.get(0));
        } else {
            LogUtil.i(TAG,"current Environment set to: SystemDefault");
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

    public boolean checkEnvironment(EnvironmentInfo info) {
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
                info.getWifiSSID().equals(currentSSID);
    }

    private boolean checkLocationEnvironment(EnvironmentInfo info) {
        return computeLocation(info.getLongitude(),info.getLatitude());
    }

    // TODO: 2016/8/26 可能需要异步处理，计算量较大
    private boolean computeLocation(double longitude, double latitude) {

        if(currentLocation==null){
            LogUtil.e(TAG,"computeLocation: currentLocation is NULL !");
            return false;
        }

        LogUtil.d(TAG,"computeLocation: startLatitude="+latitude+" startLongitude="+longitude);
        LogUtil.d(TAG,"computeLocation: endLatitude="+currentLocation.getLatitude()
                +" endLongitude="+currentLocation.getLongitude());

        float[] result = new float[3];
        Location.distanceBetween(
                latitude,
                longitude,
                currentLocation.getLatitude(),
                currentLocation.getLongitude(),
                result);

        if(result.length<1){
            LogUtil.d(TAG,"computeLocation: wrong result !" );
            return false;
        }else{
            if(result[0]<= locationError){
                return true;
            }
        }
        return false;
    }

    public LockManager setCurrentEnvironment(EnvironmentInfo currentEnvironment) {
        this.currentEnvironment = currentEnvironment;
        return this;
    }

    public void updateWifiState(String currentSSID) {
        this.currentSSID = currentSSID;
    }

    public void updateLocationState(Location location) {
        this.currentLocation = location;
    }

    public interface LockManagerControl{
        int getFragmentContainerResId();
        void showDrawer();
        void hideDrawer();
    }
}
