package com.example.asus.indoornavigation;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

/**
 * fingerPrint.class
 * description: 用于存储每一个点的指纹信息
 * Created by Zhaoguo Wang on 2017/7/18.
 */

public class fingerPrint extends DataSupport{

    fingerPrint(int x,int y,ArrayList<iBeacon> list){
        xCoordinate=x;
        yCoordinate=y;
        iBeaconList=list;
    }
    fingerPrint(){

    }

    public int xCoordinate;

    public int yCoordinate;

    //接收到的ibeacon集合
    public ArrayList<iBeacon> iBeaconList;

    public int getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public ArrayList<iBeacon> getiBeaconList() {
        return iBeaconList;
    }

    public void setiBeaconList(ArrayList<iBeacon> iBeaconList) {
        this.iBeaconList = iBeaconList;
    }

}
