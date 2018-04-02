package com.example.dengdeng.mobilesafe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.example.dengdeng.mobilesafe.R;

public class Setup1Activity extends AppCompatActivity {
    private long x1=0;
    private long x2=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
    }

    public void setup1_next(View view) {
        Intent intent = new Intent(this, Setup2Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.next_in,R.anim.next_out);

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
                    Intent intent = new Intent(this, Setup2Activity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.next_in,R.anim.next_out);

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
