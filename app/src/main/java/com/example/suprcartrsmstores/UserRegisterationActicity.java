package com.example.suprcartrsmstores;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UserRegisterationActicity extends AppCompatActivity {
    private EditText usernameEditText1, passwordEditText1,emailEditText1, phonenoEditText1;
    private DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registeration_acticity);
        dbHelper = new DBHelper(this);
        usernameEditText1 = findViewById(R.id.usernameEditText1);
        passwordEditText1 = findViewById(R.id.passwordEditText1);
        emailEditText1 = findViewById(R.id.emailEditText1);
        phonenoEditText1 = findViewById(R.id.phonenoEditText1);

        Button registerButton = findViewById(R.id.registerButton1);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText1.getText().toString();
                String password = passwordEditText1.getText().toString();
                String email = emailEditText1.getText().toString();
                String phoneno = phonenoEditText1.getText().toString();

                if (validateInput(username, password,email,phoneno)) {
                    if (dbHelper.insertUser(username, password, email, phoneno)) {
                        Toast.makeText(UserRegisterationActicity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                        finish(); // Close the registration activity
                    } else {
                        Toast.makeText(UserRegisterationActicity.this, "Registration failed", Toast.LENGTH_SHORT).show();
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