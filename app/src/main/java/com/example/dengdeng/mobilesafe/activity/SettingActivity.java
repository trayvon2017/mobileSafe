package com.example.dengdeng.mobilesafe.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.dengdeng.mobilesafe.R;
import com.example.dengdeng.mobilesafe.utils.ConstantValues;
import com.example.dengdeng.mobilesafe.utils.SpUtils;
import com.example.dengdeng.mobilesafe.view.SettingItemView;

public class SettingActivity extends AppCompatActivity {

    private SettingItemView mSetting_item_update;
    private TextView mTv_title;
    private TextView mtv_des;
    private CheckBox mCb_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initUI();
    }

    private void initUI() {
        mSetting_item_update = (SettingItemView) findViewById(R.id.setting_item_update);
        boolean aBoolean = SpUtils.getBoolean(getApplicationContext(), ConstantValues.UPDATE_VALUE, true);
        mSetting_item_update.setCheck(aBoolean);
        mSetting_item_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCheck = !mSetting_item_update.isChecked();
                mSetting_item_update.setCheck(isCheck);
                SpUtils.putBoolean(getApplicationContext(),ConstantValues.UPDATE_VALUE,isCheck);
            }
        });
        /*mTv_title = (TextView) mSetting_item_update.findViewById(R.id.tv_title);
        mtv_des = (TextView) mSetting_item_update.findViewById(R.id.tv_des);
        mCb_update = (CheckBox) mSetting_item_update.findViewById(R.id.cb_update);
        mSetting_item_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mCb_update.setChecked(!mCb_update.isChecked());
                    if(mCb_update.isChecked()){
                        mtv_des.setText("自动更新设置已开启");
                    }else{
                        mtv_des.setText("自动更新设置已关闭");
                    }
            }
        });
        mCb_update.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mtv_des.setText("自动更新设置已开启");
                }else{
                    mtv_des.setText("自动更新设置已关闭");
                }
            }
        });*/
    }
}
