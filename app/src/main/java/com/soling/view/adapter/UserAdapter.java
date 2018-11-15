package com.soling.view.adapter;

import java.util.List;



import com.soling.model.User;
import com.soling.R;

import android.content.Context;
import android.content.Intent;
import android.icu.util.BuddhistCalendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class UserAdapter extends ArrayAdapter<User> {
	private Context context;
	private LayoutInflater inflater;
	private List<User> list;
	private int ResourceId;
	public UserAdapter(Context context,int textViewResourceId, List<User> list) {
		super(context, textViewResourceId,list);		
		ResourceId=textViewResourceId;		
	}
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		User user = getItem(position);
		View view;
		if(convertView==null){
			view = LayoutInflater.from(getContext()).inflate(ResourceId, null);
		}else{
			view = convertView;
		}
		TextView newsTextView1 = (TextView) view.findViewById(R.id.tv_word);
		TextView newsTextView2 = (TextView) view.findViewById(R.id.tv_name);
		newsTextView1.setText(user.getUserId());
		newsTextView2.setText(user.getNickName());
		return view;
/*		ViewHolder holder;
		if(convertView!=null){
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.person_list, null);
			holder.tv_name = view.findViewById(R.id.tv_name);
			holder.tv_word = view.findViewById(R.id.tv_word);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) view.getTag();
		}
		String word = list.get(position).getHeadWord();
		holder.tv_word.setText(word);
		holder.tv_name.setText(list.get(position).getNickName());
		convertView.setOnClickListener(new OnClickListener() {			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(context, InformationActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("user_id", list.get(position).getUserId());
				intent.putExtras(bundle);
				context.startActivity(intent);
			}
		});
		
		 * 将相同字字母开头的合并到一起
		 
		if(position==0){
			holder.tv_word.setVisibility(View.VISIBLE);
		}else{
			String headword = list.get(position).getHeadWord();
			if(word.equals(headword)){
				holder.tv_word.setVisibility(View.GONE);
			}else{
				holder.tv_word.setVisibility(View.VISIBLE);
			}
		}
		return view;
	}
	private class ViewHolder{
		private TextView tv_word;
		private TextView tv_name;
	}
	*/
}
	}

