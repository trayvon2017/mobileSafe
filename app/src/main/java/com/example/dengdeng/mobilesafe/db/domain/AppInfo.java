package com.example.dengdeng.mobilesafe.db.domain;

import android.graphics.drawable.Drawable;

/**
 * 应用的基本信息名字，包名，图标，安装位置
 * Created by fbfatboy on 2018/4/25.
 *
 */

public class AppInfo {
    public String appName;
    public String packageName;
    public Drawable icon;
    public   boolean isSystem;
    public boolean isInSdcard;
}
