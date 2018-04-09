package com.example.dengdeng.mobilesafe.activity;

import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dengdeng.mobilesafe.R;
import com.example.dengdeng.mobilesafe.engine.AddressDao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LookForGuishudiActivity extends AppCompatActivity {

    private EditText et_phoneNum;
    private Button btn_lookforguishudi;
    private TextView tv_guishudi_result;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    //得到查询结果显示到textview
                    tv_guishudi_result.setText("未查询到有效归属地，请重新输入");
                    break;
                case 1:
                    //得到查询结果显示到textview
                    tv_guishudi_result.setText((String)msg.obj);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_for_guishudi);
        initDataBase();
        initUI();
    }

    private void initDataBase() {
        //初始化归属地数据库
        final File file = new File(getFilesDir(),"address.db");
        if (!file.exists()){
            //数据库不存在，需要先从assets下拷贝
            new Thread(new Runnable() {
                @Override
                public void run() {
                    AssetManager assets = getAssets();
                    try {
                        InputStream inputStream = assets.open("address.db");
                        FileOutputStream outputStream = new FileOutputStream(file);
                        byte[] bytes = new byte[1024];
                        int length = -1;
                        while((length=inputStream.read(bytes))!=-1){
                            outputStream.write(bytes,0,length);
                        }
                        outputStream.close();
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private void initUI() {
        et_phoneNum = (EditText) findViewById(R.id.et_phoneNum);
        btn_lookforguishudi = (Button) findViewById(R.id.btn_lookforguishudi);
        tv_guishudi_result = (TextView) findViewById(R.id.tv_guishudi_result);
        btn_lookforguishudi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击btn的时候执行获取到ET中的内容进行查询操作
                String phoneNumStr = et_phoneNum.getText().toString().trim();
                String regex = "^1[3-8]\\\\d{9}";
                Pattern p= Pattern.compile(regex);
                Matcher m=p.matcher(phoneNumStr);
                if (phoneNumStr.length()>=7){
                    //判断属于手机号
                    //进行数据库的查询操作
                    String result = queryPhoneNum(phoneNumStr);
                    Message message = Message.obtain();
                    if (!TextUtils.isEmpty(result)){
                        message.what = 1;
                        message.obj = result;
                        handler.sendMessage(message);
                    }else{
                        handler.sendEmptyMessage(0);
                    }
                }else{
                    Animation shake = AnimationUtils.loadAnimation(
                            getApplicationContext(), R.anim.shake);
                    et_phoneNum.startAnimation(shake);
                    tv_guishudi_result.setText("未查询到有效归属地，请重新输入");
                }
            }
        });
        et_phoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phoneNumStr = et_phoneNum.getText().toString().trim();
                String regex = "^1[3-8]\\\\d{9}";
                Pattern p= Pattern.compile(regex);
                Matcher m=p.matcher(phoneNumStr);
                if (phoneNumStr.length()>=7){
                    //判断属于手机号
                    //进行数据库的查询操作
                    String result = queryPhoneNum(phoneNumStr);
                    Message message = Message.obtain();
                    if (!TextUtils.isEmpty(result)){
                        message.what = 1;
                        message.obj = result;
                        handler.sendMessage(message);
                    }else{
                        handler.sendEmptyMessage(0);
                    }
                }else{
                    tv_guishudi_result.setText("未查询到有效归属地，请重新输入");
                }
            }
        });
    }

    private String queryPhoneNum(String phoneNumStr) {
        AddressDao dao = new AddressDao(getApplicationContext());
        String address = dao.getAddress(phoneNumStr);
        return address;
    }
}
