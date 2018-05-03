package com.example.dengdeng.mobilesafe.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dengdeng.mobilesafe.R;
import com.example.dengdeng.mobilesafe.db.domain.TaskInfo;
import com.example.dengdeng.mobilesafe.engine.TaskInfoEngine;

import org.w3c.dom.Text;

import java.util.List;

public class ProcessActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView mTv_process_size;
    private TextView mTv_ram;
    private ListView mLv_process;
    private Button mBtn_select_all;
    private Button mBtn_select_reverse;
    private Button mBtn_clean;
    private Button mBtn_setting;
    private List<TaskInfo> mUserTasks,mSysTasks;
    private ProcessAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);
        initUI();
        initTitle();
        initListItems();
        initButton();
    }

    private void initButton() {
        mBtn_select_all.setOnClickListener(this);
        mBtn_select_reverse.setOnClickListener(this);
        mBtn_clean.setOnClickListener(this);
        mBtn_setting.setOnClickListener(this);
    }


    private void initListItems() {
        //首次进入默认顯示所有
        mUserTasks = TaskInfoEngine.getUserTasks(getApplicationContext());
        mSysTasks = TaskInfoEngine.getSysTasks(getApplicationContext());
        mAdapter = new ProcessAdapter();
        mLv_process.setAdapter(mAdapter);
        mLv_process.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0 && position != mUserTasks.size()+1 ){
                    //checkbox取反
                    CheckBox cb_clean = (CheckBox) view.findViewById(R.id.cb_clean);
                    cb_clean.setChecked(!cb_clean.isChecked());
                    TaskInfo taskInfo = mAdapter.getItem(position);
                    taskInfo.isChecked = cb_clean.isChecked();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_select_all:
                selectAll();
                break;
            case R.id.btn_select_reverse:
                selectReverse();
                break;
            case R.id.btn_clean:

                break;
            case R.id.btn_setting:

                break;

        }
    }

    private void selectReverse() {
        for (TaskInfo info :
                mUserTasks) {
            info.isChecked = !info.isChecked;
        }
        for (TaskInfo info :
                mSysTasks) {
            info.isChecked = !info.isChecked;
        }
        mAdapter.notifyDataSetChanged();
    }

    private void selectAll() {
        for (TaskInfo info :
                mUserTasks) {
            info.isChecked = true;
        }
        for (TaskInfo info :
                mSysTasks) {
            info.isChecked = true;
        }
        mAdapter.notifyDataSetChanged();
    }


    private class ProcessAdapter extends BaseAdapter{
        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount()+1;
        }

        /**
         *
         * @param position
         * @return 0代表文本，1代表process
         */
        @Override
        public int getItemViewType(int position) {
            if (position == 0 || position == mUserTasks.size()+1 ){
                return 0;
            }else {
                return 1;
            }
        }

        @Override
        public int getCount() {
            return mUserTasks.size()+mSysTasks.size()+2;
        }

        @Override
        public TaskInfo getItem(int position) {
            if(position>0&&position<mUserTasks.size()+1){
                return mUserTasks.get(position-1);
            }else {
                if (position>mUserTasks.size()+1){
                    return  mSysTasks.get(position-mUserTasks.size()-2);
                }
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            switch (getItemViewType(position)){
                case 0:
                    View view = View.inflate(getApplicationContext(), R.layout.item_list_apps_title, null);
                    TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                    if (position == 0){
                        tv_title.setText("用戶進程");
                    }
                    if (position == mUserTasks.size()+1){
                        tv_title.setText("系統進程");
                    }
                    return view;
                case 1:
                    TaskInfo info = getItem(position);

                    ViewHolder holder = null ;
                    if (convertView == null){
                        holder = new ViewHolder();
                        convertView = View.inflate(getApplicationContext(),R.layout.item_list_process,null);
                        holder.iv_process_icon = (ImageView) convertView.findViewById(R.id.iv_process_icon);
                        holder.tv_process_name = (TextView) convertView.findViewById(R.id.tv_process_name);
                        holder.tv_process_mem = (TextView) convertView.findViewById(R.id.tv_process_mem);
                        holder.cb_clean = (CheckBox) convertView.findViewById(R.id.cb_clean);
                        convertView.setTag(holder);
                    }else {
                        holder = (ViewHolder) convertView.getTag();
                    }
                    holder.iv_process_icon.setBackgroundDrawable(info.task_icon);
                    holder.tv_process_name.setText(info.task_name);
                    holder.tv_process_mem.setText(info.task_memory);
                    holder.cb_clean.setChecked(info.isChecked);
                    return convertView;
            }
            return null;
        }

    }
    static class ViewHolder{
        ImageView iv_process_icon;
        TextView tv_process_name,tv_process_mem;
        CheckBox cb_clean;
    }


    /**
     * 初始化顶部的进程数以及ram情况
     */

    private void initTitle() {
        mTv_process_size.setText("总数："+TaskInfoEngine.getTaskCount(getApplicationContext())+"");
        mTv_ram.setText("Ram："+TaskInfoEngine.getAvailableRam(getApplicationContext())+"/"+
            TaskInfoEngine.getAllRam(getApplicationContext()));
    }

    /**
     * 找到所有的控件
     */
    private void initUI() {
        mTv_process_size = (TextView) findViewById(R.id.tv_process_size);
        mTv_ram = (TextView) findViewById(R.id.tv_ram);
        mLv_process = (ListView) findViewById(R.id.lv_process);
        mBtn_select_all = (Button) findViewById(R.id.btn_select_all);
        mBtn_select_reverse = (Button) findViewById(R.id.btn_select_reverse);
        mBtn_clean = (Button) findViewById(R.id.btn_clean);
        mBtn_setting = (Button) findViewById(R.id.btn_setting);
    }

}
