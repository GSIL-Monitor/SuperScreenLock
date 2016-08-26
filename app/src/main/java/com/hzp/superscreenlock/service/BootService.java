package com.hzp.superscreenlock.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.hzp.superscreenlock.activity.LockScreenActivity;
import com.hzp.superscreenlock.utils.LogUtil;

public class BootService extends Service {
    private static final String TAG = "BootService";

    public static final String ACTION_LAUNCH_MAIN_LOCK_SCREEN = ".LAUNCH_MAIN_LOCK_SCREEN";


    public BootService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent.getAction()!=null){
            switch (intent.getAction()){
                case ACTION_LAUNCH_MAIN_LOCK_SCREEN:
                    launchLockScreen();
                    break;
            }
        }

        // TODO: 2016/8/26 先检测是否已经存在
        bootOtherServices();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.i(TAG,"<======= onDestroy ==========>");
        //重启服务防止监听失效
        startService(new Intent(this, BootService.class));
    }

    private void bootOtherServices() {
        bootBaseService();
    }

    private void bootBaseService(){
        LogUtil.i(TAG,"booting BaseService...");
        Intent serviceIntent = new Intent(getApplicationContext(), BaseService.class);
        serviceIntent.setAction(BaseService.ACTION_START_SERVICE);

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
