package com.example.dengdeng.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dengdeng.mobilesafe.R;
import com.example.dengdeng.mobilesafe.utils.ConstantValues;

/**
 * Created by Dengdeng on 2018/3/27.
 */

public class SettingItemView extends RelativeLayout {

    private CheckBox cb_update;
    private TextView tv_des;
    private TextView tv_title;
    private String deson;
    private String desoff;

    public SettingItemView(Context context) {
        this(context,null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = View.inflate(context, R.layout.setting_item_view, this);
        //这里传入null会怎么样
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_des = (TextView) view.findViewById(R.id.tv_des);
        cb_update = (CheckBox) view.findViewById(R.id.cb_update);
        String destitle = attrs.getAttributeValue(ConstantValues.NAME_SPACE, "destitle");
        deson = attrs.getAttributeValue(ConstantValues.NAME_SPACE,"deson");
        desoff = attrs.getAttributeValue(ConstantValues.NAME_SPACE,"desoff");
        tv_title.setText(destitle);
        if (isChecked()){
            tv_des.setText(deson);
        }else{
            tv_des.setText(desoff);
        }
    }
    public boolean isChecked(){
        return cb_update.isChecked();
    }
    public void setCheck(boolean ischeck){
        if (ischeck){
            tv_des.setText(deson);
        }else{
            tv_des.setText(desoff);
        }
        cb_update.setChecked(ischeck);
    }
}

