package com.hzp.superscreenlock.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hzp.superscreenlock.service.BootService;
import com.hzp.superscreenlock.utils.LogUtil;

/**
 * 该监听器只能动态注册
 */
public class ScreenReceiver extends BroadcastReceiver {
    private static final String TAG = "ScreenReceiver";

    public ScreenReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            LogUtil.i(TAG,"ACTION_SCREEN_OFF received");
            Intent serviceIntent = new Intent(context, BootService.class);
            serviceIntent.setAction(BootService.ACTION_LAUNCH_MAIN_LOCK_SCREEN);
            context.startService(serviceIntent);
        }
    }


}
