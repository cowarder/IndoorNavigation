package com.example.asus.ui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.util.Log;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * 描类，不断地更新扫描到的iBeacon信息
 * 并将其存储在mLeDevices
 * Created by ASUS on 2017/7/22.
 */

public class scanClass {

    /**
     * Device scan callback.
     * 回调
     */
    BluetoothAdapter.LeScanCallback scanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            final iBeacon ibeacon = iBeaconClass.fromScanData(device,rssi,scanRecord);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if(ibeacon != null&&ibeacon.getMinor()!=0) {
                        //如果新扫描到的iBeacon设备在List中已经存在，就将其删除
                        if(isExist(ibeacon)!=-1)
                            mLeDevices.remove(isExist(ibeacon));
                        mLeDevices.add(ibeacon);
                        Log.d("UserInterface","list refreshed");
                        //按照信号强度进行升序排序
                        try {
                            Thread.sleep(100);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        Collections.sort(mLeDevices, new Comparator<iBeacon>() {
                            @Override
                            public int compare(iBeacon o1, iBeacon o2) {
                                return o2.getRssi()-o1.getRssi();
                            }
                        });
                    }
                }
            });
        }
    };

    private  ArrayList<iBeacon> mLeDevices = new ArrayList<iBeacon>();  //用于存储收集到的指纹信息

    private  BluetoothAdapter mBluetoothAdapter;      //蓝牙适配器

    scanClass(BluetoothAdapter bluetoothAdapter){
        mBluetoothAdapter=bluetoothAdapter;
    }




    //开启扫描
    public void startScan(boolean enable){
        /*
     * Device scan callback.
     * 回调
     */
        BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback()
        {
            @Override
            public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord)
            {
                final iBeacon ibeacon = iBeaconClass.fromScanData(device,rssi,scanRecord);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("UserInterface", "num of ibeacons:"+mLeDevices.size());
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
                            Log.d("UserInterface", "list sorted");
                        }
                    }
                });
            }
        };
        Log.d("UserInterface","start scan");
        if(enable)
            mBluetoothAdapter.startLeScan(mLeScanCallback);        //开始扫描
        else
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
    }


    /*
     *判断是否在list中是否已经存在
     * @param ibeacon
     * return iBeacon
    */
    public int isExist(iBeacon ibeacon){
        for(int i=0;i!=mLeDevices.size();i++){
            if(mLeDevices.get(i).getUuid().equals(ibeacon.getUuid())&&
                    mLeDevices.get(i).getMajor()==ibeacon.getMajor()&&mLeDevices.get(i).getMinor()==ibeacon.getMinor())
                return i;
        }
        return -1;
    }


}



