package com.soling.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;

import com.soling.R;
import com.soling.model.PhoneDto;
import com.soling.utils.BitmapUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class ListAdapter extends BaseAdapter implements SectionIndexer {
    private LayoutInflater inflater;
    private List<PhoneDto> list;
    private HashMap<String,Integer> alphaIndexer;//保存每个索引在list位置
    private String[] sections;//每个分组的索引表
    private RoundedBitmapDrawable rbd;
    public ListAdapter(Context context, List<PhoneDto> list){
        this.inflater=LayoutInflater.from(context);
        this.list = list;
        this.alphaIndexer = new HashMap<String,Integer>();
        for(int i=0;i<list.size();i++){
            String name = getAlpha(list.get(i).getFirstLetter());
            if(!alphaIndexer.containsKey(name)){
                alphaIndexer.put(name,i);
            }
        }
        Set<String> sectionLetters = alphaIndexer.keySet();
        ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);
        Collections.sort(sectionList);
        sections = new String[sectionList.size()];
        sectionList.toArray(sections);
    }

    public void updateListView(String words , ListView listView){
        for(int i=0;i<list.size();i++){
            String headWord = list.get(i).getFirstLetter();
            if(words.equals(headWord)){
                listView.setSelection(i);
                return ;
            }
        }
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
           holder.word = convertView.findViewById(R.id.person_list_word);
           holder.phone = convertView.findViewById(R.id.person_list_phone);
           holder.name = convertView.findViewById(R.id.person_list_name);
           holder.headPhoto = convertView.findViewById(R.id.person_list_heading);
           Resources r = convertView.getContext().getResources();
           Drawable drawable = r.getDrawable(R.drawable.headphoto);
           Bitmap bmp =  BitmapFactory.decodeResource(r,R.drawable.headphoto);
           rbd = BitmapUtil.roundedBitmapDrawable(bmp);
           convertView.setTag(holder);
       }else{
           holder = (SortAdapter.ViewHolder)convertView.getTag();
       }
        PhoneDto cv = list.get(position);
        String name = cv.getName();
        String number = cv.getTelPhone();
        holder.name.setText(name);
        holder.phone.setText(number);
        holder.headPhoto.setImageDrawable(rbd);
       //当前联系人的sortkey
        String currentStr = getAlpha(list.get(position).getFirstLetter());
        //上一个联系人的sortkey
        String previewStr = (position - 1) >= 0 ? getAlpha(list.get(
                position - 1).getFirstLetter()) : " ";
        /*
        判断显示#\A-Z的textview隐藏与可见
         */
        if(!previewStr.equals(currentStr)){
            holder.word.setVisibility(View.VISIBLE);
            holder.word.setText(currentStr);
        }else{
            holder.word.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    @Override
    public int getSectionForPosition(int position) {
        String key = getAlpha(list.get(position).getFirstLetter());
        for(int i=0;i<sections.length;i++){
            if(sections[i].equals(key)){
                return i;
            }
        }
        return 0;
    }
    /*
    提取英文首字母，非英文用#代替
     */
    private String getAlpha(String str){
        if(str==null){
            return "#";
        }
        if(str.trim().length()==0){
            return "#";
        }
        char c = str.trim().substring(0,1).charAt(0);
        //判断首字母是否为英文
        Pattern pattern = Pattern.compile("[a-zA-Z]+");
        if(pattern.matcher(c+"").matches()){
            return (c+"").toUpperCase();
        }else{
            return "#";
        }
    }
    /*
    根据联系人首字母返回在list中的位置
     */
    @Override
    public int getPositionForSection(int section){
        String later = sections[section];
        return alphaIndexer.get(later);
    }
}
