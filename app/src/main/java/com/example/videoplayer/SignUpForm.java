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

public class SignUpForm extends AppCompatActivity {
    private EditText fullNameEditText, usernameEditText, passwordEditText, confirmPasswordEditText;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_form);

        fullNameEditText = findViewById(R.id.fullnameEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);

        signUpButton = findViewById(R.id.signupButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = fullNameEditText.getText().toString().trim();
                String userName = usernameEditText.getText().toString().trim();
                String rawPassword = passwordEditText.getText().toString().trim();
                String rawConfirmPassword = confirmPasswordEditText.getText().toString().trim();
                if (fullName.length() == 0 || userName.length() == 0 || rawPassword.length() == 0 || rawConfirmPassword.length() == 0) {
                    Toast.makeText(SignUpForm.this, "Please provide all details!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!rawPassword.equals(rawConfirmPassword)) {
                    Toast.makeText(SignUpForm.this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT).show();
                    return;
                }
                signUpUser(fullName, userName, rawPassword);
            }
        });
    }

    private void signUpUser(String fullName, String username, String password){
        UserService userService = new UserService(SignUpForm.this);
        long userId = userService.addUser(username, fullName, password);
        if (userId != -1) {
            // Save user details in SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("LoggedInUser", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong("userId", userId);
            editor.putString("username", username);
            editor.putString("fullName", fullName);
            editor.apply();
            Intent intent = new Intent(SignUpForm.this, ManageLinks.class);
            startActivity(intent);
        } else {
            Toast.makeText(SignUpForm.this, "Failed to register user", Toast.LENGTH_SHORT).show();
        }

    }
}