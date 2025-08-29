package com.example.suprcartrsmstores;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


public class BillingActivity extends AppCompatActivity {
    private TableLayout tableLayout;
    private double totalAmount = 0.0;
    private EditText productNameEditText;
    private EditText limitsetet;
    private EditText productidEditText;
    private EditText priceEditText;
    private EditText quantityEditText;
    private EditText expiryDateEditText;
    private EditText mfdDateEditText;
    private TextView totalTextView;
    SQLiteDatabase database;
    DBHelper databaseHelper = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);
        tableLayout = findViewById(R.id.table_layout);
        productNameEditText = findViewById(R.id.product_name_edit_text);
        productidEditText = findViewById(R.id.productid_et);
        priceEditText = findViewById(R.id.price_edit_text);
        quantityEditText = findViewById(R.id.quantity_edit_text);
        mfdDateEditText = findViewById(R.id.mfd_edit_text);
        limitsetet=findViewById(R.id.limitet);
        expiryDateEditText = findViewById(R.id.expiry_edit_text);
        Button scanButton = findViewById(R.id.scan_button);
        totalTextView = findViewById(R.id.totalTextView);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initiate QR code scan
                IntentIntegrator integrator = new IntentIntegrator(BillingActivity.this);
                integrator.setOrientationLocked(false);
                integrator.initiateScan();
            }
        });

        Button addItemButton = findViewById(R.id.add_item_button);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add item to the bill table
                String prodctid = productidEditText.getText().toString();
                String productName = productNameEditText.getText().toString();
                double price = Double.parseDouble(priceEditText.getText().toString());
                String prr = String.valueOf(price);
                int quantity = Integer.parseInt(quantityEditText.getText().toString());
                String qrr = String.valueOf(quantity);
                double subtotal = price * quantity;
                String srr = String.valueOf(subtotal);
                addItemToTable(productName, prodctid, price, quantity, subtotal);
                }
        });

        Button paymentButton = findViewById(R.id.payment_button);
        paymentButton.setOnClickListener(v -> {
                makePayment();
                Toast.makeText(BillingActivity.this, "Payment successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(BillingActivity.this, UserHomeActivity.class));
                finish();
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                // QR code scan result
                String[] scanData = result.getContents().split(",");
                if (scanData.length ==7) {
                    // Update EditText fields with scan data
                    productidEditText.setText(scanData[0]);
                    productNameEditText.setText(scanData[1]);
                    priceEditText.setText(scanData[3]);
                    quantityEditText.setText(scanData[4]);
                    mfdDateEditText.setText(scanData[5]);
                    expiryDateEditText.setText(scanData[6]);
                } else if(scanData.length > 7 || scanData.length < 7) {
                    Toast.makeText(this, "Invalid QR code format", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void addItemToTable(String productName, String productid, double price, int quantity, double subtotal) {
        // Check if the product exists in the stock table
        Double totallimit=Double.POSITIVE_INFINITY;

        if(!TextUtils.isEmpty(limitsetet.getText().toString())){
            totallimit=Double.parseDouble(limitsetet.getText().toString());
        }
        if(totalAmount + subtotal >totallimit){
            Toast.makeText(this, "Limit Crossed" + totallimit, Toast.LENGTH_SHORT).show();
            return;
        }
        int currentStock = databaseHelper.getStockQuantity(productid);
        if (currentStock < quantity) {
            Toast.makeText(this, "Insufficient stock for " + productName, Toast.LENGTH_SHORT).show();
            return;
        }

        // Reduce the quantity from the stock table
        databaseHelper.updateStock(productid, -quantity);

        // Add the item to the bill table
        TableRow row = new TableRow(this);
        TextView productidTextView = new TextView(this);
        productidTextView.setText(productid);
        productidTextView.setPadding(16, 0, 16, 0);
        row.addView(productidTextView);
        TextView productNameTextView = new TextView(this);
        productNameTextView.setText(productName);
        productNameTextView.setPadding(16, 0, 16, 0);
        row.addView(productNameTextView);
        TextView priceTextView = new TextView(this);
        priceTextView.setText(String.valueOf(price));
        row.addView(priceTextView);
        TextView quantityTextView = new TextView(this);
        quantityTextView.setText(String.valueOf(quantity));
        row.addView(quantityTextView);
        TextView subtotalTextView = new TextView(this);
        subtotalTextView.setText(String.valueOf(subtotal));
        row.addView(subtotalTextView);
        Button deleteButton = new Button(this);
        deleteButton.setText("Delete");
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete row from the table and add quantity back to stock
                tableLayout.removeView(row);
                totalAmount -= subtotal;
                totalTextView.setText(String.format(Locale.getDefault(), "Total: %.2f", totalAmount));
                databaseHelper.updateStock(productid, quantity);
            }
        });
        row.addView(deleteButton);
        tableLayout.addView(row);
        totalAmount += subtotal;

        totalTextView.setText(String.format(Locale.getDefault(), "Total: %.2f", totalAmount));
    }


    private void makePayment() {
        String bill_id = UUID.randomUUID().toString();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Retrieve username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        // Insert bill details into the bills table
        databaseHelper.insertBill(bill_id, currentDate, totalAmount, username);
        for (int i = 1; i < tableLayout.getChildCount(); i++) {
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            TextView productidTextView = (TextView) row.getChildAt(0);
            TextView productNameTextView = (TextView) row.getChildAt(1);
            TextView priceTextView = (TextView) row.getChildAt(2);
            TextView quantityTextView = (TextView) row.getChildAt(3);
            TextView subtotalTextView = (TextView) row.getChildAt(4);
            String productid = productidTextView.getText().toString();
            String productName = productNameTextView.getText().toString();
            double price = parseDouble(priceTextView.getText().toString());
            int quantity = parseInt(quantityTextView.getText().toString());
            double subtotal = parseDouble(subtotalTextView.getText().toString());

            // Insert purchase details into the purchase table

            databaseHelper.insertPurchase(bill_id, productName, price, quantity, subtotal);

        }

        // Clear the tableLayout and reset total after payment
        tableLayout.removeAllViews();
        totalAmount = 0.0;
        totalTextView.setText(String.format(Locale.getDefault(), "Total: %.2f", totalAmount));
    }
}