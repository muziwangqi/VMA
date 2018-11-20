package com.soling.view.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.soling.R;
import com.soling.view.activity.LightActivity;
import com.soling.view.activity.ThemeActivity;
import com.soling.view.activity.WirelessActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SettingModuleFragment extends BaseFragment {

    private Button btnLight, btnWireless,btnTheme;
    private ImageButton ibtnLight, ibtnWireless,ibtnTheme;
    private EditText etTime;
    private DatePickerDialog datePickerDialog;

    Calendar calendar=Calendar.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settingmodule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView();
        setListener();
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("yyyy年MM月dd日");
        SimpleDateFormat date=new SimpleDateFormat(stringBuilder.toString());
        etTime.setText(date.format(new Date()));
        System.out.println("------date--"+date.format(new Date()));
    }

    private void initView() {
        etTime= (EditText) findViewById(R.id.et_time);
        btnLight= (Button) findViewById(R.id.btn_light);
        btnWireless= (Button) findViewById(R.id.btn_wireless);
        btnTheme= (Button) findViewById(R.id.btn_theme);
        ibtnLight= (ImageButton) findViewById(R.id.ibtn_light);
        ibtnWireless= (ImageButton) findViewById(R.id.ibtn_wireless);
        ibtnTheme= (ImageButton) findViewById(R.id.ibtn_theme);
    }

    private void setListener() {
        etTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showTimeDialog();
                    return true;
                }
                return false;
            }
        });

        etTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showTimeDialog();
                }
            }
        });

        btnWireless.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WirelessActivity.class);
                startActivity(intent);
            }
        });

        ibtnWireless.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WirelessActivity.class);
                startActivity(intent);
            }
        });

        btnLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LightActivity.class);
                startActivity(intent);
            }
        });

        ibtnLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LightActivity.class);
                startActivity(intent);
            }
        });

        btnTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ThemeActivity.class);
                startActivity(intent);
            }
        });

        ibtnTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ThemeActivity.class);
                startActivity(intent);
            }
        });
    }

    public void showTimeDialog() {
        Calendar calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                etTime.setText(year + "年" + month + "月" + dayOfMonth + "日");
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}
