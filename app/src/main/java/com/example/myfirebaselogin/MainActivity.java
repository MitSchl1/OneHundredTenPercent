package com.example.myfirebaselogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailEditText, passwordEditText;
    private FirebaseAuth mAuth;
    private ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView registerTextView = (TextView) findViewById(R.id.registeruser_main);
        registerTextView.setOnClickListener(this);

        TextView forgotPasswordTextView = (TextView) findViewById(R.id.forgotPassword_main);
        forgotPasswordTextView.setOnClickListener(this);
        Button signInButton = (Button) findViewById(R.id.loginbutton_main);
        signInButton.setOnClickListener(this);

        emailEditText = (EditText) findViewById(R.id.edituseremail_main);
        passwordEditText = (EditText) findViewById(R.id.edituserpassword_main);

        progressbar = (ProgressBar) findViewById(R.id.progressBar_main);
        mAuth = FirebaseAuth.getInstance();

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registeruser_main:
                startActivity(new Intent(this, RegisterUserActivity.class));
                break;

            case R.id.loginbutton_main:
                userLogin();
                break;
            case R.id.forgotPassword_main:
                startActivity(new Intent(this, ForgotPasswordActivity.class));
                break;
        }
    }

    private void userLogin() {

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty()) {
            emailEditText.setError("Bitte Email eingeben");
            emailEditText.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Bitte gültige Email angeben");
            emailEditText.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            passwordEditText.setError("Bitte Passwort eingeben");
            passwordEditText.requestFocus();
            return;
        }
        if (password.length() < 6) {
            passwordEditText.setError("Passwort zu kurz");
            passwordEditText.requestFocus();
            return;
        }

        progressbar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    assert user != null;
                    if(user.isEmailVerified()){
                        startActivity(new Intent(MainActivity.this, OverviewActivity.class));
                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Bitte verifizieren sie ihre mail", Toast.LENGTH_LONG).show();
                        progressbar.setVisibility(View.GONE);
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Falsche Benutzerdaten", Toast.LENGTH_LONG).show();
                    progressbar.setVisibility(View.GONE);
                }
            }
        });
    }
}