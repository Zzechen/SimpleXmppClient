package com.zzc.sample.simplexmppclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created : zzc
 * Time : 2017/6/6
 * Email : zzc1259@163.com
 * Description : ${desc}
 */

public class ChatActivity extends AppCompatActivity {
    private String mJid;
    private ListView mLvChat;
    private EditText mEdtMsg;
    private ImageButton mIbSend;
    private ArrayList<ChatMessage> mList;
    private ChatAdapter mAdapter;
    private ChatReceiver mReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mJid = getIntent().getStringExtra(ContactListActivity.KEY_JID);
        mLvChat = (ListView) findViewById(R.id.lv_chat);
        mEdtMsg = (EditText) findViewById(R.id.edt_msg);
        mIbSend = (ImageButton) findViewById(R.id.ib_send);
        mLvChat.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        mLvChat.setStackFromBottom(true);

        mList = new ArrayList<>();
        mAdapter = new ChatAdapter(mList, this);
        mLvChat.setAdapter(mAdapter);

        mIbSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsg();
            }
        });
        mReceiver = new ChatReceiver();
        registerReceiver(mReceiver, new IntentFilter(ConnectManager.ACTION_RECEIVE));
    }

    private void sendMsg() {
        String txt = mEdtMsg.getText().toString().trim();
        if (TextUtils.isEmpty(txt)) return;
        ChatMessage msg = new ChatMessage(LoginActivity.LOGINER, mJid, txt, "", true);
        msg.setMsgID();
        ConnectManager.getInstance().sendMsg(msg);
        mAdapter.add(msg);
        mEdtMsg.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    private class ChatReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String body = intent.getStringExtra(ConnectManager.EXTRA_BODY);
            ChatMessage msg = new ChatMessage(mJid, LoginActivity.LOGINER, body, "", false);
            msg.setMsgID();
            mAdapter.add(msg);
        }
    }
}
