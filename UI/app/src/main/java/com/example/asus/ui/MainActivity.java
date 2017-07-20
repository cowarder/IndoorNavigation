package com.example.asus.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
