package com.example.dengdeng.mobilesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.dengdeng.mobilesafe.db.BlacknumOpenHelper;
import com.example.dengdeng.mobilesafe.db.domain.Blacknum;

import java.util.ArrayList;

/**
 * Created by fbfatboy on 2018/4/18.
 */

public class BlacknumDao {

    private  BlacknumOpenHelper openHelper;

    public BlacknumDao(Context context) {
        openHelper = new BlacknumOpenHelper(context);
    }
    private static BlacknumDao blacknumDao ;

    public static BlacknumDao getInstance(Context context){
        if (blacknumDao == null){
             blacknumDao = new BlacknumDao(context);
        }
        return blacknumDao;
    }

    public Blacknum findNumByPhone(String phone){
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.query("blacknum", new String[]{"mode"}, "phone = ?", new String[]{phone}, null, null, null);
        if (cursor.moveToNext()){
            int mode = cursor.getInt(cursor.getColumnIndex("mode"));
            return new Blacknum(phone,mode);
        }
        return null;
    }
    /**
     * 添加黑名单号码
     * @param blacknum 需要添加号码
     */
    public void insert(Blacknum blacknum){
        SQLiteDatabase db = openHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("phone",blacknum.getPhone());
        values.put("mode",blacknum.getMode());
        db.insert("blacknum",null,values);
        db.close();
    }
    public void del(String phone){
        SQLiteDatabase db = openHelper.getReadableDatabase();
        db.delete("blacknum","phone = ?",new String[]{phone});
        db.close();
    }
    public ArrayList<Blacknum> findAll(){
        ArrayList<Blacknum> nums = new ArrayList<>();
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.query("blacknum", null, null, null, null, null, null);
        while (cursor.moveToNext()){
            String phone = cursor.getString(cursor.getColumnIndex("phone"));
            int mode = cursor.getInt(cursor.getColumnIndex("mode"));
            Blacknum blacknum = new Blacknum(phone, mode);
            nums.add(blacknum);
        }
        return nums;
    }
    public ArrayList<Blacknum> findApart(int fromIndex ,int length){
        ArrayList<Blacknum> nums = new ArrayList<>();
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from blacknum order by _id desc limit ?,?", new String[]{fromIndex+"",length+""});
        while (cursor.moveToNext()){
            String phone = cursor.getString(cursor.getColumnIndex("phone"));
            int mode = cursor.getInt(cursor.getColumnIndex("mode"));
            Blacknum blacknum = new Blacknum(phone, mode);
            nums.add(blacknum);
        }
        return nums;
    }
}
