package com.soling.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.soling.R;
import com.soling.model.PhoneDto;
import com.soling.model.PhoneInformation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class PhoneInformationAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<PhoneInformation> list;
    public PhoneInformationAdapter(Context context, List<PhoneInformation> list){
        this.inflater=LayoutInflater.from(context);
        this.list = list;
    }
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
        SortAdapter.ViewHolder holder;
        if(convertView==null){
            convertView = inflater.inflate(R.layout.information_log,null);
            holder = new SortAdapter.ViewHolder();
             holder.informationNumber= convertView.findViewById(R.id.info_number);
            holder.informationTime = convertView.findViewById(R.id.info_time);
            holder.informationContext = convertView.findViewById(R.id.info_content);
            convertView.setTag(holder);
        }else{
            holder = (SortAdapter.ViewHolder)convertView.getTag();
        }
        PhoneInformation cv = list.get(position);
        if(cv.getPerson()==null){
            holder.informationNumber.setText(cv.getStrAddress());
        }else{
            holder.informationNumber.setText(cv.getPerson());
        }
        holder.informationContext.setText(cv.getStrBody());
        holder.informationTime.setText(cv.getDate());
        return convertView;
    }

}
