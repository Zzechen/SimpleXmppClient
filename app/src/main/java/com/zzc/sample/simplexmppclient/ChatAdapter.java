package com.zzc.sample.simplexmppclient;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created : zzc
 * Time : 2017/6/6
 * Email : zzc1259@163.com
 * Description : ${desc}
 */

public class ChatAdapter extends BaseAdapter {
    private List<ChatMessage> mList;
    private LayoutInflater mInflater;

    public ChatAdapter(List<ChatMessage> list, Context context) {
        mList = list == null ? new ArrayList<ChatMessage>() : list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_chat, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ChatMessage chatMessage = mList.get(position);
        if (chatMessage.isMine()) {
            holder.bg.setBackgroundResource(R.drawable.bubble2);
            holder.container.setGravity(Gravity.RIGHT);
        } else {
            holder.bg.setBackgroundResource(R.drawable.bubble1);
            holder.container.setGravity(Gravity.LEFT);
        }
        holder.tvMsg.setText(chatMessage.getBody());
        return convertView;
    }

    public void add(ChatMessage message) {
        mList.add(message);
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        LinearLayout container;
        ViewGroup bg;
        TextView tvMsg;

        public ViewHolder(View convertView) {
            container = (LinearLayout) convertView.findViewById(R.id.chat_container);
            bg = (ViewGroup) convertView.findViewById(R.id.chat_bg);
            tvMsg = (TextView) convertView.findViewById(R.id.tv_msg);
        }
    }
}
