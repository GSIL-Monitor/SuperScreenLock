package com.hzp.superscreenlock.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.hzp.superscreenlock.activity.LockScreenActivity;
import com.hzp.superscreenlock.receiver.ReceiverManager;
import com.hzp.superscreenlock.utils.SystemUtil;

public class BootService extends Service {

    public static final String ACTION_LAUNCH_MAIN_LOCK_SCREEN = ".LAUNCH_MAIN_LOCK_SCREEN";


    public BootService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!ReceiverManager.getInstance().isReceiversResisted()) {
            ReceiverManager.getInstance().registerReceivers();
        }
        if(intent.getAction()!=null){
            if (intent.getAction().equals(ACTION_LAUNCH_MAIN_LOCK_SCREEN)) {
                launchLockScreen();
            }

        }

        // TODO: 2016/8/26 先检测是否已经存在
        bootOtherServices();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ReceiverManager.getInstance().isReceiversResisted()) {
            ReceiverManager.getInstance().unregisterReceivers();
        }
        //重启服务防止监听失效
        startService(new Intent(this, BootService.class));
    }

    private void bootOtherServices() {
        bootBaseService();
    }

    private void bootBaseService(){
        String wifiSSID = SystemUtil.getCurrentWifiSSID(this);

        Intent serviceIntent = new Intent(this, BaseService.class);
        serviceIntent.setAction(BaseService.ACTION_WIFI_CONNECTED);
        serviceIntent.putExtra("wifiSSID",wifiSSID);

        startService(serviceIntent);
    }

    private void launchLockScreen() {
        Intent mLockIntent = new Intent(this, LockScreenActivity.class);
        mLockIntent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        //启动锁屏页
        startActivity(mLockIntent);
    }
}
