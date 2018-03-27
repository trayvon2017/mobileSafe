package com.example.dengdeng.mobilesafe.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dengdeng.mobilesafe.R;
import com.example.dengdeng.mobilesafe.utils.ConstantValues;
import com.example.dengdeng.mobilesafe.utils.SpUtils;
import com.example.dengdeng.mobilesafe.utils.ToastUtils;

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
                        //选中手机防盗模块之后的业务逻辑
                        //首先判断是否已经存储了密码，
                        String psd_str = SpUtils.getString(getApplicationContext(), ConstantValues.MOBILE_SAFE_PSD,"");
                        if(TextUtils.isEmpty(psd_str)){
                            //未存储的话就弹出新建密码的对话框
                            createPsd();
                        }else{
                            //存储了的话就直接弹出输入密码的对话框，
                            login();
                        }

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

    private void login() {
//这里只能使用this，不能使用getApplicationContext()
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        final AlertDialog dialog = builder.create();
        dialog.setView(View.inflate(getApplicationContext(),R.layout.login_dialog,null));
        dialog.show();
        //给Button设置点击事件

        Button btn_login = (Button)dialog.findViewById(R.id.btn_login);
        final Button btn_exit = (Button)dialog.findViewById(R.id.btn_exit);
        //点击了登录
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_login_psd = (EditText)dialog.findViewById(R.id.et_login_psd);
                String login_psd_str = et_login_psd.getText().toString().trim();
                if(TextUtils.isEmpty(login_psd_str)){
                    ToastUtils.makeToast(getApplicationContext(),"输入不能为空请重新输入");
                }else{
                    String result = SpUtils.getString(getApplicationContext(),ConstantValues.MOBILE_SAFE_PSD,"");
                    //先判断密码是否相等
                    if(result.equals(login_psd_str)){
                        //进入手机防盗页面
                        dialog.dismiss();
                        Intent intent = new Intent(HomeActivity.this,PhoneSafeActivity.class);
                        startActivity(intent);
                    }else{
                        ToastUtils.makeToast(getApplicationContext(),"密码错误，请重新输入");
                    }
                }
            }
        });
        //点击了取消
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    //弹出创建密码的对话框

    private void createPsd() {
        //这里只能使用this，不能使用getApplicationContext()
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        final AlertDialog dialog = builder.create();
        dialog.setView(View.inflate(getApplicationContext(),R.layout.create_psd_dialog,null));
        dialog.show();
        //给Button设置点击事件

        Button btn_create_psd = (Button)dialog.findViewById(R.id.btn_create_psd);
        Button btn_cancel = (Button)dialog.findViewById(R.id.btn_cancel);
        //点击了创建密码
        btn_create_psd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText et_new_psd = (EditText)dialog.findViewById(R.id.et_new_psd);
                String new_psd_str = et_new_psd.getText().toString().trim();
                EditText et_confirm_psd = (EditText)dialog.findViewById(R.id.et_confirm_psd);
                String confirm_psd_str = et_confirm_psd.getText().toString().trim();
                if(!TextUtils.isEmpty(new_psd_str)&&!TextUtils.isEmpty(confirm_psd_str)){
                    //先判断密码是否相等
                    if(new_psd_str.equals(confirm_psd_str)){
                        SpUtils.putString(getApplicationContext(),ConstantValues.MOBILE_SAFE_PSD,new_psd_str);
                        //进入手机防盗页面
                        dialog.dismiss();
                        Intent intent = new Intent(HomeActivity.this,PhoneSafeActivity.class);
                        startActivity(intent);

                    }else{
                        ToastUtils.makeToast(getApplicationContext(),"两次输入的不相符，请重新输入");
                    }

                }else{
                    ToastUtils.makeToast(getApplicationContext(),"输入不能为空请重新输入");
                }
            }
        });
        //点击了取消
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
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
