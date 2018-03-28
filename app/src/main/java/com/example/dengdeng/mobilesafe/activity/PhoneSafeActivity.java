package com.example.dengdeng.mobilesafe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.dengdeng.mobilesafe.R;
import com.example.dengdeng.mobilesafe.utils.ConstantValues;
import com.example.dengdeng.mobilesafe.utils.SpUtils;

public class PhoneSafeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //首先查看之前是否已经完成基本配置
        boolean overSetUp = SpUtils.getBoolean(getApplicationContext(), ConstantValues.SETTING_COMPLETE, false);
        if (overSetUp){
            //完成设置 进入到安全设置的中心页面
            setContentView(R.layout.activity_phone_safe);
        }else {
            //未完成，运行设置向导，先进入第一个页面
            Intent intent = new Intent(this, Setup1Activity.class);
            startActivity(intent);
        }

    }
}
