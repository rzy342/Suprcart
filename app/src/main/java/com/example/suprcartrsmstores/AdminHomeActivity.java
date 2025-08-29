package com.example.suprcartrsmstores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminHomeActivity extends AppCompatActivity {
    Button n1,n2,n3,n4,n5,n6,n7;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "myprefs1";
    private static final String KEY_NAME = "username";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        n1=findViewById(R.id.addpoductbtn);
        n3=findViewById(R.id.addadminbtn);
        n4=findViewById(R.id.adminhomelogoutbtn);
        n5=findViewById(R.id.adminstock);
        n6=findViewById(R.id.Userlist);
        n7=findViewById(R.id.viewpoductbtn);


        sharedPreferences =getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        String name = sharedPreferences.getString(KEY_NAME,null);
        n1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminHomeActivity.this,AddProduct.class);
                startActivity(intent);

            }
        });
        n5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminHomeActivity.this,StockListActivity.class);
                startActivity(intent);

            }
        });
        n4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent intent=new Intent(AdminHomeActivity.this,UserLoginActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(AdminHomeActivity.this, "Logout Successfull", Toast.LENGTH_SHORT).show();

            }
        });
        n7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, ViewProductActivity.class);
                startActivity(intent);

            }
        });
        n3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminHomeActivity.this,AdminRegisterationActivity.class);
                startActivity(intent);

            }
        });
        n6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, userlist.class);
                startActivity(intent);
                finish();
            }
        });
    }
}