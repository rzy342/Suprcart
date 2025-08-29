package com.example.suprcartrsmstores;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class StockListActivity extends AppCompatActivity {
    private ListView stockListView;
    private DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_list);
        dbHelper = new DBHelper(this);
        stockListView = findViewById(R.id.stockListView);

        ArrayList<String> stockList = dbHelper.getAllStockData();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stockList);
        stockListView.setAdapter(adapter);

    }
}