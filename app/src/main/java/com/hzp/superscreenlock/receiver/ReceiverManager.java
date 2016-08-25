package com.hzp.superscreenlock.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by hezhipeng on 2016/8/25.
 */
public class ReceiverManager {

    private static ReceiverManager instance;

    private Context context;

    private boolean receiversRegisted = false;

    private ScreenReceiver screenReceiver;
    private PhoneStateReceiver phoneStateReceiver;

    private ReceiverManager(){
    }

    public static ReceiverManager getInstance(){
        if(instance==null){
            synchronized (ReceiverManager.class){
                if(instance==null){
                    instance=new ReceiverManager();
                }
            }
        }
        return instance;
    }

    public void init(Context context){
        this.context=context;

        screenReceiver= new ScreenReceiver();
        phoneStateReceiver = new PhoneStateReceiver();
    }

    public void registerReceivers(){
        IntentFilter mScreenOffFilter = new IntentFilter();
        mScreenOffFilter.addAction(Intent.ACTION_SCREEN_OFF);
        context.registerReceiver(screenReceiver, mScreenOffFilter);

        // TODO: 2016/8/25 phonestate receiver

        receiversRegisted = true;
    }

    public void unregisterReceivers(){
        context.unregisterReceiver(screenReceiver);

        receiversRegisted = false;
    }

    public boolean isReceiversRegisted() {
        return receiversRegisted;
    }
}
