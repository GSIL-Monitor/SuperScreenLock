package com.hzp.superscreenlock;

import android.app.Application;
import android.content.Intent;

import com.hzp.superscreenlock.service.BootService;

/**
 * Created by hezhipeng on 2016/8/22.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this,BootService.class));
    }
}
