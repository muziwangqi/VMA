package com.soling.view.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.soling.R;
import com.soling.presenter.SetLight;
import com.soling.view.activity.LightActivity;
import com.soling.view.activity.WirelessActivity;

import java.util.Calendar;

public class SettingModuleFragment extends Fragment {

    private Button btnLight,btnWireless,btnLess,btnMore;
    private ImageButton ibtnLight,ibtnWireless;
    private Context context;
    private EditText etTime;
    private DatePickerDialog datePickerDialog;
    private ContentResolver contentResolver;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_settingmodule,container,false);
        context=getActivity();

        etTime=(EditText) view.findViewById(R.id.et_time);
        btnWireless=(Button)view.findViewById(R.id.btn_wireless);
        ibtnWireless=(ImageButton)view.findViewById(R.id.ibtn_wireless);
        btnLight=(Button)view.findViewById(R.id.btn_light);
        ibtnLight=(ImageButton)view.findViewById(R.id.ibtn_light);
//        btnLess=(Button)view.findViewById(R.id.btn_less);
//        btnMore=(Button)view.findViewById(R.id.btn_more);

        etTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    showTimeDialog();
                    return true;
                }
                return false;
            }
        });
        etTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    showTimeDialog();
                }
            }
        });

        btnWireless.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),WirelessActivity.class);
                startActivity(intent);
            }
        });
        ibtnWireless.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),WirelessActivity.class);
                startActivity(intent);
            }
        });

        btnLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),LightActivity.class);
                startActivity(intent);
            }
        });

        ibtnLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),LightActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }

    public void showTimeDialog(){
        Calendar calendar=Calendar.getInstance();
        datePickerDialog=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                etTime.setText(year+"年"+month+"月"+dayOfMonth+"日");
            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


}
