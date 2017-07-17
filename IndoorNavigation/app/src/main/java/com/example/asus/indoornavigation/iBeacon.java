package com.example.asus.indoornavigation;

/**
 * classname:iBeacpn.java
 * author: Zhaoguo Wang
 * description: iBeacon类，表示指纹采集到的每个ibeacon信息
 * Created by Zhaoguo Wang on 2017/7/17.
 */

public class iBeacon {
    private String name;
    private int major;
    private int minor;
    private String proximityUuid;
    private String bluetoothAddress;
    private int txPower;
    private int rssi;

    public String getName(){
        return name;
    }

    public int getMajor(){
        return major;
    }

    public int getMinor(){
        return minor;
    }

    public String getUuid(){
        return proximityUuid;
    }

    public String getAddress(){
        return bluetoothAddress;
    }

    public int getTxPower(){
        return txPower;
    }

    public int getRssi(){
        return rssi;
    }

    public void setName(String present_name){
        name=present_name;
    }

    public void setBluetoothAddress(String present_address){
        bluetoothAddress=present_address;
    }

    public void setMajor(int present_pmajor){
        major=present_pmajor;
    }

    public void setMinor(int present_minor){
        minor=present_minor;
    }

    public void setProximityUuid(String present_uuid){
        proximityUuid=present_uuid;
    }

    public void setTxPower(int present_txpower){
        txPower=present_txpower;
    }

    public void setRssi(int present_rssi){
        rssi=present_rssi;
    }
}

