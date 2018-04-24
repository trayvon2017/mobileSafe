package com.example.dengdeng.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.example.dengdeng.mobilesafe.db.dao.BlacknumDao;
import com.example.dengdeng.mobilesafe.db.domain.Blacknum;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BlacknumService extends Service {

    private Sms_BlacknumBroadcastReceiver receiver;
    private TelephonyManager mTM;
    private MyPhoneStateListener myPhoneStateListener;

    public BlacknumService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        //开启拦截服务执行的逻辑

        //拦截短信，动态注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        receiver = new Sms_BlacknumBroadcastReceiver();
        registerReceiver(receiver,filter);
        //电话拦截
        mTM = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        myPhoneStateListener = new MyPhoneStateListener();
        mTM.listen(myPhoneStateListener,PhoneStateListener.LISTEN_CALL_STATE);

    }

    /**
     * 借助aidl实现挂断电话的操作
     */
    class MyPhoneStateListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            switch (state){
                case TelephonyManager.CALL_STATE_OFFHOOK:

                    break;
                case TelephonyManager.CALL_STATE_IDLE:

                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    //挂断电话
                    endCall(incomingNumber);
                    //删除通话记录
                    deleteCallLog(incomingNumber);
                    break;
            }

        }
    }

    /**
     * 删除来电记录，当次的
     * @param incomingNumber
     */
    private void deleteCallLog( String incomingNumber) {
        /*//TODO 删除来电记录
        //1.获取内容解决器
        final ContentResolver resolver = getContentResolver();
        //
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                resolver.delete(Uri.parse("content://call_log/calls"),"number = ?",new String[]{incomingNumber});
            }
        }).start();*/
        //方法2 使用观察者模式
        ContentResolver resolver = getContentResolver();
        resolver.registerContentObserver(Uri.parse("content://call_log/calls"),true,new MyContentObserver(new Handler(),incomingNumber));


    }
    class MyContentObserver extends ContentObserver{
        String incomingNumber;

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public MyContentObserver(Handler handler,String incomingNumber) {

            super(handler);
            this.incomingNumber = incomingNumber;

        }

        @Override
        public void onChange(boolean selfChange) {
            //数据库插入一条数据之后 进行删除
            getContentResolver().delete(Uri.parse("content://call_log/calls"),"number = ?",new String[]{incomingNumber});

            super.onChange(selfChange);
        }
    }

    private void endCall(String incomingNumber) {
        Blacknum blacknum = BlacknumDao.getInstance(getApplicationContext()).findNumByPhone(incomingNumber);
        if (blacknum!=null&&blacknum.getMode()!=1){
            //执行拦截电话的逻辑
//                        ITelephony.Stub.asInterface()
//                        ITelephony.Stub.asInterface(ServiceManager.getService(Context.TELEPHONY_SERVICE));
            try {
                Class<?> clazz = Class.forName("android.os.ServiceManager");

                Method method = clazz.getMethod("getService", String.class);


                IBinder binder = (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);

                ITelephony iTelephony = ITelephony.Stub.asInterface(binder);

                iTelephony.endCall();


            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }catch (NoSuchMethodException e) {
                e.printStackTrace();
            }catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }catch (RemoteException e) {
                e.printStackTrace();
            }

        }
    }

    class Sms_BlacknumBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] pduses = (Object[]) intent.getExtras().get("pdus");
            for (Object pdu :
                    pduses) {
                SmsMessage message = SmsMessage.createFromPdu((byte[]) pdu);
                String originatingAddress = message.getOriginatingAddress();
                Blacknum blacknum = BlacknumDao.getInstance(getApplicationContext()).findNumByPhone(originatingAddress);
                if (blacknum!=null&&blacknum.getMode()!=2){
                    //短信拦截的一系列操作
                    receiver.abortBroadcast();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        //关闭拦截服务之星的逻辑
        if (receiver!=null){
            unregisterReceiver(receiver);
        }
    }
}
