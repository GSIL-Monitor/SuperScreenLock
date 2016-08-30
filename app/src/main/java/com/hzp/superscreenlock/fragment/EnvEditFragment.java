package com.hzp.superscreenlock.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.text.TextUtils;

import com.hzp.superscreenlock.R;
import com.hzp.superscreenlock.entity.EnvironmentInfo;
import com.hzp.superscreenlock.utils.LogUtil;
import com.hzp.superscreenlock.utils.PreferencesUtil;


public class EnvEditFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {


    private static final String TAG = "EnvEditFragment";
    private SettingChangedCallBack callBack;

    public EnvEditFragment() {
        // Required empty public constructor
    }

    public static EnvEditFragment newInstance() {
        EnvEditFragment fragment = new EnvEditFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.env_edit);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (callBack != null) {
            String value = getPreferenceScreen().getSharedPreferences().getString(
                    key, null
            );
            if (value == null || TextUtils.isEmpty(value)) {
                return;
            }
            LogUtil.i(TAG, "start {" + value + "} detail edit");
            switch (value) {
                case "wifi":
                    callBack.onEnvironmentChanged(EnvironmentInfo.TYPE_WIFI);
                    break;
                case "location":
                    callBack.onEnvironmentChanged(EnvironmentInfo.TYPE_LOCATION);
                    break;
                case "default":
                    callBack.onEnvironmentChanged(EnvironmentInfo.TYPE_DEFAULT);
                    break;
                case "LOCK_TYPE_PASSWORD":
                    callBack.onLockTypeChanged(EnvironmentInfo.LockType.LOCK_TYPE_PASSWORD);
                    break;
                case "LOCK_TYPE_PATTERN":
                    callBack.onLockTypeChanged(EnvironmentInfo.LockType.LOCK_TYPE_PATTERN);
                    break;
                case "LOCK_TYPE_NONE":
                    callBack.onLockTypeChanged(EnvironmentInfo.LockType.LOCK_TYPE_NONE);
                    break;
            }
        }
    }

    public EnvironmentInfo getEditData(){
        EnvironmentInfo info = new EnvironmentInfo();
        SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
        info.setTitle(sp.getString(PreferencesUtil.KEY_ENV_TITLE,null))
                .setHint(sp.getString(PreferencesUtil.KEY_ENV_HINT,null))
                .setType(sp.getString(PreferencesUtil.KEY_ENV_TYPE,null))
                .setWifiSSID(sp.getString(PreferencesUtil.KEY_ENV_WIFI_SSID,null))
                .setLatitude(sp.getFloat(PreferencesUtil.KEY_ENV_LOCATION_LATITUDE,0f))
                .setLongitude(sp.getFloat(PreferencesUtil.KEY_ENV_LOCATION_LONGITUDE,0f))
                .setLockType(sp.getString(PreferencesUtil.KEY_ENV_LOCK_TYPE,null))
                .setPassword(sp.getString(PreferencesUtil.KEY_ENV_PASSWORD,null))
                .setPatternPassword(sp.getString(PreferencesUtil.KEY_ENV_PATTERN,null));

        return info;
    }

    public void clear() {
        LogUtil.i(TAG,"clear temp edit SP  data...");
        getPreferenceScreen().getSharedPreferences().edit()
                .remove(PreferencesUtil.KEY_ENV_TITLE)
                .remove(PreferencesUtil.KEY_ENV_HINT)
                .remove(PreferencesUtil.KEY_ENV_LOCK_TYPE)
                .remove(PreferencesUtil.KEY_ENV_TYPE)
                .remove(PreferencesUtil.KEY_ENV_WIFI_SSID)
                .remove(PreferencesUtil.KEY_ENV_LOCATION_LONGITUDE)
                .remove(PreferencesUtil.KEY_ENV_LOCATION_LATITUDE)
                .remove(PreferencesUtil.KEY_ENV_PASSWORD)
                .remove(PreferencesUtil.KEY_ENV_PATTERN)
                .apply();
    }

    public void setCallback(SettingChangedCallBack callback) {
        this.callBack = callback;
    }

    public interface SettingChangedCallBack {
        /**
         * @see com.hzp.superscreenlock.entity.EnvironmentInfo
         */
        void onEnvironmentChanged(String env);

        void onLockTypeChanged(EnvironmentInfo.LockType lockType);
    }
}
