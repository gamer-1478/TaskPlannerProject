package com.example.taskplannerproject.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskplannerproject.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;

public class Register extends AppCompatActivity {

    //component declarations
    public EditText name, email, password, confirmPassword;
    public TextView switchPage;
    public Button registerSubmission;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private void registerUser(String m_name, String m_email, String m_password) {
        // do the actual registration using firebase.
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(m_email, m_password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser f_user = mAuth.getCurrentUser();

                        Map<String, Object> user = new HashMap<>();
                        user.put("name", m_name);
                        user.put("email", m_email);
                        user.put("date_created", new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
                        user.put("time_created", new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date()));

                        // Add a new document with a generated ID
                        db.collection("users").document(Objects.requireNonNull(f_user).getUid())
                                .set(user)
                                .addOnSuccessListener(aVoid -> {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                    progressBar.setVisibility(View.GONE);
                                    startActivity(new Intent(Register.this, HomeScreen.class));
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Log.w(TAG, "Error writing document", e);
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(Register.this, "Authentication failed. Please check network and try again.",Toast.LENGTH_SHORT).show();
                                });

                        Log.d(TAG, "createUserWithEmail:success");

                    } else {
                        // If sign in fails, display a message to the user.
                        progressBar.setVisibility(View.GONE);
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(Register.this, "Authentication failed. Please check network and try again.",Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        FirebaseApp.initializeApp(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.register_progressBar);
        //component initializations
        name = findViewById(R.id.register_name);
        email = findViewById(R.id.register_email);
        password = findViewById(R.id.register_password);
        confirmPassword = findViewById(R.id.register_confirmPassword);
        switchPage = findViewById(R.id.register_switchPageToLogin);
        registerSubmission = findViewById(R.id.register_submission);
        db = FirebaseFirestore.getInstance();
        switchPage.setOnClickListener(v -> startActivity(new Intent(Register.this, Login.class)));

        registerSubmission.setOnClickListener(v -> {
            String s_name = name.getText().toString();
            String s_email = email.getText().toString();
            String s_password = password.getText().toString();
            String s_confirmPassword = confirmPassword.getText().toString();
            if (s_name.isEmpty() || s_email.isEmpty() || s_password.isEmpty() || s_confirmPassword.isEmpty()) {
                Toast.makeText(Register.this, "One or more fields are empty", Toast.LENGTH_SHORT).show();
            } else if (!s_password.equals(s_confirmPassword)) {
                Toast.makeText(Register.this, "Passwords do not match, please Check passwords", Toast.LENGTH_SHORT).show();
            } else if (password.length() < 6) {
                Toast.makeText(Register.this, "Password cannot be less than 6 digits", Toast.LENGTH_SHORT).show();
            } else if (!VALID_EMAIL_ADDRESS_REGEX.matcher(s_email).matches()) {
                Toast.makeText(Register.this, "Email in wrong format", Toast.LENGTH_SHORT).show();
            } else {
                registerUser(s_name, s_email, s_password);
            }
        });
    }
}