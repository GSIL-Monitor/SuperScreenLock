package com.hzp.superscreenlock.manager;

import android.content.Context;

import com.hzp.superscreenlock.entity.EnvironmentInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hezhipeng on 2016/8/25.
 */
public class EnvironmentManager {
    private static EnvironmentManager instance;
    private Context context;

    private EnvironmentManager() {/*empty*/}

    public static EnvironmentManager getInstance() {
        if (instance == null) {
            synchronized (AppInfoManager.class) {
                if (instance == null) {
                    instance = new EnvironmentManager();
                }
            }
        }
        return instance;
    }

    // TODO: 2016/8/25
    public List<EnvironmentInfo> getAllItems(){
        return new ArrayList<>();
    }
}
