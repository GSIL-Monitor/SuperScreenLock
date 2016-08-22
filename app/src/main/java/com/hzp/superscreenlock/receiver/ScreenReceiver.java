package com.hzp.superscreenlock.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hzp.superscreenlock.activity.LockScreenActivity;

public class ScreenReceiver extends BroadcastReceiver {
    public ScreenReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            launchLockScreen(context);
        }
    }

    private void launchLockScreen(Context context){
        Intent mLockIntent = new Intent(context, LockScreenActivity.class);
        mLockIntent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        //启动锁屏页
        context.startActivity(mLockIntent);
    }
}
