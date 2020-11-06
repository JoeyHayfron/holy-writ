package com.engineerskasa.holywrit;

import android.provider.BaseColumns;

public class HolyWritHistoryContract {

    public HolyWritHistoryContract() {
    }

    public static class HistoryEntry implements BaseColumns{
        public static final String TABLE_NAME = "history";
        public static final String COLUMN_NAME_REF = "ref";
        public static final String COLUMN_NAME_MODE = "mode";
        public static final String COLUMN_NAME_TIME = "time";
    }

    static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + HistoryEntry.TABLE_NAME + " (" +
                    HistoryEntry._ID + " INTEGER PRIMARY KEY," +
                    HistoryEntry.COLUMN_NAME_REF + " TEXT," +
                    HistoryEntry.COLUMN_NAME_MODE + " TEXT," +
                    HistoryEntry.COLUMN_NAME_TIME + " TEXT)";

    static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + HistoryEntry.TABLE_NAME;
}
