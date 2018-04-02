package com.example.dengdeng.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.example.dengdeng.mobilesafe.utils.ConstantValues;
import com.example.dengdeng.mobilesafe.utils.SpUtils;
import com.example.dengdeng.mobilesafe.utils.ToastUtils;

import org.w3c.dom.Text;

public class MyBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        ToastUtils.makeToast(context.getApplicationContext(),"kaijile");
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String simSerialNumber = tm.getSimSerialNumber()+"xxx";
        String result = SpUtils.getString(context, ConstantValues.SIM_SN, "");
        if (!TextUtils.isEmpty(result)){
            if (!result.equals(simSerialNumber)){
                SmsManager sm = SmsManager.getDefault();
                sm.sendTextMessage(SpUtils.getString(context.getApplicationContext(),ConstantValues.PHONE_NUM,""),null,
                        "手机卡更换了",null,null);
            }
        }
    }
}
