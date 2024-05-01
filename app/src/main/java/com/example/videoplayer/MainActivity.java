package com.example.videoplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button signupButton, loginButton;
    private EditText userNameEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userNameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signupButton = findViewById(R.id.signupButton);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = userNameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                if (userName.length() == 0 || password.length() == 0) {
                    Toast.makeText(MainActivity.this, "Please provide all details!", Toast.LENGTH_SHORT).show();
                    return;
                }
                loginUser(userName, password);
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpForm.class);
                startActivity(intent);
            }
        });
    }
    private void loginUser(String userName, String password) {
        UserService userService = new UserService(MainActivity.this);
        User loggedInUser = userService.loginUser(userName, password);
        if (loggedInUser.getUserId() == 0) {
            Toast.makeText(MainActivity.this, "Wrong username or password!", Toast.LENGTH_SHORT).show();
            return;
        }
        // Save user details in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LoggedInUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("userId", loggedInUser.getUserId());
        editor.putString("username", loggedInUser.getUsername());
        editor.putString("fullName", loggedInUser.getFullname());
        editor.apply();
        Intent intent = new Intent(MainActivity.this, ManageLinks.class);
        startActivity(intent);
    }
}