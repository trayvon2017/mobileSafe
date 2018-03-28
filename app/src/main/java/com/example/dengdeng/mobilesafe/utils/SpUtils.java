package com.example.dengdeng.mobilesafe.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Dengdeng on 2018/3/27.
 */

public class SpUtils {

    private static SharedPreferences sp;

    /**
     * 保存布尔类型的数据
     * @param context 上下文环境变量
     * @param key 键
     * @param value 保存的数值
     */
    public static void putBoolean(Context context, String key, boolean value) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key, value).commit();
    }

    /**
     *
     * @param context
     * @param key
     * @param def 设置的默认值
     * @return 没有找到键的时候返回def
     */
    public static boolean getBoolean(Context context, String key, boolean def) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        boolean result = sp.getBoolean(key, def);
        return result;
    }
    public static void putString(Context context,String key,String value){
        if(sp == null){
            sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        sp.edit().putString(key,value).commit();
    }
    public static String getString(Context context,String key,String def){
        if(sp == null){
            sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        return sp.getString(key,def);
    }

    /**
     * 清楚保存的某个属性
     * @param context
     * @param key 要清除的对象
     */
    public static void remove(Context context, String key) {
        if(sp == null){
            sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        sp.edit().remove(key).commit();
    }
}
