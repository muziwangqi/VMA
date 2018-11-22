package com.soling.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.soling.R;
import com.soling.model.PhoneCallLog;

import java.util.List;

public class PhoneCallLogAdapter extends BaseAdapter {

    private static final String TAG = "PhoneCallLogAdapter";
    private LayoutInflater inflater;
    private List<PhoneCallLog> list;
    public PhoneCallLogAdapter(Context context, List<PhoneCallLog> list){
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
            convertView = inflater.inflate(R.layout.person_list,null);
            holder = new SortAdapter.ViewHolder();
            holder.callName= convertView.findViewById(R.id.call_name);
            holder.callTime = convertView.findViewById(R.id.call_time);
            holder.callNumber = convertView.findViewById(R.id.call_number);
            holder.callType = convertView.findViewById(R.id.call_type);
            convertView.setTag(holder);
        }else{
            holder = (SortAdapter.ViewHolder)convertView.getTag();
        }
<<<<<<< 8656da03147f88c063f9719e1159244eb76c09f7
=======
        holder = new SortAdapter.ViewHolder();
        holder.callName= view.findViewById(R.id.call_name);
        holder.callTime = view.findViewById(R.id.call_time);
        holder.callNumber = view.findViewById(R.id.call_number);
        holder.callType = view.findViewById(R.id.call_type);
//        if(convertView==null){
//            convertView = inflater.inflate(R.layout.person_list,null);
//            holder = new SortAdapter.ViewHolder();
//            holder.callName= convertView.findViewById(R.id.call_name);
//            holder.callTime = convertView.findViewById(R.id.call_time);
//            holder.callNumber = convertView.findViewById(R.id.call_number);
//            holder.callType = convertView.findViewById(R.id.call_type);
//            convertView.setTag(holder);
//        }else{
//            holder = (SortAdapter.ViewHolder)convertView.getTag();
//        }
>>>>>>> addinformation
        PhoneCallLog cv = list.get(position);
        if(cv.getType().equals("呼入")){
            Log.e(TAG, "getView: " + holder == null ? "null" : "not null");
            Log.e(TAG, "getView: " + holder.callType == null ? "null" : "not null");
            holder.callType.setImageResource(R.drawable.in);
        }else if(cv.getType().equals("呼出")){
            holder.callType.setImageResource(R.drawable.out);
        }else if(cv.getType().equals("未接")){
            holder.callType.setImageResource(R.drawable.none);
        }
        holder.callNumber.setText(cv.getFormatted_number());
        holder.callTime.setText(cv.getDate());
        holder.callName.setText(cv.getName());
        return view;
    }
}
