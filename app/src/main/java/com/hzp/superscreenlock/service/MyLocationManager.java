package com.hzp.superscreenlock.service;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;

import com.hzp.superscreenlock.AppConstant;

/**
 * Created by hezhipeng on 2016/8/25.
 */
public class MyLocationManager {
    public static final String TAG = "MyLocationManager";
    private final boolean logEnable = AppConstant.env.isLogEnable();

    private static MyLocationManager instance;
    private Context context;

    private MyLocationListener listeners[] = {
            new MyLocationListener(),
            new MyLocationListener()
    };
    private LocationManager locationManager;

    private long updateMinTime = 1000 * 60;//minimum time interval between location updates, in milliseconds
    private float updateMinDistance = 500f;//minimum distance between location updates, in meters

    private MyLocationManager() {
    }

    public static MyLocationManager getInstance() {
        if (instance == null) {
            synchronized (MyLocationManager.class) {
                if (instance == null) {
                    instance = new MyLocationManager();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    //开始请求地理位置更新
    public void startRequestLocationUpdates() {
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER
                , updateMinTime
                , updateMinDistance
                , listeners[0]);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER
                , updateMinTime
                , updateMinDistance
                , listeners[1]);
    }

    //停止地理位置更新
    public void stopRequestLocationUpdates() {
        locationManager.removeUpdates(listeners[0]);
        locationManager.removeUpdates(listeners[1]);
    }

    /**
     * 获取当前位置
     * @return 可能为null且不一定精确
     */
    public Location getCurrentLocation() {
        // go in best to worst order
        for (int i = 0; i < listeners.length; i++) {
            Location l = listeners[i].current();
            if (l != null) return l;
        }
        if (logEnable) {
            Log.d(TAG, "No location received yet.");
        }
        return null;
    }

    private class MyLocationListener implements LocationListener {
        Location mLastLocation;
        boolean mValid = false;

        @Override
        public void onLocationChanged(Location newLocation) {
            if (newLocation.getLatitude() == 0.0
                    && newLocation.getLongitude() == 0.0) {
                // Hack to filter out 0.0,0.0 locations
                return;
            }
            if (logEnable && !mValid) {
                Log.d(TAG, "Got first location.");
            }
            mLastLocation.set(newLocation);
            if(logEnable){
                Log.d(TAG, "the newLocation is " + newLocation.getLongitude() + "x" + newLocation.getLatitude());
            }
            mValid = true;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle bundle) {
            if(logEnable){
                Log.e(TAG,"LocationProvider {onStatusChanged}:status-->"+status);
            }
            switch (status) {
                case LocationProvider.OUT_OF_SERVICE:
                case LocationProvider.TEMPORARILY_UNAVAILABLE: {
                    mValid = false;
                    break;
                }
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
            if(logEnable){
                Log.d(TAG, " support current " + provider);
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            if(logEnable){
                Log.d(TAG, "no support current " + provider);
            }
            mValid = false;
        }

        public Location current() {
            return mValid ? mLastLocation : null;
        }
    }
}
