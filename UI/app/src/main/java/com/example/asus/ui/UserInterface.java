package com.example.asus.ui;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserInterface extends AppCompatActivity {

    private final String Tag="UserInterface";

    private DrawerLayout drawerLayout;

    private ArrayList<iBeacon> mLeDevices = new ArrayList<iBeacon>();  //用于存储收集到的指纹信息

    private   BluetoothAdapter mBluetoothAdapter;      //蓝牙适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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


        setContentView(R.layout.activity_user_interface);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBar actionBar=getSupportActionBar();

        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_list_black_24dp);//设置导航按钮的图标
        }
        NavigationView navigationView=(NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.
                OnNavigationItemSelectedListener(){
            /**
             * Called when an item in the navigation menu is selected.
             *
             * @param item The selected item
             * @return true to display the item as the selected item
             */
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {   //滑动菜单的点击事件
                switch (item.getItemId()){
                    case R.id.business:
                        break;
                    case R.id.location:
                        break;
                    case R.id.setting:
                        break;
                    case R.id.heip:
                        break;
                    case R.id.feedback:
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        //开始扫描附近的ibeacon设备
        final Button startScan=(Button)findViewById(R.id.start_scan);
        startScan.setOnClickListener(new View.OnClickListener(){
            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                Log.d("UserInterface","start scan");
               scanLeDevice(true);
            }
        });

    }

    /*
    @para bool
    @通过蓝牙适配器开始扫描
     */
    private void scanLeDevice(final boolean enable){
        if(enable)
            if(mLeScanCallback!=null)
                mBluetoothAdapter.startLeScan(mLeScanCallback);        //开始扫描
        else
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
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
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d(Tag, "num of ibeacons:"+mLeDevices.size());
                    if(ibeacon != null&&ibeacon.getMinor()!=0) {
                        if(isExist(ibeacon)!=-1)
                            mLeDevices.remove(isExist(ibeacon));
                        mLeDevices.add(ibeacon);
                        try{
                            Thread.sleep(3000);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        //按照信号强度进行排序
                        Collections.sort(mLeDevices, new Comparator<iBeacon>() {
                            @Override
                            public int compare(iBeacon o1, iBeacon o2) {
                                return o2.getRssi()-o1.getRssi();
                            }
                        });
                        postClass p=new postClass(mLeDevices);
                        p.postToServer();
                        Log.d(Tag, "list sorted");
                    }
                }
            }).start();
        }
    };

    /*
    @para menu
    @return boolean
    @descriptiom 加载菜单栏
     */
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    /*
    @para item
    @return boolean
    @description 菜单栏的点击事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.open_camera:

                break;
            case R.id.get_info:
                break;
            default:
                break;
        }
        return true;
    }

    /*
     *判断是否在list中是否已经存在
     * @param ibeacon
     * @return iBeacon
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
