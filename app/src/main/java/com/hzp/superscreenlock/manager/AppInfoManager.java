package com.hzp.superscreenlock.manager;

import android.content.Context;

import com.hzp.superscreenlock.db.AppInfoDAO;
import com.hzp.superscreenlock.entity.AppInfo;
import com.hzp.superscreenlock.utils.SystemUtil;

import java.util.ArrayList;
import java.util.Collections;
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

    private List<AppInfoListener> listeners = new ArrayList<>();
    private List<AppInfo> appInfoList = new ArrayList<>();

    private int bottomIconNumber = 4,slideIconNumber =4;

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


    public List<AppInfo> getStubsDisplay(int showType) {

        List<AppInfo> list = new ArrayList<>();
        for (int i = 0; i < bottomIconNumber; i++) {
            switch (showType){
                case AppInfo.SCREEN_SHOW_TYPE_BOTTOM:
                    list.add(new AppInfo());
                    break;
                case AppInfo.SCREEN_SHOW_TYPE_SLIDE:
                    list.add(new AppInfo().setScreenShowType(AppInfo.SCREEN_SHOW_TYPE_SLIDE));
                    break;
            }
        }
        return list;
    }


    public List<AppInfo> getListDisplayOnBottom(){
        List<AppInfo> list = dao.queryItemsByShowType(AppInfo.SCREEN_SHOW_TYPE_BOTTOM);

        for(AppInfo info:list){
            AppInfo temp = SystemUtil.queryAppInfo(context,info.getPkgName());
            info.setAppLabel(temp.getAppLabel())
            .setAppIcon(temp.getAppIcon())
            .setIntent(temp.getIntent());
        }

        Collections.sort(list);
        return list;
    }

    public void requestUpdateAppList(){
        SystemUtil.queryAppsInfo(context, new SystemUtil.AppsInfoQueryCallback() {
            @Override
            public void onQueryFinished(List<AppInfo> list) {
                for(AppInfoListener listener:listeners){
                    listener.onAppListUpdated(list);
                }
                appInfoList.clear();
                appInfoList.addAll(list);
            }
        });
    }

    public void addListener(AppInfoListener listener) {
        listeners.add(listener);
    }

    public void removeListener(AppInfoListener listener){
        listeners.remove(listener);
    }

    public interface AppInfoListener{
        void onAppListUpdated(List<AppInfo> list);
    }

    public void saveItem(AppInfo info){
        if(info==null){
            return;
        }
        if (checkPackage(info.getPkgName())) {
             dao.insertItem(info);
        }else{
             dao.updateItem(info);
        }
    }

    public void removeItemByTypeAndPosition(int showType,int showPosition){
        dao.removeItemByTypeAndPosition(showType,showPosition);
    }

    public boolean checkPackage(String packageName){
        return dao.queryItemByPkgName(packageName)==null;
    }

}
