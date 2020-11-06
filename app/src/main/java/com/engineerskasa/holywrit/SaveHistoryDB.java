package com.engineerskasa.holywrit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SaveHistoryDB {

    private SQLiteOpenHelper mSQLiteOpenHelper;
    private SQLiteDatabase mSQLiteDatabase;
    private static SaveHistoryDB instance;


    private SaveHistoryDB(Context context){
        this.mSQLiteOpenHelper = new HolyWritHistoryDBHelper(context);
    }

    public static SaveHistoryDB getInstance(Context context){
        if(instance == null){
            instance = new SaveHistoryDB(context);
        }
        return instance;
    }

    public void open(){
        this.mSQLiteDatabase = mSQLiteOpenHelper.getWritableDatabase();
    }

    public long saveHistory(String ref, String mode, String time){
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(HolyWritHistoryContract.HistoryEntry.COLUMN_NAME_REF, ref);
        values.put(HolyWritHistoryContract.HistoryEntry.COLUMN_NAME_MODE, mode);
        values.put(HolyWritHistoryContract.HistoryEntry.COLUMN_NAME_TIME, time);
        long newRowId = 0;
        if(fetchHistory().size() == 1000){
            Cursor cursor = mSQLiteDatabase.rawQuery("SELECT * from history", null);
            if(cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    if(cursor.getInt(0) < fetchHistory().get(fetchHistory().size() - 1).getId()){
                        deleteID(String.valueOf(cursor.getInt(0)));
                        newRowId= mSQLiteDatabase.insert(HolyWritHistoryContract.HistoryEntry.TABLE_NAME, null, values);
                        break;
                    }
                    cursor.moveToNext();
                }
            }

        }else{
            newRowId= mSQLiteDatabase.insert(HolyWritHistoryContract.HistoryEntry.TABLE_NAME, null, values);
        }
        // Insert the new row, returning the primary key value of the new row
        return newRowId;
    }

    public boolean deleteTitle(String name)
    {
        return mSQLiteDatabase.delete(HolyWritHistoryContract.HistoryEntry.TABLE_NAME, HolyWritHistoryContract.HistoryEntry.COLUMN_NAME_REF + "=?", new String[]{name}) > 0;
    }
    public boolean deleteID(String id)
    {
        return mSQLiteDatabase.delete(HolyWritHistoryContract.HistoryEntry.TABLE_NAME, HolyWritHistoryContract.HistoryEntry._ID + "=?", new String[]{id}) > 0;
    }

    public ArrayList<History> fetchHistory(){

        ArrayList<History> returnList = new ArrayList<>();
        Cursor cursor = mSQLiteDatabase.rawQuery("SELECT * from history", null);
        if(cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {
                History history = new History();
                history.setId(cursor.getInt(0));
                history.setRef(cursor.getString(1));
                history.setMode(cursor.getString(2));
                history.setTimestamp(cursor.getString(3));
                returnList.add(history);
                cursor.moveToNext();
            }
        }
        cursor.close();

        return returnList;
    }

    public ArrayList<History> searchHistory(String s){
        ArrayList<History> returnList = new ArrayList<>();
       if(fetchHistory().size() != 0){
           for(History hs: fetchHistory()){
               if(hs.getRef().contains(s))
                   returnList.add(hs);
           }
       }
        return returnList;
    }

}
