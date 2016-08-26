package com.hzp.superscreenlock.db;

import android.database.Cursor;

/**
 * Created by hezhipeng on 2016/8/25.
 */
public class BaseDAO {

    protected String getString(Cursor c, String columnName) {
        return c.getString(
                c.getColumnIndexOrThrow(columnName)
        );
    }

    protected long getLong(Cursor c, String columnName) {
        return c.getLong(
                c.getColumnIndexOrThrow(columnName)
        );
    }

    protected double getDouble(Cursor c, String columnName) {
        return c.getDouble(
                c.getColumnIndexOrThrow(columnName)
        );
    }

}
