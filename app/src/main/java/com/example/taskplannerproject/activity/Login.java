package com.example.taskplannerproject.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskplannerproject.R;
import com.google.firebase.auth.FirebaseAuth;


import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;

public class Login extends AppCompatActivity {
    
    private EditText email, password;
    private FirebaseAuth mAuth;


    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);

        Button submission = (findViewById(R.id.login_submission));
        TextView forgot = findViewById(R.id.login_forgotPassword);
        TextView signIn = findViewById(R.id.login_switchToRegister);
        mAuth = FirebaseAuth.getInstance();

        forgot.setOnClickListener(v->{
            //change to forgot page
        });
        signIn.setOnClickListener(v-> startActivity(new Intent(this, Register.class)));
        submission.setOnClickListener(v -> {
          if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
              Toast.makeText(this, "One of the fields is empty, please check", Toast.LENGTH_SHORT).show();
          }
          else if (!VALID_EMAIL_ADDRESS_REGEX.matcher(email.getText().toString()).matches()) {
              Toast.makeText(this, "Email in wrong format", Toast.LENGTH_SHORT).show();
          }
          else {
              mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                      .addOnCompleteListener(this, task -> {
                          if (task.isSuccessful()) {
                              // Sign in success, update UI with the signed-in user's information
                              Log.d(TAG, "signInWithEmail:success");
                              //FirebaseUser user = mAuth.getCurrentUser();
                              startActivity(new Intent(Login.this, HomeScreen.class));
                              Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                              finish();
                          } else {
                              // If sign in fails, display a message to the user.
                              Log.w(TAG, "signInWithEmail:failure", task.getException());
                              Toast.makeText(this, "Authentication failed.",
                                      Toast.LENGTH_SHORT).show();
                          }
                      });

          }
        });
    }
}