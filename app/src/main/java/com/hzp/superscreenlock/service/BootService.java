package com.hzp.superscreenlock.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.hzp.superscreenlock.activity.LockScreenActivity;
import com.hzp.superscreenlock.receiver.ReceiverManager;
import com.hzp.superscreenlock.receiver.ScreenReceiver;

public class BootService extends Service {

    public static final String ACTION_LAUNCH_MAIN_LOCK_SCREEN =".LAUNCH_MAIN_LOCK_SCREEN";


    public BootService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!ReceiverManager.getInstance().isReceiversRegisted()) {
            ReceiverManager.getInstance().registerReceivers();
        }

        if(intent.getAction()!=null&&intent.getAction().equals(ACTION_LAUNCH_MAIN_LOCK_SCREEN)){
            launchLockScreen();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ReceiverManager.getInstance().isReceiversRegisted()) {
            ReceiverManager.getInstance().unregisterReceivers();
        }
        //重启服务防止监听失效
        startService(new Intent(this, BootService.class));
    }

    private void launchLockScreen(){
        Intent mLockIntent = new Intent(this, LockScreenActivity.class);
        mLockIntent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        //启动锁屏页
        startActivity(mLockIntent);
    }
}
