package com.example.asus.indoornavigation;

import android.util.Log;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 2017/7/18.
 */

public class handleDB {

    public static void addData(int x, int y, ArrayList<iBeacon> ibeacon_list) {
        fingerPrint info_of_grid=new fingerPrint(x,y,ibeacon_list);
        if (DataSupport.where("xCoordinate = ? and yCoordinate = ?",
                String.valueOf(x), String.valueOf(y)).find(fingerPrint.class).size() == 0) {
            Log.d("MainActivity", "new data added");
            info_of_grid.save();
        } else {
            info_of_grid.updateAll("xCoordinate = ? and yCoordinate = ?", String.valueOf(x), String.valueOf(y));
            Log.d("MainActivity", "data freshed");
        }
    }}
