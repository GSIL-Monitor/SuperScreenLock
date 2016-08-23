package com.hzp.superscreenlock.db;

import android.provider.BaseColumns;

/**
 * Created by hezhipeng on 2016/8/23.
 */
public class TableAppInfo {
    private static final String COMMA_SEP = ",";
    private static final String TEXT_TYPE = " TEXT";

    public static final String SQL_CREATE_ENTRIES=
            "CREATE TABLE " + AppInfoEntry.TABLE_NAME + " (" +
                    AppInfoEntry.COLUMN_NAME_PACKAGE + " "+TEXT_TYPE +" PRIMARY KEY," +
                    AppInfoEntry.COLUMN_NAME_APP_LABEL + TEXT_TYPE + COMMA_SEP +
                    AppInfoEntry.COLUMN_NAME_APP_ICON + TEXT_TYPE + COMMA_SEP +
                    AppInfoEntry.COLUMN_NAME_START_INTENT + TEXT_TYPE + COMMA_SEP +
            " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + AppInfoEntry.TABLE_NAME;

    public static abstract class AppInfoEntry implements BaseColumns {
        public static final String TABLE_NAME = "table_app_info";

        public static final String COLUMN_NAME_PACKAGE = "package";
        public static final String COLUMN_NAME_APP_LABEL="app_label";
        public static final String COLUMN_NAME_APP_ICON="app_icon";
        public static final String COLUMN_NAME_START_INTENT="start_intent";
    }
}
