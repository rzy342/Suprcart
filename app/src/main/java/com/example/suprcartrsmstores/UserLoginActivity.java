package com.example.suprcartrsmstores;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UserLoginActivity extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "myprefs";
    private static final String KEY_NAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        String name = sharedPreferences.getString(KEY_NAME,null);
        if(name != null){
            startActivity(new Intent(UserLoginActivity.this, UserHomeActivity.class));
            finish();
        }

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                // Perform user login authentication
                if (isValidUser(username, password)) {
                    // Save user login state
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(KEY_NAME,usernameEditText.getText().toString());
                    editor.apply();
                    // Navigate to user home activity
                    startActivity(new Intent(UserLoginActivity.this, UserHomeActivity.class));
                    finish();
                    Toast.makeText(UserLoginActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(UserLoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView registerTextView = findViewById(R.id.registerTextView);
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserLoginActivity.this, UserRegisterationActicity.class));
            }
        });
        TextView registerloginTextView = findViewById(R.id.registerloginTextView);
        registerloginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserLoginActivity.this, AdminLoginActivity.class));
            }
        });
    }

    private boolean isValidUser(String username, String password) {
        DBHelper dbHelper = new DBHelper(this);
        boolean isValid = dbHelper.isValidUser(username, password);
        dbHelper.close();
        return isValid;
    }



}