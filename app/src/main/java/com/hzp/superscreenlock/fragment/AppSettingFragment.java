package com.hzp.superscreenlock.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.hzp.superscreenlock.R;

/**
 * @author hoholiday on 2016/8/31.
 * @email hoholiday@hotmail.com
 */

public class AppSettingFragment extends PreferenceFragment {

    public AppSettingFragment() {
        // Required empty public constructor
    }

    public static AppSettingFragment newInstance() {
        AppSettingFragment fragment = new AppSettingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.app_setting);
    }
}
