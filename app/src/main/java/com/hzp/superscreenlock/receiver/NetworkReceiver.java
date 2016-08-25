package com.hzp.superscreenlock.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;

import com.hzp.superscreenlock.service.BaseService;
import com.hzp.superscreenlock.utils.LogUtil;

public class NetworkReceiver extends BroadcastReceiver {
    public static final String TAG = "NetworkReceiver";

    public NetworkReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case WifiManager.NETWORK_STATE_CHANGED_ACTION://这个监听wifi的连接状态即是否连上了一个有效无线路由
                handleWifiChange(context, intent);
                break;
        }
    }

    private void handleWifiChange(Context context, Intent intent) {
        LogUtil.i(TAG, "handle action NETWORK_STATE_CHANGED_ACTION ");


        Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if (null != parcelableExtra) {
            NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
            NetworkInfo.State state = networkInfo.getState();
            boolean isConnected = state == NetworkInfo.State.CONNECTED;
            LogUtil.i(TAG, "Wifi connect state =" + state);
            if (isConnected) {
                Intent serviceIntent = new Intent(context, BaseService.class);
                serviceIntent.setAction(BaseService.ACTION_WIFI_CONNECTED);
                context.startService(serviceIntent);
            }
        }
    }
}
