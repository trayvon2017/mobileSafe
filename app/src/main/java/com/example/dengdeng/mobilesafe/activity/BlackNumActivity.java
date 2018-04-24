package com.example.dengdeng.mobilesafe.activity;

import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.dengdeng.mobilesafe.R;
import com.example.dengdeng.mobilesafe.db.BlacknumOpenHelper;
import com.example.dengdeng.mobilesafe.db.dao.BlacknumDao;
import com.example.dengdeng.mobilesafe.db.domain.Blacknum;
import com.example.dengdeng.mobilesafe.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Random;

public class BlackNumActivity extends AppCompatActivity {

    private Button btn_add_blacknum;
    private Context mContext;
    private ListView lv_nums;
    private BlacknumAdapter mAdapter;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mAdapter.notifyDataSetChanged();
        }
    };
    private ArrayList<Blacknum> mNums;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_num);
        mContext = BlackNumActivity.this;
        initData();
        initUI();
    }

    private void initData() {
        //TODO 创建本地数据库 以存储黑名单号码
        BlacknumOpenHelper openHelper = new BlacknumOpenHelper(this);
        SQLiteDatabase database = openHelper.getReadableDatabase();
        database.close();
    }

    public void addSomeTestData(View view) {

        for (int i = 0;i<100;i++){
            String phone = "151375807";
            if (i<10){
                phone = phone + 0 +i;
            }else {
                phone = phone + i;
            }
            Random random = new Random();
            int mode = random.nextInt(3);
            Blacknum blacknum = new Blacknum(phone, mode + 1);
            BlacknumDao.getInstance(getApplicationContext()).insert(blacknum);
            mNums.add(blacknum);
        }
        if (mAdapter == null){
            mNums = BlacknumDao.getInstance(getApplicationContext()).findAll();
            mAdapter = new BlacknumAdapter(mNums);
        }
        mAdapter.notifyDataSetChanged();
    }

    class BlacknumAdapter extends BaseAdapter{
        ArrayList<Blacknum> nums = new ArrayList<Blacknum>();
        public BlacknumAdapter(ArrayList<Blacknum> nums) {
            this.nums = nums;
        }

        @Override
        public int getCount() {
            return nums.size();
        }

        @Override
        public Object getItem(int position) {
            return nums.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder ;
            if (convertView == null){
                convertView = View.inflate(getApplicationContext(), R.layout.item_blacknum, null);
                viewHolder = new ViewHolder();
                viewHolder.tv_black_num = (TextView) convertView.findViewById(R.id.tv_black_num);
                viewHolder.tv_black_num_type = (TextView) convertView.findViewById(R.id.tv_black_num_type);
                viewHolder.iv_del_black_num = (ImageView) convertView.findViewById(R.id.iv_del_black_num);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final Blacknum blacknum = nums.get(position);
            viewHolder.tv_black_num.setText(blacknum.getPhone());
            switch (blacknum.getMode()){
                case 1:
                    viewHolder.tv_black_num_type.setText("短信");
                    break;
                case 2:
                    viewHolder.tv_black_num_type.setText("电话");
                    break;
                case 3:
                    viewHolder.tv_black_num_type.setText("所有");
                    break;
            }
            viewHolder.iv_del_black_num.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //删除操作 ，是分成两个部分进行的，数据库操作和界面操作
                    BlacknumDao.getInstance(getApplicationContext()).del(blacknum.getPhone());
                    //TODO 删除之后更新页面显示，不推荐重新加载数据库，删除数据库的同时修改一下list即可
                    nums.remove(blacknum);
                    mHandler.sendEmptyMessage(0);
                }
            });
            return convertView;
        }
        class ViewHolder{
            TextView tv_black_num;
            TextView tv_black_num_type;
            ImageView iv_del_black_num;
        }
    }
    private void initUI() {

        lv_nums = (ListView) findViewById(R.id.lv_nums);
        //首次加载只加载20，并且是倒序加载
        mNums = BlacknumDao.getInstance(getApplicationContext()).findApart(0,20);
        mAdapter = new BlacknumAdapter(mNums);
        lv_nums.setAdapter(mAdapter);
        lv_nums.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //说明已经刷到最后一屏的listview
                if (firstVisibleItem + visibleItemCount == totalItemCount){
                    //获取最后一个listitem的view，
                    View lastVisalbeItem = view.getChildAt(visibleItemCount - 1);
                    //查询到的数据添加到list中 ，刷新adapter
                    mNums.addAll(BlacknumDao.getInstance(getApplicationContext()).findApart(mNums.size(),20));
                    mAdapter.notifyDataSetChanged();
                    //判断最后一个itemview的底部和当前屏幕listview的底部是否重合重合之后加载数据

                    if (lastVisalbeItem.getBottom()==view.getBottom()){

                    }
                }


            }
        });
        btn_add_blacknum = (Button)findViewById(R.id.btn_add_blacknum);
        btn_add_blacknum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                final AlertDialog dialog = builder.create();
                View view = View.inflate(mContext, R.layout.add_black_num, null);
                final EditText et_black_num = (EditText) view.findViewById(R.id.et_black_num);
                final RadioGroup rg_intercept_type = (RadioGroup) view.findViewById(R.id.rg_intercept_type);
                /*RadioButton rb_sms = (RadioButton) view.findViewById(R.id.rb_sms);
                RadioButton rb_phone = (RadioButton) view.findViewById(R.id.rb_phone);
                RadioButton rb_all = (RadioButton) view.findViewById(R.id.rb_all);*/
                Button btn_confirm_add_black_num = (Button) view.findViewById(R.id.btn_confirm_add_black_num);
                Button btn_cancel_add_black_num = (Button) view.findViewById(R.id.btn_cancel_add_black_num);
                btn_confirm_add_black_num.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Blacknum blacknum = new Blacknum();
                        String phone = et_black_num.getText().toString().trim();
                        if (!TextUtils.isEmpty(phone)){
                            blacknum.setPhone(phone);
                            int checkedRadioButtonId = rg_intercept_type.getCheckedRadioButtonId();
                            switch (checkedRadioButtonId){
                                case R.id.rb_sms:
                                    blacknum.setMode(1);
                                    break;
                                case R.id.rb_phone:
                                    blacknum.setMode(2);
                                    break;
                                case R.id.rb_all:
                                    blacknum.setMode(3);
                                    break;
                            }
                            //TODO 添加数据到本地数据库
                            BlacknumDao.getInstance(mContext).insert(blacknum);
                            dialog.dismiss();
                            //不要每次增加一条都去更新数据库,添加的数据要显示在最前面
                            mNums.add(0,blacknum);
                            mAdapter.notifyDataSetChanged();
                        }else {
                            ToastUtils.makeToast(mContext,"电话号码不能为空");
                        }
                    }
                });
                btn_cancel_add_black_num.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.setView(view);
                dialog.show();
            }
        });
    }
}
