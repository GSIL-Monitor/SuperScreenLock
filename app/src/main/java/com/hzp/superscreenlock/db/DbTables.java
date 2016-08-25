package com.hzp.superscreenlock.db;

import android.provider.BaseColumns;

/**
 * Created by hezhipeng on 2016/8/25.
 */
public abstract class DbTables {

    private static final String COMMA_SEP = ",";
    private static final String TEXT_TYPE = " TEXT ";
    private static final String LONG_TYPE = " LONG ";

    public abstract class AppInfo {
        public static final String TABLE_NAME = "table_app_info";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        Entry.COLUMN_NAME_PACKAGE + " " + TEXT_TYPE + " PRIMARY KEY," +
                        Entry.COLUMN_NAME_APP_LABEL + TEXT_TYPE + COMMA_SEP +
                        " )";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

        public abstract class Entry implements BaseColumns {

            public static final String COLUMN_NAME_PACKAGE = "package";
            public static final String COLUMN_NAME_APP_LABEL = "app_label";
        }
    }

    public abstract class EnvironmentInfo {

        public static final String TABLE_NAME = "table_environment_info";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        Entry.COLUMN_NAME_TITLE + " " + TEXT_TYPE + " PRIMARY KEY," +
                        Entry.COLUMN_NAME_HINT + TEXT_TYPE + COMMA_SEP +
                        Entry.COLUMN_NAME_TYPE + TEXT_TYPE + COMMA_SEP +
                        Entry.COLUMN_NAME_SSID + TEXT_TYPE + COMMA_SEP +
                        Entry.COLUMN_NAME_LONGITUDE + LONG_TYPE + COMMA_SEP +
                        Entry.COLUMN_NAME_LATITUDE + LONG_TYPE + COMMA_SEP +
                        Entry.COLUMN_NAME_LOCK_TYPE + LONG_TYPE + COMMA_SEP +
                        " )";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

        public abstract class Entry implements BaseColumns {

            public static final String COLUMN_NAME_TITLE = "title";
            public static final String COLUMN_NAME_HINT = "hint";
            public static final String COLUMN_NAME_TYPE = "type";
            public static final String COLUMN_NAME_SSID = "SSID";
            public static final String COLUMN_NAME_LONGITUDE = "longitude";
            public static final String COLUMN_NAME_LATITUDE = "latitude";
            public static final String COLUMN_NAME_LOCK_TYPE = "lock_type";
        }
    }
}
