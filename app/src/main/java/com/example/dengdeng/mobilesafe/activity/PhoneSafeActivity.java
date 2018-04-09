package com.example.dengdeng.mobilesafe.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.dengdeng.mobilesafe.R;
import com.example.dengdeng.mobilesafe.service.MyLocationService;
import com.example.dengdeng.mobilesafe.utils.ConstantValues;
import com.example.dengdeng.mobilesafe.utils.SpUtils;
import com.example.dengdeng.mobilesafe.utils.ToastUtils;

import java.util.ArrayList;

public class PhoneSafeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //首先查看之前是否已经完成基本配置
        boolean overSetUp = SpUtils.getBoolean(getApplicationContext(), ConstantValues.SETTING_COMPLETE, false);
        ArrayList<String> permissions = new ArrayList<String>();
        if (overSetUp){
            if (android.os.Build.VERSION.SDK_INT>=23){
                //运行时权限处理

                if (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION)!=
                        PackageManager.PERMISSION_GRANTED){
                    permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
                }if (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_PHONE_STATE)!=
                        PackageManager.PERMISSION_GRANTED){
                    permissions.add(Manifest.permission.READ_PHONE_STATE);
                }if (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                        PackageManager.PERMISSION_GRANTED){
                    permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                if (!permissions.isEmpty()){
                    //说明有权限没有授权，需要申请授权Cpo
                    requestPermissions(permissions.toArray(new String[permissions.size()]),1);
                }
            }
            //完成设置 进入到安全设置的中心页面
            setContentView(R.layout.activity_phone_safe);
            requstLocation();

        }else {
            //未完成，运行设置向导，先进入第一个页面
            Intent intent = new Intent(this, Setup1Activity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 1:
                if (grantResults.length>0){
                    for (int result : grantResults){
                        if (result != PackageManager.PERMISSION_GRANTED){
                            ToastUtils.makeToast(getApplicationContext(),"必须同意所有权限才可以使用");
                            finish();
                            return;
                        }
                    }
                    requstLocation();
                }else{
                    ToastUtils.makeToast(getApplicationContext(),"发生未知错误");
                    finish();
                }
                break;
            default:
        }
    }

    private void requstLocation() {
        Intent intent1 = new Intent(PhoneSafeActivity.this, MyLocationService.class);
        startService(intent1);
    }
}
