package com.hzp.superscreenlock.manager;

import android.content.Context;
import android.os.Build;

import com.hzp.superscreenlock.R;
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

    public static final String STUB_PACKAGE_NAME= "com.hzp.superscreenlock.stub";

    private Context context;

    private AppInfoDAO dao;

    private List<AppInfoListener> listeners = new ArrayList<>();
    private List<AppInfo> appInfoList = new ArrayList<>();

    private int bottomIconNumber = 4, slideIconNumber = 4;

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

    public void init(Context context) {
        this.context = context;
        dao = new AppInfoDAO(context);
    }


    public AppInfo getStubAppInfo() {
        AppInfo stub = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            stub = new AppInfo().setAppIcon(context.getDrawable(R.drawable.ic_add_box_white_24dp));
        } else {
            stub = new AppInfo().setAppIcon(context.getResources().getDrawable(R.drawable.ic_add_box_white_24dp));
        }
        stub.setPkgName(STUB_PACKAGE_NAME);
        stub.setShowPosition(9999);
        return stub;
    }


    public List<AppInfo> getListToDisplay(int showType) {
        List<AppInfo> list = null;

        list = dao.queryItemsByShowType(showType);

        for (AppInfo info : list) {
            AppInfo temp = SystemUtil.queryAppInfo(context, info.getPkgName());

            if(temp==null && info.getPkgName().equals(STUB_PACKAGE_NAME)){
                info = getAppInfo(info.getPkgName());
                continue;
            }

            info.setAppLabel(temp.getAppLabel())
                    .setAppIcon(temp.getAppIcon())
                    .setIntent(temp.getIntent());
        }

        return list;
    }

    public void requestUpdateAppList() {
        SystemUtil.queryAppsInfo(context, new SystemUtil.AppsInfoQueryCallback() {
            @Override
            public void onQueryFinished(List<AppInfo> list) {
                for (AppInfoListener listener : listeners) {
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

    public void removeListener(AppInfoListener listener) {
        listeners.remove(listener);
    }

    public interface AppInfoListener {
        void onAppListUpdated(List<AppInfo> list);
    }

    public void saveItem(AppInfo info) {
        if (info == null) {
            return;
        }
        if (checkPackage(info.getPkgName())) {
            dao.insertItem(info);
        } else {
            AppInfo infoTemp = checkItemShowTypeChange(info);
            if (infoTemp !=null) {//显示位置showType更换，用一个stub代替
                dao.insertItem(getStubAppInfo()
                .setScreenShowType(infoTemp.getScreenShowType())
                .setShowPosition(infoTemp.getShowPosition()));

                dao.updateItem(info);
            }else{
            dao.updateItem(info);
            }
        }
    }

    public AppInfo getAppInfo(String pkgName){
        return dao.queryItemByPkgName(pkgName);
    }

    /**
     * 检查图标showType是否与已储存的内容相同
     *
     * @param info
     * @return null未改变 有返回值说明该值改变
     */
    private AppInfo checkItemShowTypeChange(AppInfo info) {
        AppInfo infoTemp = dao.queryItemByPkgName(info.getPkgName());
       if(infoTemp.getScreenShowType() != info.getScreenShowType()){
           return infoTemp;
       }else{
           return null;
       }
    }

    public void removeItemByTypeAndPosition(int showType, int showPosition) {
        dao.removeItemByTypeAndPosition(showType, showPosition);
    }

    /**
     * 检查是否已经存在
     *
     * @param packageName
     * @return
     */
    public boolean checkPackage(String packageName) {
        return dao.queryItemByPkgName(packageName) == null;
    }

    public int getSlideIconNumber() {
        return slideIconNumber;
    }

    public AppInfoManager setSlideIconNumber(int slideIconNumber) {
        this.slideIconNumber = slideIconNumber;
        return this;
    }

    public int getBottomIconNumber() {
        return bottomIconNumber;
    }

    public AppInfoManager setBottomIconNumber(int bottomIconNumber) {
        this.bottomIconNumber = bottomIconNumber;
        return this;
    }

    /**
     * 删除某个快捷图标，然后将剩下的图标重新排序以对齐
     * @param info
     */
    public void removeItemsAndSort(AppInfo info){
        List<AppInfo> listToSort = dao.queryItemsByShowType(info.getScreenShowType(),info.getShowPosition()+1);
        //删除选定的
        dao.removeItem(info.getPkgName());
        //重新排序剩下的
        for (AppInfo i :
                listToSort) {
            if (i.getShowPosition() >= 0) {
                i.setShowPosition(i.getShowPosition()-1);
                dao.updateItem(i);
            }
        }

    }
}
