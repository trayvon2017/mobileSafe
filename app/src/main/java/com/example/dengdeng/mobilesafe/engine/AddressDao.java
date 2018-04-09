package com.example.dengdeng.mobilesafe.engine;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by fbfatboy on 2018/4/10.
 */

public class AddressDao {

    Context context;
    public AddressDao(Context context){
        this.context = context;
    }
    public String  getAddress(String phone_num){
        String substring = phone_num.substring(0, 7);
        String path = context.getFilesDir()+"/address.db";
        SQLiteDatabase database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor1 = database.query("data1", new String[]{"outkey"}, "id = ?", new String[]{substring}, null, null, null);
        while(cursor1.moveToNext()){
            String outkey = cursor1.getString(cursor1.getColumnIndex("outkey"));
            Cursor cursor2 = database.query("data2", new String[]{"location"}, "id = ?", new String[]{outkey}, null, null, null);
            while (cursor2.moveToNext()){
                String location = cursor2.getString(cursor2.getColumnIndex("location"));
                return location;
            }
        }
        return null;
    }
}
