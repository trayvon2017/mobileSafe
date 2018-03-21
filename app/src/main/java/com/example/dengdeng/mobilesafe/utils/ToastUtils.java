package com.example.dengdeng.mobilesafe.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by XYSM on 2018/3/21.
 */

public class ToastUtils {

    public static void makeToast(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
