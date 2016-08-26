package com.hzp.superscreenlock.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;

import com.hzp.superscreenlock.locker.LockManager;
import com.hzp.superscreenlock.manager.MyLocationManager;
import com.hzp.superscreenlock.receiver.ReceiverManager;
import com.hzp.superscreenlock.utils.LogUtil;
import com.hzp.superscreenlock.utils.SystemUtil;

public class BaseService extends Service {
    private static final String TAG = "BaseService";

    public static final String ACTION_WIFI_CONNECTED = "ACTION_WIFI_CONNECTED";//连接上某个wifi
    public static final String ACTION_START_SERVICE = "ACTION_START_SERVICE";//启动服务
    public static final String ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE";
    public static final String ACTION_LOCATION_CHANGED = "ACTION_LOCATION_CHANGED";

    public BaseService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent.getAction()!=null){
            switch (intent.getAction()){
                case ACTION_START_SERVICE:
                    serviceStart();
                    break;
                case ACTION_WIFI_CONNECTED:
                    handleWifiConnected();
                    break;
                case ACTION_LOCATION_CHANGED:
                    handleLocationChanged();
                    break;
                case ACTION_STOP_SERVICE:
                    serviceStop();
                    break;
            }
        }
        return START_STICKY;
    }


    private void serviceStart(){
        if (!ReceiverManager.getInstance().isReceiversRegistered()) {
            ReceiverManager.getInstance().registerReceivers();
        }
        MyLocationManager.getInstance().startRequestLocationUpdates();

        handleWifiConnected();
        handleLocationChanged();
    }

    private void serviceStop(){
        if (ReceiverManager.getInstance().isReceiversRegistered()) {
            ReceiverManager.getInstance().unregisterReceivers();
        }
        MyLocationManager.getInstance().stopRequestLocationUpdates();
        stopSelf();
    }

    private void handleWifiConnected(){
        String wifiSSID = SystemUtil.getCurrentWifiSSID(this);
        LockManager.getInstance().updateWifiState(wifiSSID);
        LockManager.getInstance().syncCurrentEnvironment();
    }

    private void handleLocationChanged() {
        Location location= MyLocationManager.getInstance().getCurrentLocation();
        LockManager.getInstance().updateLocationState(location);
        LockManager.getInstance().syncCurrentEnvironment();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.i(TAG,"<======= onDestroy ==========>");
        //重启服务防止监听失效
        startService(new Intent(this, BootService.class));
    }
}
