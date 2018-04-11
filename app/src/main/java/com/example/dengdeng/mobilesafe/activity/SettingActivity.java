package com.example.dengdeng.mobilesafe.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.dengdeng.mobilesafe.R;
import com.example.dengdeng.mobilesafe.service.PhoneAddressService;
import com.example.dengdeng.mobilesafe.utils.ConstantValues;
import com.example.dengdeng.mobilesafe.utils.ServiceUtils;
import com.example.dengdeng.mobilesafe.utils.SpUtils;
import com.example.dengdeng.mobilesafe.view.SettingClickView;
import com.example.dengdeng.mobilesafe.view.SettingItemView;

public class SettingActivity extends AppCompatActivity {

    private SettingItemView mSetting_item_update;
    private TextView mTv_title;
    private TextView mtv_des;
    private CheckBox mCb_update;
    private SettingItemView mSetting_item_phoneAddress;
    private SettingClickView mScvAddressStyle;
    private SettingClickView mScv_address_location;
    private String[] mStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initUpdata_UI();
        initPhoneLocationUI();
        initAddressStyleUI();
        initAddressLocationUI();
    }

    /**
     * 初始化设置里面的归属地提示框位置
     *
     */
    private void initAddressLocationUI() {
        mScv_address_location = (SettingClickView) findViewById(R.id.scv_address_location);
        mScv_address_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到一个新的activity
                startActivity(new Intent(getApplicationContext(),AddressLocationActivity.class));
            }
        });

    }

    /**
     * 初始化 归属地风格的设置选项
     */
    private void initAddressStyleUI() {
        mStyle = new String[]{
                "透明", "橙色", "蓝色", "灰色", "绿色"
        };
        mScvAddressStyle = (SettingClickView) findViewById(R.id.scv_address_style);
        int index = SpUtils.getInt(getApplicationContext(), ConstantValues.ADDRESS_STYLE_NUM, 0);
        mScvAddressStyle.setTv_des(mStyle[index]);
        mScvAddressStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToastStyle();
            }
        });

    }

    /**
     * 弹出选择对话框，选择风格
     */
    private void showToastStyle() {
        int addressStyleNum = SpUtils.getInt(getApplicationContext(), ConstantValues.ADDRESS_STYLE_NUM, 0);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setSingleChoiceItems(mStyle, addressStyleNum, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //选中之后保存在sp中
                SpUtils.putInt(getApplicationContext(),ConstantValues.ADDRESS_STYLE_NUM,which);
                mScvAddressStyle.setTv_des(mStyle[which]);
                dialog.dismiss();
            }
        });
        builder.setTitle("请选择归属地样式");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     *初始化电话归属地显示设置
     */
    private void initPhoneLocationUI() {
        mSetting_item_phoneAddress = (SettingItemView) findViewById(R.id.setting_item_phoneAddress);

        // 根据服务是否开启来判断此处设置是否开启
        boolean isRuning = ServiceUtils.isRuning(this, "com.example.dengdeng.mobilesafe.service.PhoneAddressService");
        mSetting_item_phoneAddress.setCheck(isRuning);

        //1,注册点击事件，
        mSetting_item_phoneAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = mSetting_item_phoneAddress.isChecked();
                mSetting_item_phoneAddress.setCheck(!checked);
                //判断，开启的时候开启服务，关闭的时候关闭服务
                if (!checked){
                        startService(new Intent(getApplicationContext(), PhoneAddressService.class));
                }else{
                        stopService(new Intent(getApplicationContext(), PhoneAddressService.class));
                }
            }
        });
    }

    private void initUpdata_UI() {
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
