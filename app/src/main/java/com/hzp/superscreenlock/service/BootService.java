package com.hzp.superscreenlock.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.hzp.superscreenlock.receiver.ScreenReceiver;

public class BootService extends Service {

    private ScreenReceiver screenReceiver;

    public BootService() {
        screenReceiver = new ScreenReceiver();
    }

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IntentFilter mScreenOffFilter = new IntentFilter();
        mScreenOffFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenReceiver, mScreenOffFilter);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(screenReceiver);
        //重启服务防止监听失效
        startService(new Intent(this,BootService.class));
    }
}
