package com.example.dengdeng.mobilesafe.utils;

import android.app.ActivityManager;
import android.content.Context;

import com.example.dengdeng.mobilesafe.activity.SettingActivity;

import java.util.List;

/**
 *
 * Created by fbfatboy on 2018/4/11.
 */

public class ServiceUtils {
    /**
     * 判断服务是否在运行
     * @param context
     * @param s  判断的服务完整类名
     * @return 在运行即返回true  否则返回false
     */
    public static boolean isRuning(Context context, String s) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services = am.getRunningServices(1000);
        for (ActivityManager.RunningServiceInfo serviceInfo:
            services ) {
            if (serviceInfo.service.getClassName().equals(s)){
                return true;
            }
        }

        return false;
    }
}
