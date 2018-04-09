package com.example.dengdeng.mobilesafe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.dengdeng.mobilesafe.R;

public class AdvancedToolsActivity extends AppCompatActivity {

    private TextView tv_guishudi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_tools);
        initUI();
    }

    private void initUI() {
        tv_guishudi = (TextView) findViewById(R.id.tv_guishudi);
        tv_guishudi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转进入查询归属地的页面
                Intent intent = new Intent(AdvancedToolsActivity.this,LookForGuishudiActivity.class);
                startActivity(intent);
            }
        });
    }
}
