package com.example.suprcartrsmstores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AdminLoginActivity extends AppCompatActivity {
    private EditText usernameEditText2, passwordEditText2;
    private DBHelper dbHelper;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "myprefs1";
    private static final String KEY_NAME1 = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        usernameEditText2 = findViewById(R.id.usernameEditText2);
        passwordEditText2 = findViewById(R.id.passwordEditText2);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        String name = sharedPreferences.getString(KEY_NAME1,null);
        if(name != null){
            startActivity(new Intent(AdminLoginActivity.this, AdminHomeActivity.class));
            finish();
        }
        Button loginButton2 = findViewById(R.id.loginButton2);
        loginButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText2.getText().toString();
                String password = passwordEditText2.getText().toString();
                // Perform user login authentication
                if (isValidUser(username, password)) {
                    // Save user login state
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(KEY_NAME1,usernameEditText2.getText().toString());
                    editor.apply();
                    // Navigate to user home activity
                    startActivity(new Intent(AdminLoginActivity.this, AdminHomeActivity.class));
                    finish();
                    Toast.makeText(AdminLoginActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(AdminLoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private boolean isValidUser(String username, String password) {
        DBHelper dbHelper = new DBHelper(this);
        boolean isValid = dbHelper.isAdmin(username, password);
        dbHelper.close();
        return isValid;
    }
}