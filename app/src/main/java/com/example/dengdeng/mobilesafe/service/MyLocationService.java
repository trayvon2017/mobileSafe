package com.example.dengdeng.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.telephony.SmsManager;
import android.util.Log;

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
//        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//        option.setCoorType("bd09ll");
//        option.setOpenGps(true);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
        return super.onStartCommand(intent, flags, startId);
    }

    class MyLocationListener extends BDAbstractLocationListener{

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            /*double latitude = bdLocation.getLatitude();
            double altitude = bdLocation.getAltitude();
            Log.d(TAG, "onReceiveLocation: latitude:"+latitude+",altitude:"+altitude);*/
            Address address = bdLocation.getAddress();
            String country = address.country;
            String province = address.province;
            String city = address.city;
            String district = address.district;
            String street = address.street;
            String address_str = "country:"+
                    country+",province:"+province+",city:"+city+",district:"+district+",street:"+street;
            Log.d(TAG, "onReceiveLocation: "+address_str);
            ToastUtils.makeToast(getApplicationContext(),address_str);
//            SmsManager manager = SmsManager.getDefault();
//            manager.sendTextMessage(SpUtils.getString(getApplicationContext(), ConstantValues.PHONE_NUM,""),null,address_str,null,null);
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
