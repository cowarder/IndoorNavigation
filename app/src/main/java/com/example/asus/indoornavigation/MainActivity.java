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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.os.Handler;
import android.widget.Toast;

import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    private static final String Tag="MainActivity";

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION=100;

    private Handler mHandler;

    public  static ArrayList<iBeacon> mLeDevices = new ArrayList<iBeacon>();  //用于存储收集到的指纹信息

    private static BluetoothAdapter mBluetoothAdapter;

    // Stops scanning after 10 seconds.设置扫描时间间隔
    private static final long SCAN_PERIOD = 60000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //创建数据库
        Connector.getDatabase();

        //android 6.0及以上动态权限申请
        //判断是否有权限
        Thread thread = new Thread(){
            public void run(){
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    //请求权限
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                    //判断是否需要 向用户解释，为什么要申请该权限
                    if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_CONTACTS)) {
                        Toast.makeText(MainActivity.this, "shouldShowRequestPermissionRationale", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        };

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

        Log.d(Tag,"begin to scan");
        //按钮scan_button的点击事件
        Button scaniBeacon=(Button)findViewById(R.id.scan_ibeacon);
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
                Log.d(Tag,"onCreate: Refresh Data!");
                scanLeDevice(true);
            }
        });

        final EditText xCoordinate=(EditText)findViewById(R.id.x_coordinate);
        final EditText yCoordinate=(EditText)findViewById(R.id.y_coordinate);

        //按钮add_finger_print的点击事件
        Button addData=(Button)findViewById(R.id.add_fingerprint);
        addData.setOnClickListener(new View.OnClickListener(){
            /**
             * Called when a view has been clicked.
             * description: 向数据库中添加指纹
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                Log.d(Tag,"add data to database");
                if(xCoordinate.getText().toString().isEmpty()||yCoordinate.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Input location coordinate", Toast.LENGTH_SHORT).show();
                    return;
                }
                handleDB.addData(Integer.parseInt(xCoordinate.getText().toString()),
                        Integer.parseInt(yCoordinate.getText().toString()),mLeDevices);

            }
        });
    }

    public static void main(String args[]){


    }

    /*
     *扫描设备
     */
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            //在预先定义的扫描时间之后停止扫描
            /*mHandler.postDelayed(new Runnable() {
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
                    Log.d(Tag, "num of ibeacons:"+mLeDevices.size());
                    if(ibeacon != null&&ibeacon.getMinor()!=0) {
                        if(isExist(ibeacon)!=-1)
                            mLeDevices.remove(isExist(ibeacon));
                        mLeDevices.add(ibeacon);
                        //按照信号强度进行排序
                        Collections.sort(mLeDevices, new Comparator<iBeacon>() {
                            @Override
                            public int compare(iBeacon o1, iBeacon o2) {
                                return o2.getRssi()-o1.getRssi();
                            }
                        });
                        Log.d(Tag, "list sorted");
                        Display();
                    }
                }
            });
        }
    };

    /*
     *判断是否在list中已经存在
     * @param ibeacon
     * return iBeacon
     */
    private int isExist(iBeacon ibeacon){
        for(int i=0;i!=mLeDevices.size();i++){
            if(mLeDevices.get(i).getUuid().equals(ibeacon.getUuid())&&
                    mLeDevices.get(i).getMajor()==ibeacon.getMajor()&&mLeDevices.get(i).getMinor()==ibeacon.getMinor())
                return i;
        }
        return -1;
    }
    //将ibeacon信息在ListView里面显示出来
    public void Display(){
        iBeaconAdapter adapter=new iBeaconAdapter(MainActivity.this,R.layout.ibeacon_item,mLeDevices);
        ListView listview=(ListView)findViewById(R.id.ibeacon_list);
        listview.setAdapter(adapter);
        Log.d(Tag,"fresh list");
    }


}
