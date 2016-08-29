package com.hzp.superscreenlock;

import android.app.Application;
import android.content.Intent;

import com.baidu.mapapi.SDKInitializer;
import com.hzp.superscreenlock.db.DbTables;
import com.hzp.superscreenlock.manager.AppInfoManager;
import com.hzp.superscreenlock.manager.EnvironmentManager;
import com.hzp.superscreenlock.receiver.ReceiverManager;
import com.hzp.superscreenlock.service.BaseService;
import com.hzp.superscreenlock.service.BootService;
import com.hzp.superscreenlock.manager.MyLocationManager;

/**
 * Created by hezhipeng on 2016/8/22.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        SDKInitializer.initialize(this);

        initManagers();

        startService(new Intent(this, BootService.class));
        startService(new Intent(this, BaseService.class));
    }

    private void initManagers() {
        AppInfoManager.getInstance().init(this);
        EnvironmentManager.getInstance().init(this);
        ReceiverManager.getInstance().init(this);
        MyLocationManager.getInstance().init(this);
    }
}
