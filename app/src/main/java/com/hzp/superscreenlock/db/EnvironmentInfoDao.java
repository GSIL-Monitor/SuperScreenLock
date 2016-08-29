package com.hzp.superscreenlock.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.hzp.superscreenlock.entity.EnvironmentInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hezhipeng on 2016/8/25.
 */
public class EnvironmentInfoDAO extends BaseDAO {

    private boolean DbReady;
    private SQLiteDatabase db;

    public EnvironmentInfoDAO(Context context) {
        InfoDbHelper dbHelper = new InfoDbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public boolean isDbReady() {
        return db == null;
    }

    /**
     * 插入一条数据
     */
    public void insertItem(EnvironmentInfo info) {
        if (info == null) {
            return;
        }
        ContentValues values = new ContentValues();
        values.put(DbTables.EnvironmentInfo.Entry.COLUMN_NAME_TITLE, info.getTitle());
        values.put(DbTables.EnvironmentInfo.Entry.COLUMN_NAME_HINT, info.getHint());
        values.put(DbTables.EnvironmentInfo.Entry.COLUMN_NAME_TYPE, info.getType());

        switch (info.getType()) {
            case EnvironmentInfo.TYPE_WIFI:
                values.put(DbTables.EnvironmentInfo.Entry.COLUMN_NAME_SSID, info.getWifiSSID());
                break;
            case EnvironmentInfo.TYPE_LOCATION:
                values.put(DbTables.EnvironmentInfo.Entry.COLUMN_NAME_LATITUDE, info.getLatitude());
                values.put(DbTables.EnvironmentInfo.Entry.COLUMN_NAME_LONGITUDE, info.getLongitude());
                break;
            case EnvironmentInfo.TYPE_DEFAULT:
                break;
        }

        values.put(DbTables.EnvironmentInfo.Entry.COLUMN_NAME_LOCK_TYPE, info.getLockType().toString());

        db.insert(DbTables.AppInfo.TABLE_NAME, null, values);
    }

    /**
     * 删除一条数据
     */
    public void removeItem(@NonNull String title) {
        String selection = DbTables.EnvironmentInfo.Entry.COLUMN_NAME_TITLE + " =  ?";
        String[] selectionArgs = {title};
        db.delete(DbTables.EnvironmentInfo.TABLE_NAME, selection, selectionArgs);
    }

    /**
     * 更新一条数据
     * 根据title索引
     */
    public void updateItem(EnvironmentInfo info) {
        if (info == null || TextUtils.isEmpty(info.getTitle())) {
            return;
        }

        ContentValues values = new ContentValues();
        if (info.getHint() != null) {
            values.put(DbTables.EnvironmentInfo.Entry.COLUMN_NAME_HINT, info.getHint());
        }

        if (info.getType() != null) {
            values.put(DbTables.EnvironmentInfo.Entry.COLUMN_NAME_TYPE, info.getType());

            switch (info.getType()) {
                case EnvironmentInfo.TYPE_WIFI:
                    values.put(DbTables.EnvironmentInfo.Entry.COLUMN_NAME_SSID, info.getWifiSSID());
                    break;
                case EnvironmentInfo.TYPE_LOCATION:
                    values.put(DbTables.EnvironmentInfo.Entry.COLUMN_NAME_LATITUDE, info.getLatitude());
                    values.put(DbTables.EnvironmentInfo.Entry.COLUMN_NAME_LONGITUDE, info.getLongitude());
                    break;
                case EnvironmentInfo.TYPE_DEFAULT:
                    break;
            }
        }

        if (info.getLockType() != null) {
            values.put(DbTables.EnvironmentInfo.Entry.COLUMN_NAME_LOCK_TYPE, info.getLockType().toString());
        }

        String selection = DbTables.EnvironmentInfo.Entry.COLUMN_NAME_TITLE + " = ?";
        String[] selectionArgs = {info.getTitle()};

        db.update(
                DbTables.EnvironmentInfo.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public EnvironmentInfo queryItemByTitle(@NonNull String title) {

        String selection = DbTables.EnvironmentInfo.Entry.COLUMN_NAME_TITLE + " = ?";
        String[] selectionArgs = {title};

        Cursor c = db.query(
                DbTables.EnvironmentInfo.TABLE_NAME,  // The table to query
                null,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        try {
            if (c!=null && c.moveToFirst()) {//只有一条查询数据
                EnvironmentInfo result = new EnvironmentInfo();
                cursorToEnvironment(c,result);
                return result;
            } else {
                return null;
            }
        } finally {
            if(c!=null){
            c.close();
            }
        }

    }

    public List<EnvironmentInfo> queryItems() {

        Cursor c = db.query(
                DbTables.EnvironmentInfo.TABLE_NAME,  // The table to query
                null,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        List<EnvironmentInfo> resultList = new ArrayList<>();
        try {
            if(c!=null&&c.moveToFirst()){
                do{
                    resultList.add(cursorToEnvironment(c,null));
                }while(c.moveToNext());
            }
            return resultList;
        } finally {
            if(c!=null){
            c.close();
            }
        }

    }

    public List<EnvironmentInfo> queryItemsByType(@NonNull String type) {

        String selection = DbTables.EnvironmentInfo.Entry.COLUMN_NAME_TYPE + " = ?";
        String[] selectionArgs = {type};

        Cursor c = db.query(
                DbTables.EnvironmentInfo.TABLE_NAME,  // The table to query
                null,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        List<EnvironmentInfo> resultList = new ArrayList<>();
        try {
            if(c!=null&&c.moveToFirst()){
                do{
                    resultList.add(cursorToEnvironment(c,null));
                }while(c.moveToNext());
            }
            return resultList;
        } finally {
            if(c!=null){
                c.close();
            }
        }

    }

    private EnvironmentInfo cursorToEnvironment(Cursor c , EnvironmentInfo info){
        if(info==null){
            info = new EnvironmentInfo();
        }
        info.setTitle(getString(c, DbTables.EnvironmentInfo.Entry.COLUMN_NAME_TITLE));
        info.setHint(getString(c, DbTables.EnvironmentInfo.Entry.COLUMN_NAME_HINT));
        info.setType(getString(c, DbTables.EnvironmentInfo.Entry.COLUMN_NAME_TYPE));

        switch (info.getType()) {
            case EnvironmentInfo.TYPE_WIFI:
                info.setWifiSSID(getString(c, DbTables.EnvironmentInfo.Entry.COLUMN_NAME_SSID));
                break;
            case EnvironmentInfo.TYPE_LOCATION:
                info.setLongitude(getDouble(c, DbTables.EnvironmentInfo.Entry.COLUMN_NAME_LONGITUDE));
                info.setLatitude(getDouble(c, DbTables.EnvironmentInfo.Entry.COLUMN_NAME_LATITUDE));
                break;
            case EnvironmentInfo.TYPE_DEFAULT:
                break;
        }

        info.setLockType(
                getString(c, DbTables.EnvironmentInfo.Entry.COLUMN_NAME_LOCK_TYPE)
        );

        return info;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        db.close();
    }
}
