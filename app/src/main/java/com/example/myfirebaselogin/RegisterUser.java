package com.example.myfirebaselogin;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {
    private TextView banner, registerUser;
    private EditText editTextName, editTextMail, editTextPassword;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private Task<Void> myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        banner =(TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        registerUser = (Button) findViewById(R.id.registerButton);
        registerUser.setOnClickListener(this);

        editTextName = (EditText) findViewById(R.id.name);
        editTextMail = (EditText) findViewById(R.id.mail);
        editTextPassword = (EditText) findViewById(R.id.password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.banner:
                startActivity( new Intent(this, MainActivity.class));
                break;
            case R.id.registerButton:
                registerUser();
                break;

        }
    }

    private void registerUser() {
        final String userMail = editTextMail.getText().toString().trim();
        final String userName = editTextName.getText().toString().trim();
        String userPassword = editTextPassword.getText().toString().trim();

        if (userName.isEmpty()) {
            editTextName.setError("Bitte Name eingeben");
            editTextName.requestFocus();
            return;
        }
        if (userMail.isEmpty()) {
            editTextMail.setError("Bitte Email eingeben");
            editTextMail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(userMail).matches()) {
            editTextMail.setError("Bitte gültige Email angeben");
            editTextMail.requestFocus();
            return;
        }
        if (userPassword.isEmpty()) {
            editTextPassword.setError("Bitte Email eingeben");
            editTextPassword.requestFocus();
            return;
        }
        if (userPassword.length() < 6) {
            editTextPassword.setError("Passwort zu kurz");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(userMail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(userName, userMail);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterUser.this, "Nutzer erfolgreich registriert", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(RegisterUser.this, MainActivity.class));
                                        progressBar.setVisibility(View.GONE);

                                    } else {
                                        Toast.makeText(RegisterUser.this, "Registrierung fehlgeschlagen! Probiers nochmal", Toast.LENGTH_LONG).show();
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