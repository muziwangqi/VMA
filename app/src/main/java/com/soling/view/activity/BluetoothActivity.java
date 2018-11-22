package com.soling.view.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.soling.R;

public class BluetoothActivity extends BaseActivity {

    private Button btn_open, btn_close, btn_search, btn_my, btn_matchfirst, btn_connect;
    private TextView tv_devices;
    private BluetoothAdapter bluetoothAdapter;
    private String address = null;
    private BluetoothDevice bluetoothDevice;
    private static int code = 1;


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
        btn_open = findViewById(R.id.btn_open);
        btn_close = findViewById(R.id.btn_close);
        btn_search = findViewById(R.id.btn_search);
        btn_matchfirst = findViewById(R.id.btn_matchfirst);
        btn_my = findViewById(R.id.btn_my);
        btn_connect = findViewById(R.id.btn_connect);
        tv_devices = findViewById(R.id.tv_devices);
    }

    private void initListener() {
        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothAdapter == null) {
                    shortToast("该设备不支持蓝牙");
                } else {
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, code);
                }
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothAdapter != null || bluetoothAdapter.isEnabled()) {
                    bluetoothAdapter.disable();
                }
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothAdapter != null) {
                    bluetoothAdapter.startDiscovery();
                }
            }
        });

        btn_my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothAdapter != null) {
                    tv_devices.setText("我的蓝牙设备名称：" + bluetoothAdapter.getName() + "\n" + "我的蓝牙设备地址：" + bluetoothAdapter.getAddress());
                }
            }
        });

       /* btn_matchfirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothDevice device=bluetoothAdapter.getRemoteDevice(address);
                if (device.getBondState()!=BluetoothDevice.BOND_BONDED){

                }
            }
        });*/
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
