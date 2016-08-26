package com.hzp.superscreenlock.manager;

import android.content.Context;

import com.hzp.superscreenlock.db.AppInfoDAO;
import com.hzp.superscreenlock.entity.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hezhipeng on 2016/8/23.
 * 应用信息管理类
 * 控制需要显示在界面的应用图标，etc
 */
public class AppInfoManager {
    private static AppInfoManager instance;
    private Context context;

    private AppInfoDAO dao;

    private AppInfoManager() {/*empty*/}

    public static AppInfoManager getInstance() {
        if (instance == null) {
            synchronized (AppInfoManager.class) {
                if (instance == null) {
                    instance = new AppInfoManager();
                }
            }
        }
        return instance;
    }

    public void init(Context context){
        this.context = context;
        dao=new AppInfoDAO(context);
    }

    /**
     * 获得需要显示在主页面列表上的图标
     *
     * @return
     */
    public List<AppInfo> getAppInfoDisplayOnMain() {
        List<AppInfo> list = new ArrayList<>();
        //// TODO: 2016/8/23 temp test
        for (int i = 0; i < 4; i++) {
            list.add(new AppInfo());
        }
        return list;
    }

}
