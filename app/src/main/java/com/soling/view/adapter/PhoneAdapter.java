package com.soling.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.soling.R;
import com.soling.model.User;
import com.soling.model.PhoneDto;

import java.util.List;

public class PhoneAdapter extends ArrayAdapter<PhoneDto> {
    private Context context;
    private LayoutInflater inflater;
    private List<User> list;
    private int ResourceId;

    public PhoneAdapter(Context context, int resource, List<PhoneDto> phoneDtoList) {
        super(context, resource, phoneDtoList);
        ResourceId = resource;
    }
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        PhoneDto phoneDto = getItem(position);
        View view;
        if(convertView==null){
            view = LayoutInflater.from(getContext()).inflate(ResourceId, null);
        }else{
            view = convertView;
        }
        ImageView myHeading = view.findViewById(R.id.person_list_heading);
        TextView myNickname = view.findViewById(R.id.person_list_name);
        TextView myStatus = view.findViewById(R.id.person_list_phone);
//        Bitmap bmp = BitmapFactory.decodeResource(view.getResources(), R.drawable.l);
//        final int minCircularSize = Math.min(bmp.getHeight(), bmp.getWidth());
//        int mCornerRadius = minCircularSize / 2;
//        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(view.getResources(), bmp);
//        drawable.setCircular(true);
//        drawable.setCornerRadius(mCornerRadius);
//          myHeading.setImageDrawable(drawable);
        myNickname.setText(phoneDto.getName());
        myStatus.setText(phoneDto.getTelPhone());
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
