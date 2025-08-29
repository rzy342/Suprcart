package com.example.suprcartrsmstores;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminRegisterationActivity extends AppCompatActivity {
    private EditText usernameEditText3, passwordEditText3,emailEditText3, phonenoEditText3;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_registeration);
        dbHelper = new DBHelper(this);
        usernameEditText3 = findViewById(R.id.usernameEditText3);
        passwordEditText3 = findViewById(R.id.passwordEditText3);
        emailEditText3 = findViewById(R.id.emailEditText3);
        phonenoEditText3 = findViewById(R.id.phonenoEditText3);
        Button registerButton = findViewById(R.id.registerButton3);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText3.getText().toString();
                String password = passwordEditText3.getText().toString();
                String email = emailEditText3.getText().toString();
                String phoneno = phonenoEditText3.getText().toString();

                if (validateInput(username, password,email,phoneno)) {
                    if (dbHelper.insertAdmin(username, password, email, phoneno)) {
                        Toast.makeText(AdminRegisterationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                        finish(); // Close the registration activity
                    } else {
                        Toast.makeText(AdminRegisterationActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean validateInput(String username, String password,String email, String phoneno) {
        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || phoneno.isEmpty()) {
            Toast.makeText(this, "Please enter all credentials", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}