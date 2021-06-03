package com.example.myfirebaselogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

    private TextView register, forgotPassword;
    private EditText editTextEmail, editTextPassword;
    private Button signIn;
    private FirebaseAuth mAuth;
    private ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = (TextView) findViewById(R.id.registeruser_main);
        register.setOnClickListener(this);

        forgotPassword = (TextView) findViewById(R.id.forgotPassword_main);
        forgotPassword.setOnClickListener(this);
        signIn = (Button) findViewById(R.id.loginbutton_main);
        signIn.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.edituseremail_main);
        editTextPassword = (EditText) findViewById(R.id.edituserpassword_main);

        progressbar = (ProgressBar) findViewById(R.id.progressBar_main);
        mAuth = FirebaseAuth.getInstance();

    }

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

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Bitte Email eingeben");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Bitte gültige Email angeben");
            editTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editTextPassword.setError("Bitte Passwort eingeben");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editTextPassword.setError("Passwort zu kurz");
            editTextPassword.requestFocus();
            return;
        }

        progressbar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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