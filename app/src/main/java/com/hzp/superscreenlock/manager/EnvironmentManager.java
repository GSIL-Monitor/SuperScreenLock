package com.hzp.superscreenlock.manager;

import android.content.Context;
import android.nfc.Tag;

import com.hzp.superscreenlock.db.EnvironmentInfoDAO;
import com.hzp.superscreenlock.entity.EnvironmentInfo;
import com.hzp.superscreenlock.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hezhipeng on 2016/8/25.
 */
public class EnvironmentManager {
    private static final String TAG = EnvironmentManager.class.getSimpleName();
    private static EnvironmentManager instance;
    private Context context;

    private EnvironmentInfoDAO dao;

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

    public void init(Context context){
        this.context = context;
        dao=new EnvironmentInfoDAO(context);
    }

    public List<EnvironmentInfo> getAllItems(){
        return dao.queryItems();
    }

    public void saveItem(EnvironmentInfo info){
        LogUtil.i(TAG,"save env item = "+info.toString());
        dao.insertItem(info);
    }

    public void deleteItems(List<EnvironmentInfo> list){
        for (EnvironmentInfo i :
                list) {
            dao.removeItem(i.getTitle());
        }
    }

    public boolean checkTitle(String title){
        return dao.queryItemByTitle(title)==null;
    }

}
