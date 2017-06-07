package com.zzc.sample.simplexmppclient;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created : zzc
 * Time : 2017/6/6
 * Email : zzc1259@163.com
 * Description : ${desc}
 */

public class ContactListActivity extends AppCompatActivity {
    public static final String KEY_JID = "keyjid";

    private ArrayList<User> mList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mList = new ArrayList<>();
        ListView lv = (ListView) findViewById(R.id.lv);
        ContactListAdapter adapter = new ContactListAdapter(mList, this);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ContactListActivity.this, ChatActivity.class);
                intent.putExtra(KEY_JID, mList.get(position).getHostName());
                startActivity(intent);
            }
        });
        mList.addAll(ConnectManager.getInstance().getContacts());
        adapter.notifyDataSetChanged();
    }

}
