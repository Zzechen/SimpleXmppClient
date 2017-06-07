package com.zzc.sample.simplexmppclient;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private XMPPTCPConnectionConfiguration mConfig;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBtn.setText((String) msg.obj);
        }
    };
    private Button mBtn;
    private XMPPTCPConnection mConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtn = (Button) findViewById(R.id.btn);
    }

    public void connect(View view) {
        if (mConfig == null) {
            try {
                mConfig = XMPPTCPConnectionConfiguration.builder()
                        .setHost("192.168.1.8")
                        .setPort(5222)
                        .setServiceName("192.168.1.8")
                        .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
                Message msg = Message.obtain();
                msg.obj = e.toString();
                mHandler.sendMessage(msg);
            }
        }
        if (mConnection == null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mConnection = new XMPPTCPConnection(mConfig);
                    Message msg = Message.obtain();
                    try {
                        mConnection.connect();
                        mConnection.login("xxx", "xxx");
                        msg.obj = "connect success" + mConnection.isConnected();
                        mHandler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                        msg.obj = e.toString();
                        mHandler.sendMessage(msg);
                    }
                }
            }).start();
        }
    }

    public void send(View view) {
        if (mConnection != null) {

        }
    }
}
