package com.hzp.superscreenlock.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hzp.superscreenlock.service.BootService;

/**
 * 该监听器只能动态注册
 */
public class ScreenReceiver extends BroadcastReceiver {
    public ScreenReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Intent serviceIntent = new Intent(context, BootService.class);
            serviceIntent.setAction(BootService.ACTION_LAUNCH_MAIN_LOCK_SCREEN);
            context.startService(serviceIntent);
        }
    }


}
