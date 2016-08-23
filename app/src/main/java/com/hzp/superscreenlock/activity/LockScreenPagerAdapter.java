package com.hzp.superscreenlock.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import java.util.List;

/**
 * Created by hezhipeng on 2016/8/23.
 */
public class LockScreenPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> list;

    public LockScreenPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
