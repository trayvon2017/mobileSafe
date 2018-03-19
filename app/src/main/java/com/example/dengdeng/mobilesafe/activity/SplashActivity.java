package com.example.dengdeng.mobilesafe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dengdeng.mobilesafe.R;
import com.example.dengdeng.mobilesafe.utils.StreamUtils;

import org.json.JSONException;
import org.json.JSONObject;

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

    private TextView tv_versionName;
    private ProgressBar pb_request;
    private Context mContext;
    private int mLocalVersionCode;
    private String mVersionName;

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

                    try {
                        //设置请求的URL
                        URL url = new URL("http://192.168.0.141:8080/version.json");
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
                            String mVersionFeature = jsonObject.getString("versionFeature");
                            String mDownloadUrl = jsonObject.getString("downloadUrl");
                            Log.d(TAG, "initData:mVersionCode "+mVersionCode);
                            Log.d(TAG, "initData:mVersionName "+mVersionName);
                            Log.d(TAG, "initData:mVersionFeature "+mVersionFeature);
                            Log.d(TAG, "initData:mDownloadUrl "+mDownloadUrl);
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
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
