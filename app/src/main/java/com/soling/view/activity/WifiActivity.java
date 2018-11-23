package com.soling.view.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.soling.App;
import com.soling.R;
import com.soling.utils.WifiUtil;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class WifiActivity extends BaseActivity implements View.OnClickListener {

    private Button scan_button;
    private TextView wifi_result_textview;
    private WifiManager wifiManager;
    private WifiInfo currWifiInfo;//当前已连接的WiFi
    private List<ScanResult> wifiList;
    private String[] str;
    private int wifiIndex;
    private ProgressDialog progressDialog;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_wifi);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        setViews();
        setListener();
    }

    @Override
    protected void onResume() {
        openWifi();
        currWifiInfo = wifiManager.getConnectionInfo();
        wifi_result_textview.setText("当前网络：" + currWifiInfo.getSSID() + " IP:" + WifiUtil.changeIP(currWifiInfo.getIpAddress()));
        new ScanWifiThread().start();
        super.onResume();
    }

    public void setViews() {
        scan_button = findViewById(R.id.scan_button);
        wifi_result_textview = findViewById(R.id.wifi_result_textview);
        //System.out.println("scan_button====="+scan_button);
    }

    public void setListener() {
        scan_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scan_button:
                lookupScan();
                break;
        }
    }

    public void openWifi() {
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
    }

    //扫描可用WiFi
    class ScanWifiThread extends Thread {
        @Override
        public void run() {
            while (true) {
                currWifiInfo = wifiManager.getConnectionInfo();
                startScan();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }

    public void startScan() {
        wifiManager.startScan();
        wifiList = wifiManager.getScanResults();
//        if (wifiList == null) {
//            if (wifiManager.getWifiState() == 3) {
//                shortToast("当前区域没有可用WiFi网络");
//            }
//        }
        //id为空
        for (ScanResult result : wifiList) {
            if (result.SSID == null || result.SSID.length() == 0) {
                continue;
            }
        }
        str = new String[wifiList.size()];
        String temStr = null;
        for (int i = 0; i < wifiList.size(); i++) {
            temStr = wifiList.get(i).SSID;
            if (null != currWifiInfo && temStr.equals(currWifiInfo.getSSID())) {
                temStr = temStr + "已连接";
            }
            str[i] = temStr;
        }
    }

    //查看扫描结果
    private void lookupScan() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        wifiList = wifiManager.getScanResults();
        if (wifiList == null) {
            shortToast("当前区域没有可用WiFi网络");
        } else {
            dialog.setTitle("可用WiFi网络");
            dialog.setItems(str, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    wifiIndex = which;
                    handler.sendEmptyMessage(3);
                }
            });
            dialog.show();
        }
    }

    class RefreshSsidThread extends Thread {
        @Override
        public void run() {
            final boolean[] flag = {true};
/*            timer=new Timer();
            TimerTask task=(new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(1);
                }
            };
            timer.schedule(task,60000);*/
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    flag[0] = false;
                }
            }, 5000);
            while (flag[0]) {
                currWifiInfo = wifiManager.getConnectionInfo();
                if (currWifiInfo.getSSID() != null && currWifiInfo.getIpAddress() != 0) {
                    flag[0] =false;
//                    break;
                }
            }
            handler.sendEmptyMessage(4);
            super.run();
        }
    }

    public void connectionConfig(int index, String password) {
        progressDialog = ProgressDialog.show(this, "正在连接...", "请稍后...");
        new ConnectWifiThread().execute(index + "", password);
    }

    class ConnectWifiThread extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            int index = Integer.parseInt(strings[0]);
            if (index > wifiList.size()) {
                return null;
            }
            //连接配置好指定id的网络
            WifiConfiguration config = WifiUtil.createWifiInfo(wifiList.get(index).SSID, strings[1], 3, wifiManager);
            int netId = wifiManager.addNetwork(config);
            if (config != null) {
                wifiManager.enableNetwork(netId, true);
                return wifiList.get(index).SSID;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if (s != null) {
                handler.sendEmptyMessage(0);
            } else {
                handler.sendEmptyMessage(1);
            }
            super.onPostExecute(s);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    wifi_result_textview.setText("正在获取IP地址...");
                    new RefreshSsidThread().start();
                    break;
                case 1:
                    shortToast("连接失败");
                    break;
                case 3:
                    View view = LayoutInflater.from(App.getInstance()).inflate(R.layout.layout_wifi_password, null);
                    final AlertDialog.Builder alert = new AlertDialog.Builder(WifiActivity.this);//不能用App.getInstance()
                    final EditText etPassword = (EditText) view.findViewById(R.id.et_password);

                    CheckBox cbPassword = view.findViewById(R.id.cb_password);
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());//设置密码不可见
                    cbPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            } else {
                                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            }
                            //光标放在末尾
                            CharSequence charSequence = etPassword.getText();
                            if (charSequence instanceof Spannable) {
                                Spannable spannable = (Spannable) charSequence;
                                Selection.setSelection(spannable, charSequence.length());
                            }
                        }
                    });

                    alert.setTitle("请输入密码");
                    alert.setView(view);
                    final String pwd = etPassword.getText().toString();
                    alert.setPositiveButton("连接", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            if (pwd.length() < 8 || pwd == null) {
//                                shortToast("密码不能少于8位，请重新输入");
//                            }
                            connectionConfig(wifiIndex, pwd);
                        }
                    });
                    alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                    alert.create().show();
                    break;
                case 4:
                    currWifiInfo = wifiManager.getConnectionInfo();
                    if (currWifiInfo.getSSID() != null && currWifiInfo.getIpAddress() != 0) {

                        shortToast("连接成功！");
                        wifi_result_textview.setText("当前网络：" + currWifiInfo.getSSID() + " IP:" + WifiUtil.changeIP(currWifiInfo.getIpAddress()));
//                    break;
                    }
                    else {
                        shortToast("连接失败！");
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

/*    @Override
    protected void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }*/
}
