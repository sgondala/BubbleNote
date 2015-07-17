package com.example.sgondala.bubblenote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by sgondala on 17/7/15.
 */
public class DatabaseOperations extends SQLiteOpenHelper{
    public static final int databaseVersion = 1;
    public String CREATE_QUERY = "CREATE TABLE myTable(MessageData TEXT);";
//    Log.d("Database operations", "Database created");

    public DatabaseOperations(Context context) {
        super(context, TableData.TableInfo.databaseName, null, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sdb) {
        sdb.execSQL(CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void putInformation(DatabaseOperations dop, String messageString){
        SQLiteDatabase SQ = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TableData.TableInfo.messageData, messageString);
        long k = SQ.insert(TableData.TableInfo.tableName, null, cv);
        Log.d("Database operations", "One row inserted");
    }

    public Cursor getInformation(DatabaseOperations dop){
        SQLiteDatabase SQ = dop.getReadableDatabase();
        String[] columns = {TableData.TableInfo.messageData};
        Cursor CR = SQ.query(TableData.TableInfo.tableName, columns, null,
                null, null, null, null);
        return CR;
    }

    public boolean deleteMessage(DatabaseOperations dop, String messageSelected){
        SQLiteDatabase SQ = dop.getWritableDatabase();
        return SQ.delete(TableData.TableInfo.tableName, TableData.TableInfo.messageData + "='" + messageSelected + "'", null) > 0;
    }

}
