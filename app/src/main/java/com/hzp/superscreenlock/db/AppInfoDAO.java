package com.hzp.superscreenlock.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.hzp.superscreenlock.entity.AppInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by hezhipeng on 2016/8/23.
 * //todo 异步处理
 */
public class AppInfoDAO extends BaseDAO {

    private SQLiteDatabase db;
    private boolean DbReady = false;

    public AppInfoDAO(Context context) {
        InfoDbHelper dbHelper = new InfoDbHelper(context);
        db = dbHelper.getWritableDatabase();
        if (db != null) {
            DbReady = true;
        }
    }

    public boolean isDbReady() {
        return db == null;
    }

    /**
     * 插入一条数据
     */
    public void insertItem(AppInfo info) {
        if (info == null || info.isEmpty()) {
            return;
        }
        ContentValues values = new ContentValues();
        values.put(DbTables.AppInfo.Entry.COLUMN_NAME_PACKAGE, info.getPkgName());
        values.put(DbTables.AppInfo.Entry.COLUMN_NAME_SHOW_TYPE, info.getScreenShowType());
        values.put(DbTables.AppInfo.Entry.COLUMN_NAME_SHOW_POSITION, info.getShowPosition());

        db.insert(DbTables.AppInfo.TABLE_NAME, null, values);
    }

    /**
     * 删除一条数据
     *
     * @param packageName
     */
    public void removeItem(@NonNull String packageName) {
        String selection = DbTables.AppInfo.Entry.COLUMN_NAME_PACKAGE + " =  ?";
        String[] selectionArgs = {packageName};
        db.delete(DbTables.AppInfo.TABLE_NAME, selection, selectionArgs);
    }

    public void removeItemByTypeAndPosition(int showType,int showPosition) {
        String selection =
                DbTables.AppInfo.Entry.COLUMN_NAME_SHOW_TYPE +" = ? "
                +" AND "
                + DbTables.AppInfo.Entry.COLUMN_NAME_SHOW_POSITION +" = ? ";
        String[] selectionArgs = { String.valueOf(showType), String.valueOf(showPosition)};
        db.delete(DbTables.AppInfo.TABLE_NAME, selection, selectionArgs);
    }

    /**
     * 更新一条数据
     */
    public void updateItem(AppInfo info) {
        if (info == null || TextUtils.isEmpty(info.getPkgName())) {
            return;
        }

        ContentValues values = new ContentValues();
        if (info.getShowPosition() != -1) {
            values.put(DbTables.AppInfo.Entry.COLUMN_NAME_SHOW_POSITION, info.getShowPosition());
        }
        values.put(DbTables.AppInfo.Entry.COLUMN_NAME_SHOW_TYPE, info.getScreenShowType());

        String selection = DbTables.AppInfo.Entry.COLUMN_NAME_PACKAGE + " = ?";
        String[] selectionArgs = {info.getPkgName()};

        db.update(
                DbTables.AppInfo.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public List<AppInfo> queryItemsByShowType(int showType) {

        String selection = DbTables.AppInfo.Entry.COLUMN_NAME_SHOW_TYPE+" = ? ";
        String[] selectionArgs = {String.valueOf(showType)};

        Cursor c = db.query(
                DbTables.AppInfo.TABLE_NAME,  // The table to query
                null,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        List<AppInfo> resultList = new ArrayList<>();
        try {
            if(c!=null&&c.moveToFirst()){
                do{
                    resultList.add(cursorToAppInfo(c,null));
                }while(c.moveToNext());
            }
            return resultList;
        } finally {
            if(c!=null){
                c.close();
            }
        }

    }

    public AppInfo queryItemByPkgName(@NonNull String packageName) {

        String selection = DbTables.AppInfo.Entry.COLUMN_NAME_PACKAGE + " = ?";
        String[] selectionArgs = {packageName};

        Cursor c = db.query(
                DbTables.AppInfo.TABLE_NAME,  // The table to query
                null,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        try {
            if (c.moveToFirst()) {//只有一条查询数据
                AppInfo result = new AppInfo();
                cursorToAppInfo(c,result);
                c.close();
                return result;
            } else {
                return null;
            }
        } finally {
            c.close();
        }
    }


    private AppInfo cursorToAppInfo(Cursor c,AppInfo info){
        if(info ==null){
            info = new AppInfo();
        }
        info.setPkgName(getString(c, DbTables.AppInfo.Entry.COLUMN_NAME_PACKAGE));
        info.setScreenShowType(getInteger(c, DbTables.AppInfo.Entry.COLUMN_NAME_SHOW_TYPE));
        info.setShowPosition(getInteger(c,DbTables.AppInfo.Entry.COLUMN_NAME_SHOW_POSITION));
        return info;
    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        db.close();
    }

}
