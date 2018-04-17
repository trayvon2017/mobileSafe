package com.example.dengdeng.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by fbfatboy on 2018/4/18.
 */

public class BlacknumOpenHelper extends SQLiteOpenHelper {
    public BlacknumOpenHelper(Context context) {
        super(context, "blacknumDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table blacknum" +
                "(_id integer primary key autoincrement ,phone varchar(20),mode integer);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
