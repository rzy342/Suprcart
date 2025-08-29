package com.example.suprcartrsmstores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ViewOldBillActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> billList;
    private DBHelper dbHelper;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "myprefs";
    private static final String KEY_NAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_old_bill);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        String loggedInUsername = sharedPreferences.getString(KEY_NAME,null);
        dbHelper = new DBHelper(this);
        billList = dbHelper.getAllbBillData(loggedInUsername);

        listView = findViewById(R.id.listViewProducts11);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, billList);
        listView.setAdapter(adapter);


    }
}