package com.example.taskplannerproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class LoginSignup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);
        Button login = findViewById(R.id.loginSignup_login);
        Button signup = findViewById(R.id.loginSignup_signup);

        login.setOnClickListener(v -> startActivity(new Intent(LoginSignup.this, Login.class)));
        signup.setOnClickListener(v -> startActivity(new Intent(LoginSignup.this, Register.class)));
    }
}