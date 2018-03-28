package com.example.dengdeng.mobilesafe.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;

import com.example.dengdeng.mobilesafe.R;
import com.example.dengdeng.mobilesafe.utils.ConstantValues;
import com.example.dengdeng.mobilesafe.utils.SpUtils;
import com.example.dengdeng.mobilesafe.utils.ToastUtils;
import com.example.dengdeng.mobilesafe.view.SettingItemView;

public class Setup2Activity extends AppCompatActivity {

    private SettingItemView siv_bind_sim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        initUI();
    }

    private void initUI() {
        siv_bind_sim = (SettingItemView) findViewById(R.id.siv_bind_sim);
        //回显的问题
        String simSN = SpUtils.getString(getApplicationContext(), ConstantValues.SIM_SN, "");
        if (TextUtils.isEmpty(simSN)){
            //之前没有存储过，这里就不勾选
            siv_bind_sim.setCheck(false);
        }else {
            //存储过的划要选中
            siv_bind_sim.setCheck(true);
        }
        siv_bind_sim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击之后设置当前选中状态的相反状态
                boolean checked = siv_bind_sim.isChecked();
                siv_bind_sim.setCheck(!checked);
                //判断是否保存simSN
                if (!checked){
                    //保存
                    TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    String simSerialNumber = tm.getSimSerialNumber();
                    SpUtils.putString(getApplicationContext(),ConstantValues.SIM_SN,simSerialNumber);

                }else {
                    //清楚保存的smSN
                    SpUtils.remove(getApplicationContext(),ConstantValues.SIM_SN);
                }
            }
        });
    }

    public void setup2_previous(View view) {
        Intent intent = new Intent(this, Setup1Activity.class);
        startActivity(intent);
        finish();
    }

    public void setup2_next(View view) {
        //需要先判断是否bind sim
        String simSN = SpUtils.getString(getApplicationContext(), ConstantValues.SIM_SN, "");
        if (TextUtils.isEmpty(simSN)){
            //之前没有存储过，提示用户
            ToastUtils.makeToast(getApplicationContext(),"请先绑定sim卡");
        }else {
            //进行下一步
            Intent intent = new Intent(this, Setup3Activity.class);
            startActivity(intent);
            finish();
        }
    }
}
