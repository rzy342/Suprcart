package com.example.suprcartrsmstores;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class AddProduct extends AppCompatActivity {
    private EditText proidEditText, pronameEditText, probrandEditText, propriceEditText, proquanEditText, promfdEditText, proexpryEditText;
    private Button scanButton, addButton, cancelButton;
    private DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        dbHelper = new DBHelper(this);

        proidEditText = findViewById(R.id.proidEditText);
        pronameEditText = findViewById(R.id.pronameEditText);
        probrandEditText = findViewById(R.id.probrandEditText);
        propriceEditText = findViewById(R.id.propriceEditText);
        proquanEditText = findViewById(R.id.proquanEditText);
        promfdEditText = findViewById(R.id.promfdEditText);
        proexpryEditText = findViewById(R.id.proexpryEditText);
        scanButton = findViewById(R.id.scanButton);
        addButton = findViewById(R.id.addButton);
        cancelButton = findViewById(R.id.CANCELButton);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(AddProduct.this);
                integrator.setOrientationLocked(false); // Allow orientation change
                integrator.setPrompt("Scan a QR Code");
                integrator.initiateScan();
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String proid = proidEditText.getText().toString();
                String proname = pronameEditText.getText().toString();
                String probrand = probrandEditText.getText().toString();
                String propricestr = propriceEditText.getText().toString();
                String proquanstr = proquanEditText.getText().toString();
                String promfd = promfdEditText.getText().toString();
                String proexpry = proexpryEditText.getText().toString();

                if (proid.isEmpty() || proname.isEmpty() || probrand.isEmpty() ||
                        propricestr.isEmpty() || proquanstr.isEmpty() || promfd.isEmpty() ||
                        proexpry.isEmpty()) {
                    Toast.makeText(AddProduct.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                double proprice = Double.parseDouble(propricestr);
                int proquan = Integer.parseInt(proquanstr);



                if (dbHelper.isProductExists(proid)) {
                    // Product already exists, update the quantity in the stock table
                    int existingQuantity = dbHelper.getStockQuantity(proid);
                    int newQuantity = existingQuantity + proquan;
                    dbHelper.updateStockQuantity(proid, newQuantity);
                } else {
                    // Product does not exist, insert new product into product table and stock table
                    dbHelper.addProduct(proid, proname, probrand, proprice, proquan, promfd, proexpry);
                }

                Toast.makeText(AddProduct.this, "Product added successfully", Toast.LENGTH_SHORT).show();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddProduct.this, AdminHomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String[] scanData1 = result.getContents().split(",");
                if (scanData1.length == 7) {
                    String[] parts = result.getContents().split(",");
                    proidEditText.setText(parts[0]);
                    pronameEditText.setText(parts[1]);
                    probrandEditText.setText(parts[2]);
                    propriceEditText.setText(parts[3]);
                    proquanEditText.setText(parts[4]);
                    promfdEditText.setText(parts[5]);
                    proexpryEditText.setText(parts[6]);
                } else if (scanData1.length > 7 || scanData1.length < 7) {
                    Toast.makeText(this, "Invalid QR code format", Toast.LENGTH_SHORT).show();
                }
            }

        }

    }
}