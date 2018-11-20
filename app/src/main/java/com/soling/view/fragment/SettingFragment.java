package com.soling.view.fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.soling.R;
import com.soling.service.player.PlayerService;
import com.soling.view.activity.AboutActivity;
import com.soling.view.activity.HelpActivity;
import com.soling.view.adapter.ShakeListener;
import com.soling.view.adapter.ShakeListener.OnShakeListener;
import com.soling.view.adapter.TimePickerDialog;
import com.soling.view.adapter.WiperSwitch;
import com.soling.view.adapter.WiperSwitch.IOnChangedListener;

public class SettingFragment extends Fragment implements View.OnClickListener {

    private WiperSwitch wiperSwitch1;// 摇一摇
    private WiperSwitch wiperSwitch2;// 线控
    private AlertDialog alertDialog;// 对话框
    private TimePickerDialog timePickerDialog;
    private Context context;
    private OnShakeListener onShakeListener;
    private ShakeListener shakeListener;// 震动监听
    private PlayerFragment playerFragment = new PlayerFragment();
    private PlayerService playerService;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater
                .inflate(R.layout.fragment_setting, container, false);
        context = getActivity();
        // 定时停止
        view.findViewById(R.id.id_button_ontimestop).setOnClickListener(
                new OnClickListener() {

                    public void onClick(View arg0) {
                        //System.out.println("===========");
                        final String[] items = {"不开启", "十分钟后", "二十分钟后",
                                "三十分钟后", "自定义"};
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
                                context);
                        alertBuilder.setTitle("定时停止播放");
                        // DialogInterface.OnClickListener() {单选
                        alertBuilder.setItems(items,
                                new DialogInterface.OnClickListener() { // 列表

                                    public void onClick(
                                            DialogInterface dialogInterface,
                                            final int i) {
                                        if (i < items.length - 1) {
                                            // 停止
                                            class Test {
                                                public void main(String[] args) {
                                                    Test test = new Test();
                                                    MyThread myThread = test.new MyThread();
                                                    myThread.start();
                                                }

                                                class MyThread extends Thread {
                                                    @Override
                                                    public void run() {
                                                        super.run();
                                                        try {
                                                            Thread.currentThread().sleep(Integer.parseInt(items[i]) * 60000);
                                                        } catch (InterruptedException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
                                            }
                                            Intent intent = new Intent(context, PlayerService.class);
                                            intent.setAction(PlayerService.ACTION_PLAY_TOGGLE);
                                            context.startService(intent);
                                            //System.out.println("thread");
                                            Toast.makeText(context, items[i], Toast.LENGTH_SHORT).show();
                                        } else if (i == items.length - 1) {
                                            // 自定义shijianxuanzeqi
                                            timePickerDialog = new TimePickerDialog(context);
                                            Intent intent = new Intent(context, PlayerService.class);
                                            intent.setAction(PlayerService.ACTION_PLAY_TOGGLE);
                                            context.startService(intent);
                                        }
                                    }
                                });
                        alertDialog = alertBuilder.create();
                        alertDialog.show();
                    }
                });

        // 摇一摇切歌
        shakeListener = new ShakeListener(context);
        shakeListener.stop();
        shakeListener.setOnShakeListener(new OnShakeListener() {

            public void onShake() {
                // qiege
                Intent service = new Intent(context, PlayerService.class);
                service.setAction(PlayerService.ACTION_PLAY_NEXT);
                context.startService(service);
//                            if (service!=null){
//                            	playerService.playNext();
//							}

                Toast.makeText(context, "sqiege", Toast.LENGTH_SHORT).show();
            }
        });

        wiperSwitch1 = (WiperSwitch) view
                .findViewById(R.id.id_kaiguan_shakesong);
        wiperSwitch1.setChecked(false);// 初始冠
        wiperSwitch1.setOnChangedListener(new IOnChangedListener() {

            public void onChange(WiperSwitch wiperSwitch, boolean checkStat) {
                if (checkStat) {
                    Toast.makeText(context, "开关已打开", Toast.LENGTH_SHORT).show();
                    shakeListener.start();// 开关打开时开始监听震动
                } else if (!checkStat) {
                    shakeListener.stop();// 开关关闭时停止监听震动
                    Toast.makeText(context, "开关已关闭", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 线控切歌
        wiperSwitch2 = (WiperSwitch) view
                .findViewById(R.id.id_kaiguan_linecontrol);// main.xml获取setting.xml
        wiperSwitch2.setChecked(false);// 初始冠
        wiperSwitch2.setOnChangedListener(new IOnChangedListener() {

            public void onChange(WiperSwitch wiperSwitch, boolean checkStat) {
                Intent intent = new Intent(context, PlayerService.class);
                if (checkStat) {
                    Toast.makeText(context, "开关已打开", Toast.LENGTH_SHORT).show();
                    intent.setAction(PlayerService.ACTION_PLAY_NEXT);
                    context.startService(intent);
                } else if (!checkStat) {
                    Toast.makeText(context, "开关已关闭", Toast.LENGTH_SHORT).show();
                    intent.setAction(PlayerService.ACTION_PLAY_TOGGLE);
                    context.startService(intent);
                }
            }
        });

        // 关于V乐
        view.findViewById(R.id.id_ib_about).setOnClickListener(
                new OnClickListener() {
                    public void onClick(View arg0) {
                        Intent intent = new Intent(context, AboutActivity.class);
                        startActivity(intent);
                    }
                });


        //help
        view.findViewById(R.id.id_ib_help).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, HelpActivity.class);
                        startActivity(intent);
                    }
                });
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View arg0) {
        // TODO Auto-generated method stub

    }

}

