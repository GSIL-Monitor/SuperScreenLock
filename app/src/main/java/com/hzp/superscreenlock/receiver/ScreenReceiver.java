package com.hzp.superscreenlock.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hzp.superscreenlock.activity.LockScreenActivity;
import com.hzp.superscreenlock.service.BaseService;
import com.hzp.superscreenlock.service.BootService;

public class ScreenReceiver extends BroadcastReceiver {
    public ScreenReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Intent serviceIntent = new Intent(context, BaseService.class);
            serviceIntent.setAction(BootService.ACTION_LAUNCH_MAIN_LOCK_SCREEN);
            context.startService(serviceIntent);
        }
    }


}
