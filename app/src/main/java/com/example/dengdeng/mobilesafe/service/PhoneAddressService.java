package com.example.dengdeng.mobilesafe.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.dengdeng.mobilesafe.R;
import com.example.dengdeng.mobilesafe.engine.AddressDao;
import com.example.dengdeng.mobilesafe.utils.ConstantValues;
import com.example.dengdeng.mobilesafe.utils.SpUtils;
import com.example.dengdeng.mobilesafe.utils.ToastUtils;

public class PhoneAddressService extends Service {

    private TelephonyManager mTm;
    private MyPhoneStateListener mPhoneStateListener;
    private WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
    private WindowManager mWM;
    private View mAddressView;
    private int mHeight;
    private int mWidth;
    private WindowManager.LayoutParams params;

    public PhoneAddressService() {
    }

    @Override
    public void onCreate() {

        mWM = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mWidth = mWM.getDefaultDisplay().getWidth();
        mHeight = mWM.getDefaultDisplay().getHeight();

        //获取状态栏高度
        int statusBarHeight1 = -1;
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
        }
        mHeight = mHeight -statusBarHeight1;
        //创建自定义的Toast
        //开启服务的时候需要创建电话状态的监听
        mTm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mPhoneStateListener = new MyPhoneStateListener();
        Log.d("PhoneAddressService", "onCreate: 服务创建了");
        mTm.listen(mPhoneStateListener,PhoneStateListener.LISTEN_CALL_STATE);


    }
    class MyPhoneStateListener extends PhoneStateListener{

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch(state){
                case TelephonyManager.CALL_STATE_IDLE:
                    if (mWM!=null&mAddressView!=null){
                        mWM.removeView(mAddressView);
                    }
                    ToastUtils.makeToast(getApplicationContext(),"空闲状态");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:

                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    showToast(incomingNumber);
                    break;
            }
        }
    }

    /**
     * 展示来电归属地 的toast、
     * @param incomingNumber 来电号码
     */
    private void showToast(String incomingNumber) {
        String address = new AddressDao(getApplicationContext()).getAddress(incomingNumber);

        params = mParams;
        //TODO 来电的时候应用已经存储的位置


//        不可缺少？？？ 这个是设置的什么？
        //指定吐司的所在位置(将吐司指定在左上角)
        params.gravity = Gravity.LEFT+ Gravity.TOP;

        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
//        params.windowAnimations = com.android.internal.R.style.Animation_Toast;
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.setTitle("Toast");
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;



        mAddressView = View.inflate(getApplicationContext(), R.layout.address,null);

        TextView tv_address_show = (TextView) mAddressView.findViewById(R.id.tv_address_show);

        tv_address_show.setText(address);


        //TODO 根据设置中的风格 更换背景图片
        int styleIndex = SpUtils.getInt(getApplicationContext(), ConstantValues.ADDRESS_STYLE_NUM, 0);
        int[] styles = {
                R.drawable.call_locate_white,
                R.drawable.call_locate_orange,
                R.drawable.call_locate_blue,
                R.drawable.call_locate_gray,
                R.drawable.call_locate_green

        };
        tv_address_show.setBackgroundResource(styles[styleIndex]);

        //获取sp存储的位置
        int drag_left = SpUtils.getInt(getApplicationContext(), ConstantValues.DRAG_LEFT, 0);
        int drag_top = SpUtils.getInt(getApplicationContext(), ConstantValues.DRAG_TOP, 0);
        params.x =drag_left;
        params.y =drag_top;

        mWM.addView(mAddressView, params);

        //设置来电归属地可以拖动
        mAddressView.setOnTouchListener(new View.OnTouchListener() {

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


                        switch (event.getAction()){
                            case MotionEvent.ACTION_DOWN:

                                //获取手指触摸屏幕时候的位置
                                startX = event.getRawX();
                                startY = event.getRawY();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                //获取四边的初始位置
                                left = params.x;
                                right = mAddressView.getRight();
                                top = params.y;
                                bottom = mAddressView.getBottom();
                                //计算移动后的位置
                                moveX = event.getRawX();
                                moveY = event.getRawY();
                                //计算手指x和y的位移
                                dX = (int)(moveX - startX);
                                dY = (int)(moveY - startY);
                                /*//容错处理，控件不能超出屏幕
                                if (left + dX < 0|| right + dX > mWidth|| top + dY < 0|| bottom + dY > mHeight){
                                    return true;
                                }*/

                                //计算移动之后的位置
                                endLeft = left + dX;
                                endTop = top + dY;
                                endRight = right + dX ;
                                endBottom = bottom + dY;
//                                mAddressView.layout(endLeft, endTop, endRight, endBottom);
                                params.x = endLeft;
                                params.y = endTop;
                                mWM.updateViewLayout(mAddressView,params);
                                //一次移动之后需要更新startX和startY
                                startX = moveX;
                                startY = moveY;
                                break;
                            case MotionEvent.ACTION_UP:
                                //存储位置
                                SpUtils.putInt(getApplicationContext(), ConstantValues.DRAG_LEFT,mAddressView.getLeft());
                                SpUtils.putInt(getApplicationContext(), ConstantValues.DRAG_TOP,mAddressView.getTop());

                                break;
                        }

                return true;
            }
        });



    }

    @Override
    public void onDestroy() {
        //非空判断
        if (mTm!= null&&mPhoneStateListener!= null){
            mTm.listen(mPhoneStateListener,PhoneStateListener.LISTEN_NONE);
        }


    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
