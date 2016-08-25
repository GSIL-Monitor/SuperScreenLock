package com.hzp.superscreenlock.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.hzp.superscreenlock.entity.EnvironmentInfo;

/**
 * Created by hezhipeng on 2016/8/25.
 */
public class EnvironmentInfoDAO extends BaseDAO{

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
     *
     */
    public void insertItem(EnvironmentInfo info) {
        if (info == null) {
            return;
        }
        ContentValues values = new ContentValues();
        values.put(DbTables.EnvironmentInfo.Entry.COLUMN_NAME_TITLE, info.getTitle());
        values.put(DbTables.EnvironmentInfo.Entry.COLUMN_NAME_HINT, info.getHint());
        values.put(DbTables.EnvironmentInfo.Entry.COLUMN_NAME_TYPE, info.getType());

        switch (info.getType()){
            case EnvironmentInfo.LOCK_TYPE_WIFI:
                values.put(DbTables.EnvironmentInfo.Entry.COLUMN_NAME_SSID,info.getWifiSSID());
                break;
            case EnvironmentInfo.LOCK_TYPE_GPS:
                values.put(DbTables.EnvironmentInfo.Entry.COLUMN_NAME_LATITUDE,info.getLatitude());
                values.put(DbTables.EnvironmentInfo.Entry.COLUMN_NAME_LONGITUDE,info.getLongitude());
                break;
            case EnvironmentInfo.LOCK_TYPE_UNKNOWN:
                break;
        }

        values.put(DbTables.EnvironmentInfo.Entry.COLUMN_NAME_LOCK_TYPE,info.getLockType());

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
        if(info==null || TextUtils.isEmpty(info.getTitle())){
            return;
        }

        ContentValues values = new ContentValues();
        if(info.getHint()!=null){
            values.put(DbTables.EnvironmentInfo.Entry.COLUMN_NAME_HINT,info.getHint());
        }

        if(info.getType()!=null){
            values.put(DbTables.EnvironmentInfo.Entry.COLUMN_NAME_TYPE, info.getType());

            switch (info.getType()){
                case EnvironmentInfo.LOCK_TYPE_WIFI:
                    values.put(DbTables.EnvironmentInfo.Entry.COLUMN_NAME_SSID,info.getWifiSSID());
                    break;
                case EnvironmentInfo.LOCK_TYPE_GPS:
                    values.put(DbTables.EnvironmentInfo.Entry.COLUMN_NAME_LATITUDE,info.getLatitude());
                    values.put(DbTables.EnvironmentInfo.Entry.COLUMN_NAME_LONGITUDE,info.getLongitude());
                    break;
                case EnvironmentInfo.LOCK_TYPE_UNKNOWN:
                    break;
            }
        }

        if(info.getLockType()!=null){
            values.put(DbTables.EnvironmentInfo.Entry.COLUMN_NAME_LOCK_TYPE,info.getLockType());
        }

        String selection = DbTables.EnvironmentInfo.Entry.COLUMN_NAME_TITLE + " = ?";
        String[] selectionArgs = {info.getTitle()};

        db.update(
                DbTables.EnvironmentInfo.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public EnvironmentInfo queryItem(@NonNull String title){

        String selection = DbTables.EnvironmentInfo.Entry.COLUMN_NAME_TITLE+" = ?";
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

        try{
            if(c.moveToFirst()){//只有一条查询数据
                EnvironmentInfo result = new EnvironmentInfo();

                result.setTitle(getString(c, DbTables.EnvironmentInfo.Entry.COLUMN_NAME_TITLE) );
                result.setHint(getString(c,DbTables.EnvironmentInfo.Entry.COLUMN_NAME_HINT));
                result.setType(getString(c,DbTables.EnvironmentInfo.Entry.COLUMN_NAME_TYPE) );

                switch (result.getType()){
                    case EnvironmentInfo.LOCK_TYPE_WIFI:
                        result.setWifiSSID(getString(c, DbTables.EnvironmentInfo.Entry.COLUMN_NAME_SSID));
                        break;
                    case EnvironmentInfo.LOCK_TYPE_GPS:
                        result.setLongitude(getLong(c, DbTables.EnvironmentInfo.Entry.COLUMN_NAME_LONGITUDE));
                        result.setLatitude(getLong(c, DbTables.EnvironmentInfo.Entry.COLUMN_NAME_LATITUDE));
                        break;
                    case EnvironmentInfo.LOCK_TYPE_UNKNOWN:
                        break;
                }

                result.setLockType(getString(c, DbTables.EnvironmentInfo.Entry.COLUMN_NAME_LOCK_TYPE));

                c.close();
                return result;

            }else{
                return null;
            }
        }
        finally{
            c.close();
        }

    }
}
