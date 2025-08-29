package com.example.suprcartrsmstores;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class viewallbill extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> billllist;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewallbill);
        dbHelper = new DBHelper(this);
        billllist = dbHelper.getAllBillData();

        listView = findViewById(R.id.listv);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, billllist);
        listView.setAdapter(adapter);
    }
}