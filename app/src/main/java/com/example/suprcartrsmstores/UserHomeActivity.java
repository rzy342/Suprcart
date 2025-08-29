package com.example.suprcartrsmstores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UserHomeActivity extends AppCompatActivity {
    Button logoutbtn,tobillbtn,viewbillbtn;
    TextView Usertextid;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "myprefs";
    private static final String KEY_NAME = "username";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        Usertextid=findViewById(R.id.UserTextID);
        logoutbtn=findViewById(R.id.UserLogoutbtn);
        tobillbtn=findViewById(R.id.bill_button);
        viewbillbtn=findViewById(R.id.view_recent_bill_button);
        sharedPreferences =getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        String name = sharedPreferences.getString(KEY_NAME,null);
        if(name!=null){
            Usertextid.setText("name:"+name);
        }
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(UserHomeActivity.this, UserLoginActivity.class));
                finish();
                Toast.makeText(UserHomeActivity.this, "Logout Successfull", Toast.LENGTH_SHORT).show();

            }
        });
        tobillbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserHomeActivity.this, BillingActivity.class));
            }
        });
        viewbillbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewoldbills();
            }
        });
    }
    private void viewoldbills(){
        startActivity(new Intent(UserHomeActivity.this, ViewOldBillActivity.class));
    }

}