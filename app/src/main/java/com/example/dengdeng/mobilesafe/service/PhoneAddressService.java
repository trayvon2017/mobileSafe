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
    private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
    private WindowManager mWM;
    private View mAddressView;

    public PhoneAddressService() {
    }

    @Override
    public void onCreate() {
        mWM = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
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
                    ToastUtils.makeToast(getApplicationContext(),new AddressDao(getApplicationContext()).getAddress(incomingNumber));
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

        final WindowManager.LayoutParams params = mParams;
        //TODO 来电的时候应用已经存储的位置
        //获取sp存储的位置
        int left = SpUtils.getInt(getApplicationContext(), ConstantValues.DRAG_LEFT, 0);
        int top = SpUtils.getInt(getApplicationContext(), ConstantValues.DRAG_TOP, 0);
        params.x =left;
        params.y =top;

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

        mWM.addView(mAddressView,params);
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
