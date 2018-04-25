package com.example.dengdeng.mobilesafe.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

/**
 * 获取手机内部存储以及sd卡存储大小
 * Created by fbfatboy on 2018/4/25.
 */

public class MemUtils {
    private static MemUtils memUtils;
    private Context context;
    public MemUtils(Context context) {
        this.context = context;
    }
    public static MemUtils getInstance(Context context){
        if(memUtils==null){
            memUtils = new MemUtils(context);
        }
        return memUtils;
    }

    public  String getTotaLSdMem() {
        String path = Environment.getExternalStorageDirectory().getPath();

        return getTotalMemByPath(path);
    }

    public  String getAvaliSdMem() {

        String path = Environment.getExternalStorageDirectory().getPath();


        return getAvaliMemByPath(path);
    }


    public  String getTotaLPhoneMem() {

        String path = Environment.getDataDirectory().getPath();

        return getTotalMemByPath(path);

    }

    public  String getAvaliPhoneMem() {

        String path = Environment.getDataDirectory().getPath();
        return getAvaliMemByPath(path);
    }
    private  String getTotalMemByPath(String path){
        StatFs statFs = new StatFs(path);
        long blockSize = statFs.getBlockSize();
        long blockCount = statFs.getBlockCount();
        String s = Formatter.formatFileSize(context, blockSize * blockCount);
        return s;
    }

    private  String getAvaliMemByPath(String path){
        StatFs statFs = new StatFs(path);
        long availableBlocks = statFs.getAvailableBlocks();
        long blockSize = statFs.getBlockSize();
        String s = Formatter.formatFileSize(context, blockSize * availableBlocks);
        return s;

    }
}
