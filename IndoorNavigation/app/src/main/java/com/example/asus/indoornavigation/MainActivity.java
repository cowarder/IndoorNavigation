package com.example.asus.indoornavigation;

/**
 * name: MainActivity.java
 * author: Zhaoguo Wang
 * description: 主活动
 * Created by Zhaoguo Wang on 2017/7/17.
 */

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.os.Handler;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION=100;
    private Handler mHandler;

    private ArrayList<iBeacon> mLeDevices = new ArrayList<iBeacon>();  //用于存储收集到的指纹信息

    private BluetoothAdapter mBluetoothAdapter;

    // Stops scanning after 10 seconds.设置扫描时间间隔
    private static final long SCAN_PERIOD = 60000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //android 6.0及以上动态权限申请
        //判断是否有权限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //请求权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
            //判断是否需要 向用户解释，为什么要申请该权限
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                Toast.makeText(this, "shouldShowRequestPermissionRationale", Toast.LENGTH_SHORT).show();
            }
        }

        Button scaniBeacon=(Button)findViewById(R.id.scan_ibeacon);
        mHandler = new Handler();

        // 使用此检查来确定设备是否支持BLE。 然后，您可以选择性地禁用BLE相关功能。
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "Ble not supported", Toast.LENGTH_SHORT).show();
            finish();
        }

        //初始化蓝牙适配器
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        //检查该设备是否支持蓝牙
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "error:Bluetooth not supported", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        //开启蓝牙
        mBluetoothAdapter.enable();

        scanLeDevice(true);//开始扫描

        Log.d("MainActvity","begin to scan");
        scaniBeacon.setOnClickListener(new View.OnClickListener(){
            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                scanLeDevice(false);
                mLeDevices.clear();
                Log.d("MainActivity","onCreate: Refresh Data!");
                scanLeDevice(true);
            }
        });
    }

    /*
     *扫描设备
     */
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            /* 在预先定义的扫描时间之后停止扫描
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);*/
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        invalidateOptionsMenu();
    }

    /**
     * Device scan callback.
     * 回调
     */
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback()
    {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord)
        {
            final iBeacon ibeacon = iBeaconClass.fromScanData(device,rssi,scanRecord);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("MainActivity", "run: add ibeacon"+mLeDevices.size());
                    if(ibeacon != null&&ibeacon.getMinor()!=0) {
                        /*if(isExist(ibeacon))
                            mLeDevices.remove(ibeacon);*/
                        mLeDevices.remove(IsExist(ibeacon));
                        mLeDevices.add(ibeacon);
                        Log.d("MainActivity", "run: add ibeacon"+mLeDevices.size());
                        Display();
                    }
                }
            });
        }
    };

    /*
     *判断是否在list中已经存在
     * @param ibeacon
     * return boolean
     */
    private boolean isExist(iBeacon ibeacon){
        for(iBeacon iBeacon:mLeDevices){
            if(ibeacon.getAddress()==iBeacon.getAddress()){
                return true;
            }
        }
        return false;
    }

    private iBeacon IsExist(iBeacon ibeacon){
        iBeacon sameibeacon=new iBeacon();
        for(iBeacon item:mLeDevices){
            if (item.getUuid().equals(ibeacon.getUuid())&&
                    item.getMajor()==ibeacon.getMajor()&&item.getMinor()==ibeacon.getMinor()){
                sameibeacon=item;
                Log.d("MainActivity", "IsExist!!!");
                break;
            }
        }
        return sameibeacon;
    }

    //将ibeacon信息在ListView里面显示出来
    public void Display(){
        iBeaconAdapter adapter=new iBeaconAdapter(MainActivity.this,R.layout.ibeacon_item,mLeDevices);
        ListView listview=(ListView)findViewById(R.id.ibeacon_list);
        listview.setAdapter(adapter);
    }

    /*private void addDevice(iBeacon device) {            //更新mLeDevice里面的ibeacon信息
        if(device==null) {
            Log.d("DeviceScanActivity ", "device==null ");
            return;
        }

        for(int i=0;i<mLeDevices.size();i++){
            String btAddress = mLeDevices.get(i).getAddress();
            if(btAddress.equals(device.getAddress())){
                mLeDevices.add(i+1, device);
                mLeDevices.remove(i);
                break;
            }
        }
        mLeDevices.add(device);
    }*/
}
