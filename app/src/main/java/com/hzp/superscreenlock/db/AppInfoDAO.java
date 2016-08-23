package com.hzp.superscreenlock.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


/**
 * Created by hezhipeng on 2016/8/23.
 * //todo 异步处理
 */
public class AppInfoDAO {

    private SQLiteDatabase db;
    private boolean DbReady = false;

    public AppInfoDAO(Context context) {
        AppInfoDBHelper dbHelper = new AppInfoDBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public boolean isDbReady(){
        return db==null;
    }

    /**
     * 插入一条app数据
     * @param info {packageName,label,icon,intent}
     */
    public void insertAppInfo(String[] info){
        if(info==null || info.length==0){
            return;
        }
        ContentValues values = new ContentValues();
        values.put(TableAppInfo.AppInfoEntry.COLUMN_NAME_PACKAGE,info[0]);
        values.put(TableAppInfo.AppInfoEntry.COLUMN_NAME_APP_LABEL,info[1]);
        values.put(TableAppInfo.AppInfoEntry.COLUMN_NAME_APP_LABEL,info[2]);
        values.put(TableAppInfo.AppInfoEntry.COLUMN_NAME_START_INTENT,info[3]);

        db.insert(TableAppInfo.AppInfoEntry.TABLE_NAME,null,values);
    }

    public void removeAppInfo(String packageName){
        String selection = TableAppInfo.AppInfoEntry.COLUMN_NAME_PACKAGE + " =  ?";
        String[] selectionArgs = { packageName };
        db.delete(TableAppInfo.AppInfoEntry.TABLE_NAME,selection,selectionArgs);
    }

}
