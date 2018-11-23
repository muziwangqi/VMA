package com.soling.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.soling.R;
import com.soling.model.PhoneDto;
import com.soling.model.PhoneInformation;
import com.soling.utils.BitmapUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ChatAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<PhoneInformation> list;
    public ChatAdapter(Context context, List<PhoneInformation> list){
        this.inflater=LayoutInflater.from(context);
        this.list = list;
    }@Override
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
        SortAdapter.ViewHolder holder = new SortAdapter.ViewHolder();
        if(list.get(position).getType().equals("收到的")){
            if(convertView==null){
                convertView = inflater.inflate(R.layout.chat_get,null);
                holder = new SortAdapter.ViewHolder();
                holder.chatContent = convertView.findViewById(R.id.chatlist_text_other);
                convertView.setTag(holder);
            }else{
                holder = (SortAdapter.ViewHolder)convertView.getTag();
            }
        }else if(list.get(position).getType().equals("已发出")){
            if(convertView==null){
                convertView = inflater.inflate(R.layout.chat_send,null);
                holder = new SortAdapter.ViewHolder();
                holder.chatContent = convertView.findViewById(R.id.chatlist_text_me);
                convertView.setTag(holder);
            }else{
                holder = (SortAdapter.ViewHolder)convertView.getTag();
            }
        }
        PhoneInformation cv = list.get(position);
        String content = cv.getStrBody();
        holder.chatContent.setText(content);
        return convertView;
    }
}
