package com.example.dengdeng.mobilesafe.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dengdeng.mobilesafe.R;
import com.example.dengdeng.mobilesafe.utils.ConstantValues;
import com.example.dengdeng.mobilesafe.utils.SpUtils;
import com.example.dengdeng.mobilesafe.utils.ToastUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

public class Setup3Activity extends AppCompatActivity {

    private EditText et_phone_num;
    private ArrayList<HashMap> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        initUI();
    }



    private void initUI() {
        et_phone_num = (EditText) findViewById(R.id.et_phone_num);
        //回显
        String gotPhone = SpUtils.getString(getApplicationContext(), ConstantValues.PHONE_NUM, "");
        if (!TextUtils.isEmpty(gotPhone)){
            et_phone_num.setText(gotPhone);
        }
    }

    public void setup3_next(View view) {
        String phoneNum = et_phone_num.getText().toString().trim();

        if (!TextUtils.isEmpty(phoneNum)){
            SpUtils.putString(getApplicationContext(),ConstantValues.PHONE_NUM,phoneNum);
            Intent intent = new Intent(this, Setup4Activity.class);
            startActivity(intent);
            finish();
        }else {
            ToastUtils.makeToast(getApplicationContext(),"请先绑定手机号");
        }
    }

    public void setup3_previous(View view) {
        Intent intent = new Intent(this, Setup2Activity.class);
        startActivity(intent);
        finish();
    }
    //选中之后读取本地联系人
    public void chooseContacts(View view) {
        //跳转到联系人界面选择联系人
        Intent intent = new Intent(this, ShowContacts.class);
        startActivityForResult(intent, ConstantValues.SHOWCONTACTS_request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ConstantValues.SHOWCONTACTS_request && resultCode == ConstantValues.SHOWCONTACTS_OK){
            String phone = data.getStringExtra("phone").replace(" ","").replace("-","").trim();
            if (!TextUtils.isEmpty(phone)){
                et_phone_num.setText(phone);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
