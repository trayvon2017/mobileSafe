package com.example.dengdeng.mobilesafe.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dengdeng.mobilesafe.R;
import com.example.dengdeng.mobilesafe.utils.StreamUtils;
import com.example.dengdeng.mobilesafe.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.HttpManager;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.ContentValues.TAG;

/**
 * The type Splash activity.
 */
public class SplashActivity extends Activity {

    private static final int HIGH_VERSION = 100;
    private static final int NO_HIGH_VERSION = 101;
    private static final  int JSON_ERROR =102;
    private static final  String DOWNLOAD_URL = "http://10.0.2.2:8080/***.apk";
    private static final String VERSIONINFOURL = "http://10.0.2.2:8080/version.json";
    private TextView tv_versionName;
    private ProgressBar pb_request;
    private Context mContext;
    private int mLocalVersionCode;
    private String mVersionName;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HIGH_VERSION:
                    //弹出对话框
                    alertUpdateDialog();
                    break;
                case NO_HIGH_VERSION:
                    //直接进入主页面
                    openHomePage();
                    break;
                case JSON_ERROR:
                    Toast.makeText(SplashActivity.this,"json error",Toast.LENGTH_SHORT).show();
                    openHomePage();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 弹出升级新版本的对话框
     */
    private void alertUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("发现新版本，请及时更新");
        builder.setMessage(mVersionFeature);
        builder.setPositiveButton("下载安装", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    //下载安装的逻辑，下载使用xutils
                downloadAndInstall();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //清楚对话框，进入主页面
                dialog.dismiss();
                openHomePage();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                //清楚对话框，进入主页面
                dialog.dismiss();
                openHomePage();
            }
        });

        builder.show();
    }

    /**
     * 下载安装，更新版本
     */
    private void downloadAndInstall() {

        final ProgressDialog pd = new ProgressDialog(mContext);
        RequestParams params = new RequestParams(DOWNLOAD_URL);
        params.setSaveFilePath(Environment.getExternalStorageDirectory().getPath()+"mobileSafe.apk");
        x.http().get(params, new Callback.ProgressCallback<File>(){
            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pd.setMessage("下载中");
            }
            @Override
            public void onLoading(long total, long current, boolean isDownloading) {

                pd.show();
                pd.setMax((int)total );
                pd.setProgress((int)current);
            }

            @Override
            public void onSuccess(File result) {
                ToastUtils.makeToast(mContext,"下载完成");
                pd.dismiss();
                // TODO 调用系统应用安装apk文件 PackageInstallerActivity
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtils.makeToast(mContext,"检查网络或者sd卡状态");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private String mVersionFeature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        //找到需要操作的控件
        myFindView();
        initData();

    }

    /**
     * 初始化数据
     * 1:获取到本地的版本信息
     * 2:获取到服务端的版本信息
     */
    private void initData() {
        PackageManager pm = mContext.getPackageManager();
        try {
            //通过packageInfo获取到本地的版本信息，版本信息其实存储在
            //AndroidManiFest.xml文件中
            PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), 0);
            mLocalVersionCode = pi.versionCode;
            mVersionName = pi.versionName;
            tv_versionName.setText("版本名称：" + pi.versionName);
            //网络请求获取到服务端的版本信息
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message msg = Message.obtain();

                    try {
                        //设置请求的URL 办公室192.168.1.106  家里192.168.0.141
                        URL url = new URL(VERSIONINFOURL);
                        //获取连接
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        //设置连接超时时间
                        connection.setConnectTimeout(1000 * 2);
                        //设置读取超时时间
                        connection.setReadTimeout(4 * 1000);
                        //设置请求方式
                        connection.setRequestMethod("GET");
                        if (connection.getResponseCode() == 200) {
                            InputStream is = connection.getInputStream();
                            String json_String = StreamUtils.stream2String(is);
                            Log.d(TAG, "run: json"+json_String);
                            JSONObject jsonObject = new JSONObject(json_String);
                            String mVersionCode = jsonObject.getString("versionCode");
                            String mVersionName = jsonObject.getString("versionName");
                            mVersionFeature = jsonObject.getString("versionFeature");
                            String mDownloadUrl = jsonObject.getString("downloadUrl");
                            Log.d(TAG, "initData:mVersionCode "+mVersionCode);
                            Log.d(TAG, "initData:mVersionName "+mVersionName);
                            Log.d(TAG, "initData:mVersionFeature "+ mVersionFeature);
                            Log.d(TAG, "initData:mDownloadUrl "+mDownloadUrl);
                            //判断线上的系统版本和本地的系统版本号小进行对比
                            if (Integer.parseInt(mVersionCode)>mLocalVersionCode){
                                //线上》本地，跳出对话框更新
                                msg.what = HIGH_VERSION;

                            }else{
                                //进入主页面 未发现
                                msg.what = NO_HIGH_VERSION;

                            }
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        msg.what = JSON_ERROR;
                        e.printStackTrace();
                    }finally {
                        mHandler.sendMessage(msg);
                    }
                }
            }).start();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 进入主页面的activity
     */
    private void openHomePage() {

        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 找到需要操作的控件
     */
    private void myFindView() {
        mContext = SplashActivity.this;
        tv_versionName = (TextView) findViewById(R.id.tv_versionName);
        pb_request = (ProgressBar) findViewById(R.id.pb_request);
    }
}
