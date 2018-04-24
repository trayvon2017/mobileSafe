package com.example.dengdeng.mobilesafe.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dengdeng.mobilesafe.R;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AdvancedToolsActivity extends AppCompatActivity {

    private TextView tv_guishudi;
    private TextView tv_sms_backup;
    private final static String TAG = "backupsms";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_tools);
        initUI();
    }

    private void initUI() {
        tv_guishudi = (TextView) findViewById(R.id.tv_guishudi);
        tv_sms_backup = (TextView) findViewById(R.id.tv_sms_backup);
        tv_guishudi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转进入查询归属地的页面
                Intent intent = new Intent(AdvancedToolsActivity.this,LookForGuishudiActivity.class);
                startActivity(intent);
            }
        });
        tv_sms_backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                backupSms();

                backupSms2();
            }
        });
    }

    private void backupSms2() {
        //备份短信，备份位置，然后 address,date,type,body
        final String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"smss.xml";
        //备份的数据写入xml中，同时需要进度条，进度条结束之后提示备份成
        final ProgressDialog dialog = new ProgressDialog(AdvancedToolsActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.show();
        //把具体的备份逻辑放到单独的方法中区
        backup(path,new CallBack(){
            @Override
            public void setMax(int max) {
                dialog.setMax(max);
            }

            @Override
            public void setProgress(int progress) {
                dialog.setProgress(progress);
            }

            @Override
            public void dismiss() {
                dialog.dismiss();
            }
        });

    }

    private void backup( final String path, final CallBack callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long startTime = System.currentTimeMillis();
                Cursor cursor = getContentResolver().query(Uri.parse("content://sms/"), new String[]{"address", "date", "type", "body"},
                        null, null, null);
                callBack.setMax(cursor.getCount());
                int num = 0;//当前备份短信的条数
                XmlSerializer serializer = Xml.newSerializer();
                try {
                    serializer.setOutput(new FileOutputStream(path), "utf-8");
                    serializer.startDocument("utf-8", true);
                    serializer.startTag(null, "smss");


                    while (cursor.moveToNext()) {
                        //存储短信到一个xml文件 smss 》sms》address,date,type,body

                        String address = cursor.getString(cursor.getColumnIndex("address"));
                        int date = cursor.getInt(cursor.getColumnIndex("date"));
                        int type = cursor.getInt(cursor.getColumnIndex("type"));
                        String body = cursor.getString(cursor.getColumnIndex("body"));
                        serializer.startTag(null,"sms");

                        serializer.startTag(null,"address");
                        serializer.text(address);
                        serializer.endTag(null,"address");
                        serializer.startTag(null,"date");
                        serializer.text(date+"");
                        serializer.endTag(null,"date");
                        serializer.startTag(null,"type");
                        serializer.text(type+"");
                        serializer.endTag(null,"type");
                        serializer.startTag(null,"body");
                        serializer.text(body);
                        serializer.endTag(null,"body");

                        serializer.endTag(null,"sms");
                        Log.d(TAG, "backupSms: " + cursor.getString(cursor.getColumnIndex("address")));
                        num++;
                        callBack.setProgress(num);
                    }
                    serializer.endTag(null, "smss");
                    serializer.endDocument();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                long endTime = System.currentTimeMillis();
                if((endTime-startTime)<2000){
                    try {
                        Thread.sleep(2000-(endTime-startTime));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                callBack.dismiss();
            }

        }).start();

    }

    interface CallBack{
        void setMax(int max);
        void setProgress(int progress);

        void dismiss();
    }


    private void backupSms() {
        //备份短信，备份位置，然后 address,date,type,body

        final String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"smss.xml";
        //备份的数据写入xml中，同时需要进度条，进度条结束之后提示备份成
        final ProgressDialog dialog = new ProgressDialog(AdvancedToolsActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


        dialog.show();
       new Thread(new Runnable() {
           @Override
           public void run() {
               Cursor cursor = getContentResolver().query(Uri.parse("content://sms/"), new String[]{"address", "date", "type", "body"},
                       null, null, null);
               dialog.setMax(cursor.getCount());
               int num = 0;//当前备份短信的条数
               XmlSerializer serializer = Xml.newSerializer();
               try {
                   serializer.setOutput(new FileOutputStream(path), "utf-8");
                   serializer.startDocument("utf-8", true);
                   serializer.startTag(null, "smss");


                   while (cursor.moveToNext()) {
                       //存储短信到一个xml文件 smss 》sms》address,date,type,body

                       String address = cursor.getString(cursor.getColumnIndex("address"));
                       int date = cursor.getInt(cursor.getColumnIndex("date"));
                       int type = cursor.getInt(cursor.getColumnIndex("type"));
                       String body = cursor.getString(cursor.getColumnIndex("body"));
                       serializer.startTag(null,"sms");

                       serializer.startTag(null,"address");
                       serializer.text(address);
                       serializer.endTag(null,"address");
                       serializer.startTag(null,"date");
                       serializer.text(date+"");
                       serializer.endTag(null,"date");
                       serializer.startTag(null,"type");
                       serializer.text(type+"");
                       serializer.endTag(null,"type");
                       serializer.startTag(null,"body");
                       serializer.text(body);
                       serializer.endTag(null,"body");

                       serializer.endTag(null,"sms");
                       Log.d(TAG, "backupSms: " + cursor.getString(cursor.getColumnIndex("address")));
                       num++;
                       dialog.setProgress(num);
                   }
                   serializer.endTag(null, "smss");
                   serializer.endDocument();
                   dialog.dismiss();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
       }).start();





    }
}
