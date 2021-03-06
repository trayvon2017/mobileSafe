package com.example.dengdeng.mobilesafe.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import com.example.dengdeng.mobilesafe.R;
import com.example.dengdeng.mobilesafe.db.domain.AppInfo;
import com.example.dengdeng.mobilesafe.engine.AppInfoEngine;
import com.example.dengdeng.mobilesafe.utils.MemUtils;

import java.util.ArrayList;
import java.util.List;

public class AppManageActivity extends AppCompatActivity implements View.OnClickListener{

    private List<AppInfo> userApps = new ArrayList<AppInfo>();
    private List<AppInfo> sysApps = new ArrayList<AppInfo>();
    private TextView mTv_phone_mem;
    private TextView mTv_sdcard_mem;
    private ListView mLv_apps;
    private Context mContext;
    private MyAppAdapter mAppAdapter;
    private TextView mTv_fixed_title;
    private TextView tv_popup_uninstall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manage);
        mContext = AppManageActivity.this;
        initUI();
        initMemTitle();
        initListItems();
    }

    private void initListItems() {
        userApps = AppInfoEngine.getAppInfos(getApplicationContext(), AppInfoEngine.USER_APPS);
        sysApps = AppInfoEngine.getAppInfos(getApplicationContext(), AppInfoEngine.SYS_APPS);
        mAppAdapter = new MyAppAdapter();
        mLv_apps.setAdapter(mAppAdapter);
        mLv_apps.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem >= userApps.size()+1){
                    mTv_fixed_title.setText("系统应用");
                }else {
                    mTv_fixed_title.setText("用户应用");
                }
            }
        });
        mLv_apps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0 && position != userApps.size()+1){
                    AppInfo appInfo = new AppInfo();
                    if (position<userApps.size()+1){
                        appInfo = userApps.get(position-1);
                    }else {
                        appInfo = sysApps.get(position-userApps.size()-2);
                    }
                    popupWindow(view,appInfo);
                }
            }
        });
    }

    private void popupWindow(View view, final AppInfo appinfo) {
        final View popupView = View.inflate(getApplicationContext(),
                R.layout.popup_window_layout, null);
        TextView tv_popup_uninstall = (TextView) popupView.findViewById(R.id.tv_popup_uninstall);
        TextView tv_popup_open = (TextView) popupView.findViewById(R.id.tv_popup_open);
        TextView tv_popup_share = (TextView) popupView.findViewById(R.id.tv_popup_share);

        final PopupWindow popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true);
        tv_popup_uninstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //卸载
                removeApp(appinfo);
                popupWindow.dismiss();
            }
        });
        tv_popup_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开应用
                startApp(appinfo);
                popupWindow.dismiss();
            }
        });
        tv_popup_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //分享应用
                shareApp(appinfo);
                popupWindow.dismiss();
            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable());

        popupWindow.showAsDropDown(view,view.getWidth()/2,-view.getHeight());
    }

    private void shareApp(AppInfo appinfo) {
        PackageManager packageManager = mContext.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_SEND);

            intent.putExtra(Intent.EXTRA_TEXT,"分享一个应用给你，"+
                    appinfo.appName);
            intent.setType("text/plain");
            startActivity(intent);

    }

    private void startApp(AppInfo appinfo) {
        PackageManager packageManager = mContext.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(appinfo.packageName);
        if(intent != null){
            startActivity(intent);
        }

    }

    private void removeApp(AppInfo appinfo) {
        Uri uri = Uri.fromParts("package", appinfo.packageName, null);
        //也可以写成Uri uri = Uri.parse("package:"+packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        /*switch (v.getId()){
            case R.id.tv_popup_uninstall:

                break;
            case R.id.tv_popup_open:

                break;
            case R.id.tv_popup_share:

                break;
            default:
                break;
        }*/
    }

    class MyAppAdapter extends BaseAdapter{
        private static final int TEXT_VIEW_TYPE = 0;
        private static final int ITEM_TYPE = 1;

        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount()+1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position==0 || position ==userApps.size()+1){
                return TEXT_VIEW_TYPE;
            }else {
                return ITEM_TYPE;
            }
        }

        @Override
        public int getCount() {
            return userApps.size()+sysApps.size()+2;
        }

        @Override
        public AppInfo getItem(int position) {
            //
            if (position==0 || position ==userApps.size()+1){
                return null;
            }else if (position>0&&position<userApps.size()+1){
                return userApps.get(position-1);
            }else{
                return sysApps.get(position-userApps.size()-2);
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            /*if (getItemViewType(position) == TEXT_VIEW_TYPE){
                View view = View.inflate(getApplicationContext(), R.layout.item_list_apps_title, null);
                TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                if (position == 0){

                    tv_title.setText("用户应用");
                }
                if (position == userApps.size()+1){

                    tv_title.setText("系统应用");
                }
                return view;
            }else {
                return View.inflate(getApplicationContext(), R.layout.item_list_apps_title, null);
            }*/
            switch (getItemViewType(position)){
                case TEXT_VIEW_TYPE:
                    View view = View.inflate(getApplicationContext(), R.layout.item_list_apps_title, null);
                    TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                    if (position == 0){

                        tv_title.setText("用户应用");
                    }
                    if (position == userApps.size()+1){

                        tv_title.setText("系统应用");
                    }
                    return view;
                case ITEM_TYPE:
                    ViewHolder holder = null;
                    AppInfo appInfo = null;
                    if (convertView == null){
                        holder = new ViewHolder();
                        convertView = View.inflate(getApplicationContext(),R.layout.item_list_apps,null);
                        holder.iv_applist_appicon = (ImageView)convertView.findViewById(R.id.iv_applist_appicon);
                        holder.tv_applist_appname = (TextView)convertView.findViewById(R.id.tv_applist_appname);
                        holder.tv_applist_issys = (TextView)convertView.findViewById(R.id.tv_applist_issys);
                        convertView.setTag(holder);
                    }else {
                        holder = (ViewHolder) convertView.getTag();
                    }
                    /*if (position>0&&position<userApps.size()+1){
                         appInfo = userApps.get(position-1);
                    }else{
                        appInfo = sysApps.get(position-userApps.size()-2);
                    }*/
                    appInfo = getItem(position);

                    holder.iv_applist_appicon.setBackgroundDrawable(appInfo.icon);
                    holder.tv_applist_appname.setText(appInfo.appName);
                    if (appInfo.isSystem){
                        holder.tv_applist_issys.setText("系统应用");
                    }else {
                        holder.tv_applist_issys.setText("用户应用");
                    }
                    return convertView;
            }
            return null;
        }
    }
    class ViewHolder{
        ImageView iv_applist_appicon;
        TextView tv_applist_appname;
        TextView tv_applist_issys;
    }

    private void initUI() {

        mTv_phone_mem = (TextView) findViewById(R.id.tv_phone_mem);
        mTv_sdcard_mem = (TextView) findViewById(R.id.tv_sdcard_mem);
        mTv_fixed_title = (TextView) findViewById(R.id.tv_fixed_title);
        mLv_apps = (ListView) findViewById(R.id.lv_apps);

    }

    /**
     * 初始化顶部的可用内存栏
     */
    private void initMemTitle() {
        String totalSdMem = MemUtils.getInstance(mContext).getTotaLSdMem();
        String avaliSdMem = MemUtils.getInstance(mContext).getAvaliSdMem();
        String totalPhoneMem = MemUtils.getInstance(mContext).getTotaLPhoneMem();
        String avaliPhoneMem = MemUtils.getInstance(mContext).getAvaliPhoneMem();
        mTv_phone_mem.setText("内部："+avaliPhoneMem+"/"+totalPhoneMem);
        mTv_sdcard_mem.setText("sd卡："+avaliSdMem+"/"+totalSdMem);
    }


}
