package com.soling.view.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.soling.R;
import com.soling.view.adapter.WiperSwitch;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

public class BluetoothActivity extends BaseActivity {

    private static final String TAG = BluetoothActivity.class.getSimpleName();
    private Button btn_open, btn_close, btn_search, btn_my, btn_matchfirst, btn_connect;
    private TextView tv_devices, tv_bluetooth_name, tv_bluetooth_address;
    private BluetoothAdapter bluetoothAdapter;
    private String address = null;
    private BluetoothDevice connectedDevice;
    private static int code = 1;
    public static final String UUID_CONTENT = "00001101-0000-1000-8000-00805F9B34FB";
    private WiperSwitch ws_bttop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_bluetooth2);
        initView();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, intentFilter);
        initListener();
    }

    private void initView() {
//        btn_open = findViewById(R.id.btn_open);
//        btn_close = findViewById(R.id.btn_close);
//        btn_search = findViewById(R.id.btn_search);
        btn_matchfirst = findViewById(R.id.btn_matchfirst);
//        btn_my = findViewById(R.id.btn_my);
        btn_connect = findViewById(R.id.btn_connect);
        tv_devices = findViewById(R.id.tv_devices);
        tv_bluetooth_name = findViewById(R.id.tv_bluetooth_name);
        tv_bluetooth_address = findViewById(R.id.tv_bluetooth_address);
        ws_bttop = findViewById(R.id.ws_bttop);
    }

    private void initListener() {

        ws_bttop.setChecked(false);
        ws_bttop.setOnChangedListener(new WiperSwitch.IOnChangedListener() {
            @Override
            public void onChange(WiperSwitch wiperSwitch, boolean checkStat) {
//                shortToast("lllllllllllllllllllllllllllllllll");
                if (checkStat){
                    if (bluetoothAdapter == null) {
                        shortToast("该设备不支持蓝牙");
                    } else {
                        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(intent, code);
                        if (bluetoothAdapter != null) {
                            tv_bluetooth_name.setText(bluetoothAdapter.getName());
                            tv_bluetooth_address.setText(bluetoothAdapter.getAddress());
                            bluetoothAdapter.startDiscovery();
                        }
                    }
                }else{
                    ws_bttop.setChecked(false);
                }
            }
        });


//        btn_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (bluetoothAdapter != null || bluetoothAdapter.isEnabled()) {
//                    tv_devices.setText("");
//                    bluetoothAdapter.disable();
//                }
//            }
//        });

//        btn_search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (bluetoothAdapter != null) {
//                    bluetoothAdapter.startDiscovery();
//                }
//            }
//        });
//
//        btn_my.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (bluetoothAdapter != null) {
//                    tv_devices.setText("我的蓝牙设备名称：" + bluetoothAdapter.getName() + "\n" + "我的蓝牙设备地址：" + bluetoothAdapter.getAddress());
//                }
//            }
//        });

        btn_matchfirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    try {
                        autoBound(device.getClass(), device, "1");
                        creatBound(device.getClass(), device);
                        connectedDevice = device;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    connectedDevice = device;
                }
            }
        });

        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            UUID uuid = UUID.fromString(UUID_CONTENT);
                            BluetoothSocket socket = connectedDevice.createRfcommSocketToServiceRecord(uuid);
                            socket.connect();
                            ;
                            OutputStream outputStream = socket.getOutputStream();
                            if (outputStream != null) {
                                shortToast("连接成功");
                                outputStream.write("hello, this is bluetooth".getBytes());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    //match
    static public boolean creatBound(Class<? extends BluetoothDevice> aClass, BluetoothDevice device) throws Exception {
        Method creatBoundMethod = aClass.getMethod("createBond");
        Boolean retuenValue = (boolean) creatBoundMethod.invoke(device);
        return retuenValue.booleanValue();
    }

    //auto match pin
    static public boolean autoBound(Class<? extends BluetoothDevice> aClass, BluetoothDevice device, String s) throws Exception {
        Method autoBoundMethod = aClass.getMethod("setPin", new Class[]{byte[].class});
        Boolean result = (Boolean) autoBoundMethod.invoke(device, new Object[]{s.getBytes()});
        return result;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (address == null) {
                    address = device.getAddress();
                }
                tv_devices.setText(tv_devices.getText() + "\n" + device.getName() + "-->" + device.getAddress());
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
