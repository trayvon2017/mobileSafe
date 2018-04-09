package com.example.dengdeng.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.Address;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.dengdeng.mobilesafe.utils.ConstantValues;
import com.example.dengdeng.mobilesafe.utils.SpUtils;
import com.example.dengdeng.mobilesafe.utils.ToastUtils;

import static android.content.ContentValues.TAG;

public class MyLocationService extends Service {

    private LocationClient mLocationClient;
    private MyLocationListener mLocationListener = new MyLocationListener();
    public MyLocationService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();

    }
    @Override
    public int onStartCommand(Intent intent,  int flags, int startId) {
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(mLocationListener);
        LocationClientOption option = new LocationClientOption();

        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
        return super.onStartCommand(intent, flags, startId);
    }

    class MyLocationListener extends BDAbstractLocationListener{

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            String addr = bdLocation.getAddrStr();    //获取详细地址信息
            ToastUtils.makeToast(getApplicationContext(),addr);
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
