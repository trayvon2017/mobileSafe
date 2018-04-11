package com.example.dengdeng.mobilesafe.activity;

import android.content.Context;
import android.graphics.Rect;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.dengdeng.mobilesafe.R;
import com.example.dengdeng.mobilesafe.utils.ConstantValues;
import com.example.dengdeng.mobilesafe.utils.SpUtils;

public class AddressLocationActivity extends AppCompatActivity {

    private float startX;
    private float startY;
    private WindowManager mWM;
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_location);
        initUI();
    }

    /***
     *
     */
    private void initUI() {
        final Button btn_top = (Button) findViewById(R.id.btn_top);
        final Button btn_bottom = (Button) findViewById(R.id.btn_bottom);
        final ImageView iv_drag = (ImageView) findViewById(R.id.iv_drag);


        mWM = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        //获取app显示区域
        final Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int dv_left = SpUtils.getInt(getApplicationContext(), ConstantValues.DRAG_LEFT, 0);
        int dv_top = SpUtils.getInt(getApplicationContext(), ConstantValues.DRAG_TOP, 0);
        final int dv_right = SpUtils.getInt(getApplicationContext(), ConstantValues.DRAG_RIGHT, 0);
        final int dv_bottom = SpUtils.getInt(getApplicationContext(), ConstantValues.DRAG_BOTTOM, 0);

        final long[] array = new long[2];
        //双击剧中
        iv_drag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.arraycopy(array,1,array,0,2);
                array[array.length-1] = SystemClock.uptimeMillis();
                if((array[array.length-1]-array[0])<500){
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.leftMargin = (dv_right-iv_drag.getWidth())/2;
                    layoutParams.topMargin = (dv_bottom-iv_drag.getHeight())/2;
                    iv_drag.setLayoutParams(layoutParams);
                }
            }

        });

        //这么设置不生效
        /*if (dv_left==0&dv_top==0&dv_right==0&dv_bottom==0){

        }else {
            iv_drag.layout(dv_left,dv_top,dv_right,dv_bottom);
        }*/
        //iv_drag在相对布局中,所以其所在位置的规则需要由相对布局提供

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = dv_left;
        layoutParams.topMargin = dv_top;
        iv_drag.setLayoutParams(layoutParams);


        //获取屏幕
        //1.获取存储的位置，为存储的话默认居中



        //2.拖动的，手指离开屏幕的时候保存位置
        iv_drag.setOnTouchListener(new View.OnTouchListener() {

            private int bottom;
            private int top;
            private int right;
            private int left;
            private int dY;
            private int dX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (v.getId()){
                    case R.id.iv_drag:
                        switch (event.getAction()){
                            case MotionEvent.ACTION_DOWN:
                                //获取手指触摸屏幕时候的位置
                                startX = event.getRawX();
                                startY = event.getRawY();
                                
                                break;
                            case MotionEvent.ACTION_MOVE:
                                //移动到的位置
                                float moveX = event.getRawX();
                                float moveY = event.getRawY();
                                //计算手指x和y的位移
                                dX = (int)(moveX - startX);
                                dY = (int)(moveY - startY);
                                //获取四边的初始位置
                                left = iv_drag.getLeft();
                                right = iv_drag.getRight();
                                top = iv_drag.getTop();
                                bottom = iv_drag.getBottom();
                                //容错处理，控件不能超出屏幕
                                if (left + dX <rect.left|| right + dX >rect.right|| top + dY <rect.top|| bottom + dY >rect.bottom){
                                    return true;
                                }
                                //隐藏其中一个btn
                                if (top + dY <rect.bottom/2){
                                    btn_bottom.setVisibility(View.INVISIBLE);
                                    btn_top.setVisibility(View.VISIBLE);
                                }else {
                                    btn_top.setVisibility(View.INVISIBLE);
                                    btn_bottom.setVisibility(View.VISIBLE);
                                }
                                //设置之后移动之后的位置
                                iv_drag.layout(left + dX, top + dY, right + dX, bottom + dY);
                                //一次移动之后需要更新startX和startY
                                startX = moveX;
                                startY = moveY;

                                break;
                            case MotionEvent.ACTION_UP:
                                //存储位置
                                SpUtils.putInt(getApplicationContext(), ConstantValues.DRAG_LEFT,left + dX);
                                SpUtils.putInt(getApplicationContext(), ConstantValues.DRAG_TOP,top + dY);
                                SpUtils.putInt(getApplicationContext(), ConstantValues.DRAG_RIGHT,right + dX);
                                SpUtils.putInt(getApplicationContext(), ConstantValues.DRAG_BOTTOM,bottom + dY);

                                break;
                        }
                        break;
                }
                return true;
            }
        });

    }
}
