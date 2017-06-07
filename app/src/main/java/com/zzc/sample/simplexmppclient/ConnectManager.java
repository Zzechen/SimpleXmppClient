package com.zzc.sample.simplexmppclient;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.util.Log;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created : zzc
 * Time : 2017/6/6
 * Email : zzc1259@163.com
 * Description : ${desc}
 */

public class ConnectManager {
    public static final String ACTION_RECEIVE = "com.zzc.xmpp.receive";
    public static final String EXTRA_BODY = "body";

    private static volatile ConnectManager instance;
    private XMPPTCPConnection mConnection;
    private XMPPTCPConnectionConfiguration mConfiguration;
    private ChatMessageListener mMessageListener = new MessageListenerImpl();
    private ChatManagerListenerImpl mChatManagerListener = new ChatManagerListenerImpl();


    private ConnectManager() {
    }

    public static ConnectManager getInstance() {
        if (instance == null) {
            synchronized (ConnectManager.class) {
                if (instance == null) {
                    instance = new ConnectManager();
                }
            }
        }
        return instance;
    }

    public boolean isConnected() {
        if (mConnection != null) return mConnection.isConnected();
        return false;
    }

    public void connect(final ResultListener listener) {
        initConnect();
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    mConnection.connect();
                    return true;
                } catch (SmackException | IOException | XMPPException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean aVoid) {
                if (listener != null) {
                    listener.result(aVoid);
                }
            }
        }.execute();
    }

    private void initConnect() {
        if (mConfiguration == null) {
            mConfiguration = XMPPTCPConnectionConfiguration.builder()
                    .setHost("192.168.1.8")
                    .setPort(5222)
                    .setServiceName("192.168.1.8")
                    .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                    .build();
        }
        if (mConnection == null) {
            mConnection = new XMPPTCPConnection(mConfiguration);
        }
    }

    public boolean isLogin() {
        return mConnection != null && mConnection.isAuthenticated();
    }

    public interface ResultListener {
        void result(boolean login);
    }

    public void login(final String name, final String psd, final ResultListener listener) {
        if (!isLogin()) {
            new AsyncTask<Void, Void, Boolean>() {

                @Override
                protected Boolean doInBackground(Void... params) {
                    try {
                        if (mConnection == null) {
                            initConnect();
                        }
                        if (!mConnection.isConnected()) {
                            mConnection.connect();
                        }
                        mConnection.login(name, psd);
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }

                @Override
                protected void onPostExecute(Boolean aVoid) {
                    if (listener != null) {
                        listener.result(aVoid);
                    }
                    if (aVoid) {
                        ChatManager.getInstanceFor(mConnection).addChatListener(mChatManagerListener);
                    }
                }
            }.execute();
        } else {
            if (listener != null) {
                listener.result(true);
            }
        }
    }

    public List<User> getContacts() {
        Roster roster = Roster.getInstanceFor(mConnection);
        List<User> users = new ArrayList<>();
        for (RosterEntry entry : roster.getUnfiledEntries()) {
            User user = new User();
            user.setName(entry.getName());
            user.setHostName(entry.getUser());
            users.add(user);
        }
        return users;
    }

    public void sendMsg(ChatMessage msg) {
        if (!isLogin()) return;
        Chat chat = ChatManager.getInstanceFor(mConnection).createChat(msg.getReceiver());
        Message message = new Message();
        message.setBody(msg.getBody());
        message.setType(msg.getType());
        try {
            chat.sendMessage(message);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    public static final String TAG = "ConnectManager";

    private class MessageListenerImpl implements ChatMessageListener {

        @Override
        public void processMessage(Chat chat, Message message) {
            if (message.getBody() != null) {
                Intent intent = new Intent(ACTION_RECEIVE);
                intent.putExtra(EXTRA_BODY, message.getBody());
                App.getInstance().sendBroadcast(intent);
            }
        }
    }

    private class ChatManagerListenerImpl implements ChatManagerListener {

        @Override
        public void chatCreated(Chat chat, boolean createdLocally) {
            if (!createdLocally) {
                chat.addMessageListener(mMessageListener);
            }
        }
    }
}
