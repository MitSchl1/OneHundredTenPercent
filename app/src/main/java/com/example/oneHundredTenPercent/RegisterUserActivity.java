package com.example.oneHundredTenPercent;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
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
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterUserActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText nameEditText, emailEditText, passwordEditText, weightEditText;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        TextView bannerTextView = (TextView) findViewById(R.id.banner_registeruser);
        bannerTextView.setOnClickListener(this);

        TextView registerUserTextView = (Button) findViewById(R.id.registerButton_registeruser);
        registerUserTextView.setOnClickListener(this);

        nameEditText = (EditText) findViewById(R.id.editusername_registeruser);
        emailEditText = (EditText) findViewById(R.id.edituseremail_registeruser);
        passwordEditText = (EditText) findViewById(R.id.edituserpassword_registeruser);
        weightEditText = (EditText) findViewById(R.id.edituserweight_registeruser);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_registeruser);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.banner_registeruser:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.registerButton_registeruser:
                registerUser();
                break;

        }
    }

    private void registerUser() {
        final String userMail = emailEditText.getText().toString().trim();
        final String userName = nameEditText.getText().toString().trim();
        String userPassword = passwordEditText.getText().toString().trim();
        final String stringWeight = weightEditText.getText().toString().trim();
        final double userWeight = Double.parseDouble(stringWeight);


        if (userName.isEmpty()) {
            nameEditText.setError("Bitte Name eingeben");
            nameEditText.requestFocus();
            return;
        }
        if (userMail.isEmpty()) {
            emailEditText.setError("Bitte Email eingeben");
            emailEditText.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(userMail).matches()) {
            emailEditText.setError("Bitte gültige Email angeben");
            emailEditText.requestFocus();
            return;
        }
        if (userPassword.isEmpty()) {
            passwordEditText.setError("Bitte Email eingeben");
            passwordEditText.requestFocus();
            return;
        }
        if (userPassword.length() < 6) {
            passwordEditText.setError("Passwort zu kurz");
            passwordEditText.requestFocus();
            return;
        }
        if (stringWeight.isEmpty()) {
            weightEditText.setError("Bitte Gewicht eingeben");
            weightEditText.requestFocus();
            return;
        }
        if (userWeight < 20 || userWeight > 300) {
            weightEditText.setError("Bitte gültiges Gewicht eingeben. Zwischen 20 & 300 kg");
            weightEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(userMail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(userName, userMail, userWeight);
                            user.addSuccess(Successes.CREATEACCOUNT);
                            user.setPoints(user.getPoints() + Successes.CREATEACCOUNT.getPoints());
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterUserActivity.this, "Nutzer erfolgreich registriert", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(RegisterUserActivity.this, MainActivity.class));
                                        progressBar.setVisibility(View.GONE);

                                    } else {
                                        Toast.makeText(RegisterUserActivity.this, "Registrierung fehlgeschlagen! Probiers nochmal", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });

                            FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
                            fUser.sendEmailVerification();
                        }
                    }
                });
    }
}