package com.soling.view.fragment;

import java.io.Serializable;
import java.util.List;

import com.soling.model.FloatDragView;
import com.soling.model.PhoneCallLog;
import com.soling.model.PhoneDto;
import com.soling.model.PhoneInformation;
import com.soling.model.User;
import com.soling.presenter.MainActivityInterface;
import com.soling.utils.FloatButtonUtil;
import com.soling.utils.PhoneUtil;
import com.soling.view.activity.InformationActivity;
import com.soling.view.activity.MainActivity;
import com.soling.view.adapter.ListAdapter;
import com.soling.view.adapter.PhoneAdapter;
import com.soling.view.adapter.PhoneCallLogAdapter;
import com.soling.view.adapter.PhoneInformationAdapter;
import com.soling.view.adapter.UserAdapter;
import com.soling.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PhoneFragment extends Fragment implements MainActivityInterface {

    private List<User> users;
    private ListView personListView;
    private UserAdapter userAdapter;
    private View view;
    private String userId;
    private User user;
    private PhoneAdapter phoneAdapter;
    private PhoneCallLogAdapter phoneCallLogAdapter;
    private PhoneInformationAdapter phoneInformationAdapter;
    private ListAdapter listAdapter;
    private List<PhoneDto> phoneDtos;
    private List<PhoneCallLog> phoneCallLogs;
    private List<PhoneInformation> phoneInformations;

    //	private ImageView myHead;
//	private TextView myName;
    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.person_lists, container, false);
        personListView = view.findViewById(R.id.person_list);
        RelativeLayout relativeLayout = view.findViewById(R.id.my_list);
        refreshPhoneList1();
        personListView.setAdapter(listAdapter);
        addFloatButton(relativeLayout);
        personListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                PhoneDto phoneDto = phoneDtos.get(arg2);
                Intent intent = new Intent(getActivity(),InformationActivity.class);
                intent.putExtra("name", phoneDto.getName());
                intent.putExtra("phoneNumber", phoneDto.getTelPhone());
                intent.putExtra("id", phoneDto.getId());
                startActivity(intent);
            }
        });
        return view;
    }

    public void refreshPhoneList(User user, List<User> users) {
        // TODO Auto-generated method stub
//		user = UserList.buildUser("h", new Date(16660812), 1, 1, 40000);
//		users  =UserList.getUsers();
        ImageView myHeading = view.findViewById(R.id.tvmy_heading);
        TextView myNickname = view.findViewById(R.id.tvmy_name);
        TextView myStatus = view.findViewById(R.id.tvmy_status);
        final int minCircularSize = Math.min(user.getAvatarUrl().getHeight(), user.getAvatarUrl().getWidth());
        int mCornerRadius = minCircularSize / 2;
        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(view.getResources(), user.getAvatarUrl());
        drawable.setCircular(true);
        drawable.setCornerRadius(mCornerRadius);
        myHeading.setImageDrawable(drawable);
        myNickname.setText(user.getNickName());
        myStatus.setText(user.getStatus());
        //userAdapter = new UserAdapter(getActivity(),R.layout.person_list,users);
    }

    public void refreshPhoneList1() {
        PhoneDto phoneDto = new PhoneDto();
        PhoneUtil phoneUtil = new PhoneUtil(view.getContext());
        phoneDtos = phoneUtil.getPhoneList();
        listAdapter = new ListAdapter(getActivity(), phoneDtos);
        //phoneAdapter = new PhoneAdapter(getActivity(),R.layout.person_list,phoneDtos);
    }

    public void addFloatButton(final RelativeLayout relativeLayout) {
        FloatDragView.addFloatDragView(this.getActivity(), relativeLayout, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView text2 = view.findViewById(R.id.tv_text2);
                switch (text2.getText().toString()) {
                    case "好友列表":
                        text2.setText("通话记录");
                        refreshPhoneCallLog();
                        personListView.setAdapter(phoneCallLogAdapter);
                        addFloatButton(relativeLayout);
                        break;
                    case "通话记录":
                        text2.setText("信息记录");
                        refreshPhoneInformation();
                        personListView.setAdapter(phoneInformationAdapter);
                        addFloatButton(relativeLayout);
                        break;
                    case "信息记录":
                        text2.setText("好友列表");
                        refreshPhoneList1();
                        personListView.setAdapter(listAdapter);
                        addFloatButton(relativeLayout);
                        break;
                }
            }
        });
    }

    public void refreshPhoneCallLog() {
        PhoneCallLog phoneCallLog = new PhoneCallLog();
        PhoneUtil phoneUtil = new PhoneUtil(view.getContext());
        phoneCallLogs = phoneUtil.getPhoneCallLog();
        phoneCallLogAdapter = new PhoneCallLogAdapter(getActivity(), phoneCallLogs);
    }

    public void refreshPhoneInformation() {
        PhoneInformation phoneInformation = new PhoneInformation();
        PhoneUtil phoneUtil = new PhoneUtil(view.getContext());
        phoneInformations = phoneUtil.getInformationList();
        phoneInformationAdapter = new PhoneInformationAdapter(getActivity(), phoneInformations);
    }


//	}

}
