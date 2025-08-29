package com.example.suprcartrsmstores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class userlist extends AppCompatActivity {
    private DBHelper dbHelper;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlist);
        b1=findViewById(R.id.view_all_data);
        dbHelper = new DBHelper(this);
        ListView userlist=findViewById(R.id.userlistview);
        ArrayList<String> usertotallist =dbHelper.getUserTotalAmounts();
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,usertotallist);
        userlist.setAdapter(adapter);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(userlist.this,viewallbill.class);
                startActivity(intent);
            }
        });

    }
}