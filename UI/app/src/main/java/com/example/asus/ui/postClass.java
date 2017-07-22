package com.example.asus.ui;

import android.util.Log;

import java.util.List;
import java.util.StringTokenizer;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * created b Zhaoguo Wang
 * 将获取到的ibeacon信息发送到服务器
 * Created by ASUS on 2017/7/22.
 */
//
//private String name;
//private int major;
//private int minor;
//private String proximityUuid;
//private String bluetoothAddress;
//private int txPower;
//private int rssi;

public class postClass {

    private List<iBeacon> ibeacons;

    postClass(List<iBeacon> ibeacons){
        for(iBeacon item:ibeacons){
            Log.d("UserInterface",item.getUuid());
            this.ibeacons=ibeacons;
        }
    }

    /*
    发送数据到服务器后台
     */
    public void postToServer(){
        for(iBeacon item:ibeacons){
            try{
                Log.d("UserInterface","successfully post");
                //建立一个用户对象
                OkHttpClient client=new OkHttpClient();
                //建立RequestBody来存放发送的数据
                RequestBody requestBody=new FormBody.Builder()
                        .add("name","ftft")
                        .add("major",String.valueOf(item.getMajor()))
                        .add("minor",String.valueOf(item.getMinor()))
                        .add("uuid",item.getUuid())
                        .add("macaddress",item.getAddress())
                        .add("txpower",String.valueOf(item.getTxPower()))
                        .add("rssi", String.valueOf(item.getRssi()))
                        .build();
                //如果想要添加额外的数据发送到服务器，就要采用post的方法，而不能使用get
                //构建request对象
                Request request=new Request.Builder()
                        .url("http://192.168.1.108:80/post/addPlace")
                        .post(requestBody)
                        .build();
                Log.d("UserInterface",item.getUuid());
                //获取服务器返回数据
                Response response=client.newCall(request).execute();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
