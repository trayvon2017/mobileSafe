package com.example.dengdeng.mobilesafe.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.example.dengdeng.mobilesafe.db.domain.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取当前手机安装应用的有关信息
 * Created by fbfatboy on 2018/4/25.
 */

public class AppInfoEngine {


    public static final int ALL_APPS = 1;
    public static final int USER_APPS = 2;
    public static final int SYS_APPS = 3;

    /**
     *根据需求获取到应用，所有应用，用户应用，系统应用
     */
    public static List<AppInfo> getAppInfos(Context context,int type){
        //准备储存app列表
        List<AppInfo> apps = new ArrayList<AppInfo>();
        //获取系统package的管理员
        PackageManager packageManager = context.getPackageManager();
        //获取安装在手机上的所有包的信息
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);
        //循环遍历包，拿到我们要的信息并存储
        for (PackageInfo packageInfo :
                installedPackages) {
            AppInfo appInfo = new AppInfo();
            appInfo.packageName = packageInfo.packageName;
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            appInfo.appName = applicationInfo.loadLabel(packageManager).toString();
            appInfo.icon = applicationInfo.loadIcon(packageManager);
            if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)==ApplicationInfo.FLAG_SYSTEM){
                appInfo.isSystem = true;
            }else{
                appInfo.isSystem = false;
            }
            if ((applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == ApplicationInfo.FLAG_EXTERNAL_STORAGE){
                appInfo.isInSdcard = true ;
            }else {
                appInfo.isInSdcard = false;
            }
            //根据我们要的类型来选择合适的存储方式
            switch (type){
                case AppInfoEngine.ALL_APPS:
                    apps.add(appInfo);
                    break;
                
                case AppInfoEngine.USER_APPS:
                    if (!appInfo.isSystem){
                        apps.add(appInfo);
                    }
                    break;
                case AppInfoEngine.SYS_APPS:
                    if (appInfo.isSystem){
                        apps.add(appInfo);
                    }
                    break;
                    
            }
            

        }

        return  apps;
    }
    /**
     * 判断是否是系统应用
     */
    public static boolean isSYS(Context context ,String packageName){
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            if ((applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM){
                return true;
            }else {
                return false;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }
        return false;
    }

}


