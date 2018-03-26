package com.example.dengdeng.mobilesafe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dengdeng.mobilesafe.R;

public class HomeActivity extends AppCompatActivity {

    private GridView mGv_home;
    private String[] names;
    private String[] mIconNames;
    private int[] mIconIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initData();
    }

    private void initData() {
        mGv_home = (GridView) findViewById(R.id.gv_home);
        mIconNames = new String[]{
                 "手机防盗","通信卫士","软件管理","进程管理","流量统计","手机杀毒" ,
                         "缓存清理","高级工具","设置中心"
         };
        mIconIds = new int[]{R.drawable.home_safe,R.drawable.home_callmsgsafe,R.drawable.home_apps,
                R.drawable.home_taskmanager,R.drawable.home_netmanager,R.drawable.home_trojan,
                R.drawable.home_sysoptimize,R.drawable.home_tools,R.drawable.home_settings};

        MyBaseAdapter adapter = new MyBaseAdapter();
        mGv_home.setAdapter(adapter);
        mGv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:

                        break;

                    case 8:
                        //进入设置中心
                        Intent intent = new Intent(HomeActivity.this,SettingActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }


    private class MyBaseAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return mIconNames.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(), R.layout.items_icons, null);
            ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            TextView tv_iconname = (TextView) view.findViewById(R.id.tv_iconname);
            iv_icon.setBackgroundResource(mIconIds[position]);
            tv_iconname.setText(mIconNames[position]);

            return view;
        }
    }
}
