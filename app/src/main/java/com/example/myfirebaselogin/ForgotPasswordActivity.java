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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText emailEditText;
    private ProgressBar progressbar;

    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailEditText = (EditText) findViewById(R.id.edituseremail_forgotpassword);
        Button resetButton = (Button) findViewById(R.id.resetpasswordbutton_forgotpassword);
        progressbar = (ProgressBar) findViewById(R.id.progressBar_forgotpassword);

        mAuth = FirebaseAuth.getInstance();

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 resetPassword();
            }

        });
    }

    private void resetPassword() {
        String email = emailEditText.getText().toString().trim();

        if(email.isEmpty()){
            emailEditText.setError("Bitte Mail eingeben");
            emailEditText.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches() ){
            emailEditText.setError("Bitte gültige Email angeben");
            emailEditText.requestFocus();
            return;
        }
        progressbar.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPasswordActivity.this,"Email zum zurücksetzen des Passworts wurde versendet", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ForgotPasswordActivity.this, MainActivity.class));
                    progressbar.setVisibility(View.GONE);
                }else{
                    Toast.makeText(ForgotPasswordActivity.this," Probiers nochmal ", Toast.LENGTH_LONG).show();
                    progressbar.setVisibility(View.GONE);
                }
            }
        });
    }
}