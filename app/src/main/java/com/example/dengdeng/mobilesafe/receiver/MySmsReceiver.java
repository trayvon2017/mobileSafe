package com.example.dengdeng.mobilesafe.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;

import com.example.dengdeng.mobilesafe.R;
import com.example.dengdeng.mobilesafe.utils.ToastUtils;

public class MySmsReceiver extends BroadcastReceiver {

    private DevicePolicyManager mDpm;
    private ComponentName mDeviceAdminSample;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        ToastUtils.makeToast(context,"shoudaoduanxin");
        Object[] pdus = (Object[]) intent.getExtras().get("pdus");
        for (Object pdu:
             pdus) {
            SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu);
            String originatingAddress = sms.getOriginatingAddress();
            String messageBody = sms.getMessageBody();
            if (messageBody.contains("#*location*#")){
                //发送本机的gps定位给安全手机
                //1 获取到本机的经纬度坐标

                //2 发送短信给安全手机


            }else if (messageBody.contains("#*alarm*#")){
                //本机播放音乐
                //1.准备播放器
                //2.播放指定音乐
                MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ylzs);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();


            }else if (messageBody.contains("#*wipadata*#")){
                //擦除数据，这个需要一个设备管理器，待定
                ToastUtils.makeToast(context,"清除数据");


            }else if (messageBody.contains("#*lockscreen*#")){
                mDeviceAdminSample = new ComponentName(context, MyDeviceAdminReceiver.class);
                mDpm =  ((DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE));
               if (mDpm.isAdminActive(mDeviceAdminSample)){
                   mDpm.lockNow();
                   mDpm.resetPassword("123",0);
               }


            }
        }
    }

}
