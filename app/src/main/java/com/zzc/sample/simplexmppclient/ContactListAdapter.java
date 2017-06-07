package com.zzc.sample.simplexmppclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created : zzc
 * Time : 2017/6/6
 * Email : zzc1259@163.com
 * Description : ${desc}
 */

public class ContactListAdapter extends BaseAdapter {
    private List<User> mList;
    private Context mContext;

    public ContactListAdapter(List<User> list, Context context) {
        mList = list == null ? new ArrayList<User>() : list;
        mContext = context;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_contact, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(mList.get(position).getHostName());
        return convertView;
    }

    private class ViewHolder {
        TextView tvName;

        public ViewHolder(View convertView) {
            tvName = (TextView) convertView.findViewById(R.id.tv_name);
        }
    }
}
