package com.hzp.superscreenlock.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;


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

    public boolean isDbReady() {
        return db == null;
    }

    /**
     * 插入一条app数据
     *
     * @param info {packageName,label,icon,intent}
     */
    public void insertAppInfo(String[] info) {
        if (info == null || info.length == 0) {
            return;
        }
        ContentValues values = new ContentValues();
        values.put(TableAppInfo.AppInfoEntry.COLUMN_NAME_PACKAGE, info[0]);
        values.put(TableAppInfo.AppInfoEntry.COLUMN_NAME_APP_LABEL, info[1]);
        values.put(TableAppInfo.AppInfoEntry.COLUMN_NAME_APP_LABEL, info[2]);
        values.put(TableAppInfo.AppInfoEntry.COLUMN_NAME_START_INTENT, info[3]);

        db.insert(TableAppInfo.AppInfoEntry.TABLE_NAME, null, values);
    }

    /**
     * 删除一条数据
     * @param packageName
     */
    public void removeAppInfo(@NonNull String packageName) {
        String selection = TableAppInfo.AppInfoEntry.COLUMN_NAME_PACKAGE + " =  ?";
        String[] selectionArgs = {packageName};
        db.delete(TableAppInfo.AppInfoEntry.TABLE_NAME, selection, selectionArgs);
    }

    /**
     * 更新一条数据
     *
     * @param options {label,icon,intent}，如果某项不更新则输入空串"" 或 null
     */
    public void updateAppInfo(@NonNull String packageName, String[] options) {
        if(options.length==0){
            return;
        }

        ContentValues values = new ContentValues();
        if (options[0] != null && !options[0].isEmpty()) {
            values.put(TableAppInfo.AppInfoEntry.COLUMN_NAME_APP_LABEL, options[0]);
        }
        if (options[1] != null && !options[1].isEmpty()) {
            values.put(TableAppInfo.AppInfoEntry.COLUMN_NAME_APP_LABEL, options[1]);
        }
        if (options[2] != null && !options[2].isEmpty()) {
            values.put(TableAppInfo.AppInfoEntry.COLUMN_NAME_START_INTENT, options[2]);
        }

        String selection = TableAppInfo.AppInfoEntry.COLUMN_NAME_PACKAGE + " = ?";
        String[] selectionArgs = {packageName};

        db.update(
                TableAppInfo.AppInfoEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

}
