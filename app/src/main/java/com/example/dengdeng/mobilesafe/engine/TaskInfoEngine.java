package com.example.dengdeng.mobilesafe.engine;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug;
import android.text.format.Formatter;

import com.example.dengdeng.mobilesafe.db.domain.TaskInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fbfatboy on 2018/5/2.
 */

public  class TaskInfoEngine {

    private static final int ALL =0;
    private static final int USER =1;
    private static final int SYSTEM =2;

    //获取正在运行的总进程数
    public static int getTaskCount(Context context){

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        int size = am.getRunningAppProcesses().size();

        return size;
    }
    //获取系统的ram信息
    public  static String getAvailableRam(Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo);
        long availMem = memoryInfo.availMem;
       return android.text.format.Formatter.formatFileSize(context,availMem);
    }
    //获取系统的ram信息
    public static String getAllRam(Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo);
        long totalMem = memoryInfo.totalMem;
        return android.text.format.Formatter.formatFileSize(context,totalMem);
    }
    //基础方法，根据需求查询指定的进程
    public  static List<TaskInfo> getTasks(Context context , int flag){

        List<TaskInfo> taskInfos = new ArrayList<>();

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        PackageManager pm = context.getPackageManager();
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info :
                runningAppProcesses) {
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.task_name =  info.processName;
            Debug.MemoryInfo[] memoryInfos = am.getProcessMemoryInfo(new int[]{info.pid});
            taskInfo.task_memory = Formatter.formatFileSize(context,memoryInfos[0].getTotalPrivateDirty());

            ApplicationInfo applicationInfo = null;
            try {
                applicationInfo = pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA);

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            if (applicationInfo != null){
//               taskInfo.task_name = applicationInfo.loadLabel(pm).toString();
                taskInfo.task_icon = applicationInfo.loadIcon(pm);
                if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM){
                    taskInfo.isSys = true;
                }else{
                    taskInfo.isSys = false;
                }
                switch (flag){
                    case TaskInfoEngine.ALL:
                        taskInfos.add(taskInfo);

                        break;
                    case TaskInfoEngine.USER:
                            if (!taskInfo.isSys){
                                taskInfos.add(taskInfo);
                            }
                        break;
                    case TaskInfoEngine.SYSTEM:
                        if (taskInfo.isSys){
                            taskInfos.add(taskInfo);
                        }
                        break;
                }

            }

        }

        return taskInfos;
    }

    /**
     * 查询用户进程
     * @param context 上下文
     * @return 返回包含所有正在运行的用户进程的集合
     */
    public  static List<TaskInfo> getUserTasks(Context context) {
        return getTasks(context,TaskInfoEngine.USER);
    }
    /**
     * 查询用户进程
     * @param context 上下文
     * @return 返回包含所有正在运行的进程的集合
     */
    public  static List<TaskInfo> getAllTasks(Context context) {
        return getTasks(context,TaskInfoEngine.ALL);
    }
    /**
     * 查询用户进程
     * @param context 上下文
     * @return 返回包含所有正在运行的系统进程的集合
     */
    public  static List<TaskInfo> getSysTasks(Context context) {
        return getTasks(context,TaskInfoEngine.SYSTEM);
    }
}
