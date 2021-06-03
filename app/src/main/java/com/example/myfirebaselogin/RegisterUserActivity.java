package com.example.myfirebaselogin;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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

public class RegisterUserActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView banner, registerUser;
    private EditText editTextName, editTextMail, editTextPassword, editTextWeight;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private Task<Void> myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        banner =(TextView) findViewById(R.id.banner_registeruser);
        banner.setOnClickListener(this);

        registerUser = (Button) findViewById(R.id.registerButton_registeruser);
        registerUser.setOnClickListener(this);

        editTextName = (EditText) findViewById(R.id.editusername_registeruser);
        editTextMail = (EditText) findViewById(R.id.edituseremail_registeruser);
        editTextPassword = (EditText) findViewById(R.id.edituserpassword_registeruser);
        editTextWeight = (EditText) findViewById(R.id.edituserweight_registeruser);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_registeruser);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.banner_registeruser:
                startActivity( new Intent(this, MainActivity.class));
                break;
            case R.id.registerButton_registeruser:
                registerUser();
                break;

        }
    }

    private void registerUser() {
        final String userMail = editTextMail.getText().toString().trim();
        final String userName = editTextName.getText().toString().trim();
        String userPassword = editTextPassword.getText().toString().trim();
        final String stringWeight = editTextWeight.getText().toString().trim();
        final double userWeight = Double.parseDouble(stringWeight);


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
        if(stringWeight.isEmpty()){
            editTextWeight.setError("Bitte Gewicht eingeben");
            editTextWeight.requestFocus();
            return;
        }
        if(userWeight < 20 || userWeight > 300) {
            editTextWeight.setError("Bitte gültiges GEwicht eingeben");
            editTextWeight.requestFocus();
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
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
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