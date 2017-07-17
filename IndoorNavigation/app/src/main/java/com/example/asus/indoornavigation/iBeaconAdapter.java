package com.example.asus.indoornavigation;

/**
 * classname:iBeacpnAdapter.java
 * author: Zhaoguo Wang
 * description: 自定义的适配器，使iBeacon各项信息显示在ListView空间之中
 * Created by Zhaoguo Wang on 2017/7/17.
 */
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class iBeaconAdapter extends ArrayAdapter<iBeacon> {

    private int resourceId;

    public iBeaconAdapter(Context context,int textViewResourceId,List<iBeacon> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;          //获取索引值
    }

    @Override
    public View getView(int position,View convertView, ViewGroup parent) {
        iBeacon ibeacon=getItem(position);          //获取当前项的iBeacon实例
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

        TextView ibeaconName=(TextView)view.findViewById(R.id.ibeacon_name);
        TextView ibeaconAddress=(TextView)view.findViewById(R.id.ibeacon_address);
        TextView ibeaconUuid=(TextView)view.findViewById(R.id.ibeacon_uuid);
        TextView ibeaconMajor=(TextView)view.findViewById(R.id.ibeacon_major);
        TextView ibeaconMinor=(TextView)view.findViewById(R.id.ibeacon_minor);
        TextView ibeaconTxPower=(TextView)view.findViewById(R.id.ibeacon_txpower);
        TextView ibeaconRssi=(TextView)view.findViewById(R.id.ibeacon_rssi);

        ibeaconName.setText("Name"+ibeacon.getName());
        ibeaconAddress.setText("Address"+ibeacon.getAddress());
        ibeaconUuid.setText("UUID"+ibeacon.getUuid());
        ibeaconMajor.setText("major"+ibeacon.getMajor());
        ibeaconMinor.setText("minor"+ibeacon.getMinor());
        ibeaconTxPower.setText("txpower"+ibeacon.getTxPower());
        ibeaconRssi.setText("rssi"+ibeacon.getRssi());

        return view;
    }
}
