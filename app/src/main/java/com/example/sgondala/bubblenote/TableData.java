package com.example.sgondala.bubblenote;

import android.provider.BaseColumns;

/**
 * Created by sgondala on 17/7/15.
 */
public class TableData {

    public TableData(){};

    public static abstract class TableInfo implements BaseColumns{
        public static final String messageData = "MessageData";
        public static final String databaseName = "appDB";
        public static final String tableName = "myTable";
    }
}
