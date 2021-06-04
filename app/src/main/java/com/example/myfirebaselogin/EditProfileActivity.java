package com.example.myfirebaselogin;

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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText nameEditText, emailEditText, weightEditText;
    private ProgressBar progressBar;
    private DatabaseReference dbReference;
    private String userId;
    private Task<Void> myRef;

    Button editProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        TextView bannerTextView = (TextView) findViewById(R.id.banner_editprofile);
        bannerTextView.setOnClickListener(this);
        TextView changePasswordTextView = (TextView) findViewById(R.id.changePassword_editprofile);
        changePasswordTextView.setOnClickListener(this);

        editProfileButton = (Button) findViewById(R.id.editButton_editprofile);
        editProfileButton.setOnClickListener(this);
        ImageButton menuImageButton = (ImageButton) findViewById(R.id.menubutton_editprofile);
        menuImageButton.setOnClickListener(this);

        nameEditText = (EditText) findViewById(R.id.editusername_editprofile);
        emailEditText = (EditText) findViewById(R.id.edituseremail_editprofile);
        weightEditText = (EditText) findViewById(R.id.edituserweight_editprofile);

        progressBar = (ProgressBar) findViewById(R.id.progressBar_editprofile);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        dbReference = FirebaseDatabase.getInstance().getReference("Users");
        assert firebaseUser != null;
        userId = firebaseUser.getUid();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.banner_editprofile:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.editButton_editprofile:
                editUserProfile();
                break;
            case R.id.changePassword_editprofile:
                startActivity(new Intent(this, ForgotPasswordActivity.class));
                break;
            case R.id.menubutton_editprofile:
                startActivity(new Intent(this, OverviewActivity.class));
                break;

        }
    }

    private void editUserProfile() {


        dbReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                String userMail = emailEditText.getText().toString().trim();
                String userName = nameEditText.getText().toString().trim();
                String stringWeight = weightEditText.getText().toString().trim();
                double userWeight = 0;
                if (userProfile != null) {

                    String name = userProfile.getName();
                    String mail = userProfile.getMail();
                    double weight = userProfile.getWeight();

                    if (userMail.isEmpty()) {
                        userMail = mail;
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(userMail).matches()) {
                        emailEditText.setError("Bitte gültige Email angeben");
                        emailEditText.requestFocus();
                        return;
                    } else if (userMail.equals(mail)) {
                        emailEditText.setError("eingegebene Mail gleich der Mail");
                        emailEditText.requestFocus();
                        return;
                    } else {
                        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
                        assert fUser != null;
                        fUser.updateEmail(userMail);
                        fUser.sendEmailVerification();
                    }
                    if (userName.isEmpty()) {
                        userName = name;
                    }
                    if (stringWeight.isEmpty()) {
                        userWeight = weight;
                    } else {
                        userWeight = Double.parseDouble(stringWeight);
                    }
                }

                progressBar.setVisibility(View.VISIBLE);
                assert userProfile != null;
                userProfile.setMail(userMail);
                userProfile.setName(userName);
                userProfile.setWeight(userWeight);
                FirebaseDatabase.getInstance().getReference("Users")
                        .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                        .setValue(userProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditProfileActivity.this, "Nutzer erfolgreich registriert", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(EditProfileActivity.this, ProfileActivty.class));
                            progressBar.setVisibility(View.GONE);

                        } else {
                            Toast.makeText(EditProfileActivity.this, "Registrierung fehlgeschlagen! Probiers nochmal", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfileActivity.this, "Da ist wohl was schiefgelaufen", Toast.LENGTH_LONG).show();
            }
        });

    }
}