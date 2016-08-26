package com.hzp.superscreenlock.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.hzp.superscreenlock.locker.LockManager;

public class BaseService extends Service {

    public static final String ACTION_WIFI_CONNECTED = "ACTION_WIFI_CONNECTED";//连接上某个wifi

    public BaseService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        switch (intent.getAction()){
            case ACTION_WIFI_CONNECTED:
                handleWifiConnected(intent.getStringExtra("wifiSSID"));
                break;

        }
        return START_STICKY;
    }

    private void handleWifiConnected(String SSID){
        LockManager.getInstance().updateWifiState(SSID);
    }
}
