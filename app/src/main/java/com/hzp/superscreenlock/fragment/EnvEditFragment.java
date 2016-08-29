package com.hzp.superscreenlock.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;

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
        switch (key) {
            case PreferencesUtil.KEY_ENV_TYPE:
                if (callBack != null) {
                    String value = getPreferenceScreen().getSharedPreferences().getString(
                            key, null
                    );
                    if (value == null) {
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
                    }
                }
                break;
        }

    }

    public void clear() {
        getPreferenceScreen().getSharedPreferences().edit()
                .remove(PreferencesUtil.KEY_ENV_TITLE)
                .remove(PreferencesUtil.KEY_ENV_HINT)
                .remove(PreferencesUtil.KEY_ENV_LOCK_TYPE)
                .remove(PreferencesUtil.KEY_ENV_TYPE)
                .commit();
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
