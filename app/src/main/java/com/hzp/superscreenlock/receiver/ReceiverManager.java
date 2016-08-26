package com.hzp.superscreenlock.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;

import com.hzp.superscreenlock.utils.LogUtil;

/**
 * Created by hezhipeng on 2016/8/25.
 */
public class ReceiverManager {

    private static final String TAG = "ReceiverManager";
    private static ReceiverManager instance;

    private Context context;

    private boolean receiversRegistered = false;

    private ScreenReceiver screenReceiver;
    private NetworkReceiver networkReceiver;


    private ReceiverManager() {
    }

    public static ReceiverManager getInstance() {
        if (instance == null) {
            synchronized (ReceiverManager.class) {
                if (instance == null) {
                    instance = new ReceiverManager();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        this.context = context;

        screenReceiver = new ScreenReceiver();
        networkReceiver = new NetworkReceiver();
    }

    public void registerReceivers() {
        IntentFilter screenReceiverFilter = new IntentFilter();
        screenReceiverFilter.addAction(Intent.ACTION_SCREEN_OFF);
        context.registerReceiver(screenReceiver, screenReceiverFilter);
        LogUtil.i(TAG,"screenReceiver registered");

        IntentFilter networkReceiverFilter = new IntentFilter();
        networkReceiverFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        networkReceiverFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        context.registerReceiver(networkReceiver,networkReceiverFilter);
        LogUtil.i(TAG,"networkReceiver registered");

        receiversRegistered = true;
    }

    public void unregisterReceivers() {
        context.unregisterReceiver(screenReceiver);
        context.unregisterReceiver(networkReceiver);

        receiversRegistered = false;
    }

    public boolean isReceiversRegistered() {
        return receiversRegistered;
    }
}
