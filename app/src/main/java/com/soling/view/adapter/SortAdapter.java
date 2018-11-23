package com.soling.view.adapter;

import android.content.Context;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.soling.model.PhoneDto;

import java.util.List;

public class SortAdapter extends BaseAdapter {
    private List<PhoneDto> phoneDtoList = null ;
    private Context context;
    public SortAdapter(List<PhoneDto> phoneDtoList,Context context){
        this.context=context;
        this.phoneDtoList = phoneDtoList;
    }
    @Override
    public int getCount() {
        return this.phoneDtoList.size();
    }

    @Override
    public Object getItem(int position) {
        return phoneDtoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder ;

        return null;
    }
    final static class ViewHolder{
        TextView phone;
        TextView name;
        TextView word;
        TextView informationContext;
        TextView informationTime;
        TextView informationNumber;
        TextView callName;
        TextView callTime;
        TextView callNumber;
        ImageView callType;
        ImageView headPhoto;
        TextView chatContent;
    }
    //获取catalog第一次出现的位置
    public int getPositionForSerction(String catalog){
        for(int i=0;i<getCount();i++){
            String sortStr = phoneDtoList.get(i).getFirstLetter();
            if(catalog.equalsIgnoreCase(sortStr)){
                return i;
            }
        }
        return -1;
    }
}
