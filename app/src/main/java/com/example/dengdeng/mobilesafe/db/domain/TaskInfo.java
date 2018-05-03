package com.example.dengdeng.mobilesafe.db.domain;

import android.graphics.drawable.Drawable;

/**
 * Created by fbfatboy on 2018/5/2.
 */

public class TaskInfo {
    //图标
    public Drawable task_icon;
    //名称
    public String task_name;
    //占用的内存
    public String task_memory;
    //包名
    public String packageName;
    //是否被选中
    public boolean isChecked;
    //
    public boolean isSys;
}
