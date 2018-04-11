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

public class SettingClickView extends RelativeLayout {

    private TextView tv_des;
    private TextView tv_title;

    public SettingClickView(Context context) {
        this(context,null);
    }

    public SettingClickView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SettingClickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = View.inflate(context, R.layout.setting_click_view, this);
        //这里传入null会怎么样
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_des = (TextView) view.findViewById(R.id.tv_des);
        String destitle = attrs.getAttributeValue(ConstantValues.NAME_SPACE, "destitle");
        String des = attrs.getAttributeValue(ConstantValues.NAME_SPACE, "des");

        tv_title.setText(destitle);
        tv_des.setText(des);

    }
    public void setTv_des(String des){
        tv_des.setText(des);
    }


}

