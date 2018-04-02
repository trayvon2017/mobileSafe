package com.example.dengdeng.mobilesafe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;

import com.example.dengdeng.mobilesafe.R;
import com.example.dengdeng.mobilesafe.utils.ConstantValues;
import com.example.dengdeng.mobilesafe.utils.SpUtils;
import com.example.dengdeng.mobilesafe.utils.ToastUtils;

public class Setup4Activity extends AppCompatActivity {

    private CheckBox cb_complete;
    private long x1=0;
    private long x2=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        initUI();
    }

    private void initUI() {
        final CheckBox cb_complete = (CheckBox) findViewById(R.id.cb_complete);
        //回显
        boolean isComplete = SpUtils.getBoolean(getApplicationContext(), ConstantValues.SETTING_COMPLETE, false);
        cb_complete.setChecked(isComplete);

        cb_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Setup4Activity", "onClick: 被点击了");
                boolean checked = cb_complete.isChecked();
                cb_complete.setChecked(checked);
                if (checked){
                    //完成，保存状态
                    SpUtils.putBoolean(getApplicationContext(),ConstantValues.SETTING_COMPLETE,checked);
                    cb_complete.setText("手机防盗已开启");
                }else {
                    //清除已保存的状态
                    SpUtils.remove(getApplicationContext(),ConstantValues.SETTING_COMPLETE);
                    cb_complete.setText("手机防盗已关闭");
                }
            }
        });

    }

    public void setup4_next(View view) {
        //判断是否已经完成设置
        boolean isComplete = SpUtils.getBoolean(getApplicationContext(), ConstantValues.SETTING_COMPLETE, false);
        if (isComplete){
            Intent intent = new Intent(this, PhoneSafeActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.next_in,R.anim.next_out);
        }else{
            ToastUtils.makeToast(getApplicationContext(),"请先完成设置");
        }

    }

    public void setup4_previous(View view) {
        Intent intent = new Intent(this, Setup3Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.previous_in,R.anim.previous_out);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN :
                x1 = (long) event.getX();
                break;
            case MotionEvent.ACTION_UP :
                x2 = (long) event.getX();


                if(x1-x2>20){
                    //左滑和nextButton一样的逻辑
                    Intent intent = new Intent(this, PhoneSafeActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.next_in,R.anim.next_out);

                }else if(x1-x2<20){
                    //右滑和previousButton一样的逻辑
                    Intent intent = new Intent(this, Setup3Activity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.previous_in,R.anim.previous_out);
                }
                break;
           /* case :MotionEvent.ACTION_DOWN
                    x1 = event.getX();
            break;

			case:MotionEvent.ACTION_UP
                    x2 = event.getX();

            break;*/
        }


        return super.onTouchEvent(event);
    }

}
