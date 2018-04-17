package com.example.dengdeng.mobilesafe.activity;

import android.content.Context;
import android.graphics.Rect;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.dengdeng.mobilesafe.R;
import com.example.dengdeng.mobilesafe.utils.ConstantValues;
import com.example.dengdeng.mobilesafe.utils.SpUtils;

public class AddressLocationActivity extends AppCompatActivity {

    private static final String TAG = "AddressLocationActivity";
    private long[] mHits = new long[2];
    private WindowManager mWM = null;
    private int width;
    private int height;





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


        //获取屏幕的宽搞

        mWM = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        width = mWM.getDefaultDisplay().getWidth();
        height = mWM.getDefaultDisplay().getHeight();
        Log.d(TAG, "initUI: width="+ width +"height="+ height);

        //获取状态栏高度
        int statusBarHeight1 = -1;
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
        }
        //程序区域的宽高,宽度和屏幕一致，高度需要减去状态栏高度
        height = height - statusBarHeight1;

        int dv_left = SpUtils.getInt(getApplicationContext(), ConstantValues.DRAG_LEFT, 0);
        int dv_top = SpUtils.getInt(getApplicationContext(), ConstantValues.DRAG_TOP, 0);
        //TODO 直接使用layout方法设置控件的位置不生效
      /*  //这么设置不生效
        if (dv_left==0&dv_top==0&dv_right==0&dv_bottom==0){

        }else {
            iv_drag.layout(dv_left,dv_top,dv_right,dv_bottom);
        }*/
        if (iv_drag.getTop()<height/2){
            btn_bottom.setVisibility(View.VISIBLE);
            btn_top.setVisibility(View.INVISIBLE);
        }else {
            btn_bottom.setVisibility(View.VISIBLE);
            btn_top.setVisibility(View.INVISIBLE);
        }
        //iv_drag在相对布局中,所以其所在位置的规则需要由相对布局提供
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = dv_left;
        layoutParams.topMargin = dv_top;
        iv_drag.setLayoutParams(layoutParams);

        //实现双击居中
        iv_drag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.arraycopy(mHits,1,mHits,0,mHits.length-1);
                mHits[mHits.length-1] = SystemClock.uptimeMillis();
                if (mHits[mHits.length-1]-mHits[0]<500){
                    //判断点击的间隔满足条件之后 执行我们的逻辑
                    int left = width/2 - iv_drag.getWidth()/2;
                    int top = height/2 - iv_drag.getHeight()/2;
                    int right = width/2 + iv_drag.getWidth()/2;
                    int bottom = height/2 + iv_drag.getHeight()/2;

                    iv_drag.layout(left,top,right,bottom);

                    SpUtils.putInt(getApplicationContext(),ConstantValues.DRAG_LEFT,left);
                    SpUtils.putInt(getApplicationContext(),ConstantValues.DRAG_TOP,top);
                }
            }
        });

        //2.拖动的，手指离开屏幕的时候保存位置
        iv_drag.setOnTouchListener(new View.OnTouchListener() {
            //手指移动的前一个位置坐标
            float startX;
            float startY;
            //手指移动HOU的位置
            float moveX ;
            float moveY ;
            //移动一次之后的位置坐标
            int endLeft;
            int endTop;
            int endRight;
            int endBottom;
            //拖动空间的位置
            int bottom;
            int top;
            int right;
            int left;
            //移动距离
            int dY;
            int dX;
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
                                //获取四边的初始位置
                                left = iv_drag.getLeft();
                                right = iv_drag.getRight();
                                top = iv_drag.getTop();
                                bottom = iv_drag.getBottom();
                                //计算移动后的位置
                                moveX = event.getRawX();
                                moveY = event.getRawY();
                                //计算手指x和y的位移
                                dX = (int)(moveX - startX);
                                dY = (int)(moveY - startY);
                                //容错处理，控件不能超出屏幕
                                if (left + dX < 0|| right + dX > width|| top + dY < 0|| bottom + dY > height){
                                    return true;
                                }
                                //隐藏其中一个btn
                                if (top + dY < height/2){
                                    btn_bottom.setVisibility(View.VISIBLE);
                                    btn_top.setVisibility(View.INVISIBLE);
                                }else {
                                    btn_top.setVisibility(View.VISIBLE);
                                    btn_bottom.setVisibility(View.INVISIBLE);
                                }
                                //计算移动之后的位置
                                endLeft = left + dX;
                                endTop = top + dY;
                                endRight = right + dX ;
                                endBottom = bottom + dY;
                                iv_drag.layout(endLeft, endTop, endRight, endBottom);
                                //一次移动之后需要更新startX和startY
                                startX = moveX;
                                startY = moveY;
                                break;
                            case MotionEvent.ACTION_UP:
                                //存储位置
                                SpUtils.putInt(getApplicationContext(), ConstantValues.DRAG_LEFT,iv_drag.getLeft());
                                SpUtils.putInt(getApplicationContext(), ConstantValues.DRAG_TOP,iv_drag.getTop());

                                break;
                        }
                        break;
                }
                return false;
            }
        });

    }
}
