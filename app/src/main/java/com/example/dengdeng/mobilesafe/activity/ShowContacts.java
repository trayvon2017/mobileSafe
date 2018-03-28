package com.example.dengdeng.mobilesafe.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dengdeng.mobilesafe.R;
import com.example.dengdeng.mobilesafe.utils.ConstantValues;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowContacts extends AppCompatActivity {

    private ArrayList<HashMap> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contacts);
        initData();
        initUI();
    }

    /**
     * 读取本地联系人
     */
    private void initData() {
        list = new ArrayList<>();
        ContentResolver resolver = getContentResolver();
        //先查询raw_contacts表
        Cursor cursor = resolver.query(Uri.parse("content://com.android.contacts/raw_contacts"), new String[]{"contact_id"},
                null, null, null);
        while (cursor.moveToNext()){
            //HashMap保存得到的数据
            HashMap<String, String> map = new HashMap<>();
            String contact_id = cursor.getString(cursor.getColumnIndex("contact_id"));
            Cursor cursor1 = resolver.query(Uri.parse("content://com.android.contacts/data"), new String[]{"data1", "mimetype"},
                    "raw_contact_id = ?",new String[]{contact_id},null);
            while (cursor1.moveToNext()){
                //通过mimetypes判断是电话号码还是名字
                String data1 = cursor1.getString(cursor1.getColumnIndex("data1"));
                String mimetype = cursor1.getString(cursor1.getColumnIndex("mimetype"));
                if (!TextUtils.isEmpty(mimetype)){
                    if ("vnd.android.cursor.item/phone_v2".equals(mimetype)){
                        //data1是手机号
                        map.put("phone",data1);
                    }else {
                        //data1是名字
                        map.put("name",data1);
                    }
                }
                list.add(map);
            }
            cursor1.close();

        }
        cursor.close();
    }

    private void initUI() {
        ListView lv_contacts = (ListView) findViewById(R.id.lv_contacts);
        lv_contacts.setAdapter(new MyAdapter());
        lv_contacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String phone = (String )list.get(position).get("phone");
                Intent intent = getIntent();
                intent.putExtra("phone",phone);
                setResult(ConstantValues.SHOWCONTACTS_OK,intent);
                finish();
            }
        });
    }


    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
//            ViewHolder holder = null;
            if(convertView == null){
//                holder = new ViewHolder();
                view = View.inflate(getApplicationContext(), R.layout.item_contacts, null);
//                holder.tv_contact_name = (TextView) view.findViewById(R.id.tv_contact_name);
//                holder.tv_contact_phone = (TextView) view.findViewById(R.id.tv_contact_phone);

            }else{
                view = convertView;
            }
            TextView tv_contact_name = (TextView) view.findViewById(R.id.tv_contact_name);
            TextView tv_contact_phone = (TextView) view.findViewById(R.id.tv_contact_phone);
            HashMap<String, String> contact = (HashMap<String, String>) getItem(position);
            tv_contact_name.setText(contact.get("name"));
            tv_contact_phone.setText(contact.get("phone"));

            return view;
        }
    }
   /* class ViewHolder{
        TextView tv_contact_name;
        TextView tv_contact_phone;
    }*/
}
