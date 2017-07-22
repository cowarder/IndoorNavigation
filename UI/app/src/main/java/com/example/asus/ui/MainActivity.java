package com.example.asus.ui;


/*
 *author Zhaoguo Wang
 * 用户登录界面，实现账户密码的验证与注册，打开蓝牙
 *created on 7/22/2017
 */
import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION=100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 使用此检查来确定设备是否支持BLE。 然后，您可以选择性地禁用BLE相关功能。
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "Ble not supported", Toast.LENGTH_SHORT).show();
            finish();
        }

        //android 6.0及以上动态权限申请
        //判断是否有权限
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    //请求权限
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                    //判断是否需要,向用户解释，为什么要申请该权限
                    if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_CONTACTS)) {
                        Toast.makeText(MainActivity.this, "shouldShowRequestPermissionRationale", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }).start();

        Button startUser=(Button)findViewById(R.id.start_user);
        startUser.setOnClickListener(new View.OnClickListener() {
                /**
                 * Called when a view has been clicked.
                 *
                 * @param v The view that was clicked.
                 */
                @Override
                public void onClick(View v) {
                    EditText amountEdit=(EditText)findViewById(R.id.account);
                    EditText passwordEdit=(EditText)findViewById(R.id.password);
                    String amount=amountEdit.getText().toString();
                    String password=passwordEdit.getText().toString();
                    Log.d("MainActivity",amount);
                    Log.d("MainActivity",password);
                    if(amount.equals("123")&&password.equals("456")) {
                        Intent startUserActivity = new Intent(MainActivity.this, UserInterface.class);
                        startActivity(startUserActivity);
                        finish();           //调用用户界面之后销毁
                    }
                    else {
                        Toast.makeText(MainActivity.this, "用户名不存在", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
}
