package com.hzp.superscreenlock.service;

import android.content.Context;

/**
 * Created by hezhipeng on 2016/8/23.
 * 应用信息管理类
 * 控制需要显示在界面的应用图标，etc
 */
public class AppInfoManager {
    private static AppInfoManager instance;
    private Context context;

    private AppInfoManager(){/*empty*/}

    public static AppInfoManager getInstance(){
        if(instance==null){
            synchronized (AppInfoManager.class){
                if(instance==null){
                    instance=new AppInfoManager();
                }
            }
        }
        return instance;
    }

    public void init(Context context){
        this.context = context;
    }
}
