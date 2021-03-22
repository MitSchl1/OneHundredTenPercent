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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView banner;
    private EditText editTextName, editTextMail, editTextWeight;
    private ProgressBar progressBar;
    private FirebaseUser user;
    private DatabaseReference dbReference;
    private String userId;
    private Task<Void> myRef;

    Button editProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        banner =(TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        editProfileButton = (Button) findViewById(R.id.editButton);
        editProfileButton.setOnClickListener(this);

        editTextName = (EditText) findViewById(R.id.name);
        editTextMail = (EditText) findViewById(R.id.mail);
        editTextWeight = (EditText) findViewById(R.id.weight);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        user = FirebaseAuth.getInstance().getCurrentUser();
        dbReference = FirebaseDatabase.getInstance().getReference("Users");
        userId = user.getUid();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.banner:
                startActivity( new Intent(this, MainActivity.class));
                break;
            case R.id.editButton:
                editUserProfile();
                break;

        }
    }

    private void editUserProfile() {


        dbReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                String userMail = editTextMail.getText().toString().trim();
                String userName = editTextName.getText().toString().trim();
                String stringWeight = editTextWeight.getText().toString().trim();
                double userWeight = 0;
                if(userProfile != null){

                    String name = userProfile.name;
                    String mail = userProfile.mail;
                    double weight = userProfile.weight;

                    if(userMail.isEmpty()){
                        userMail = mail;
                    }else if (!Patterns.EMAIL_ADDRESS.matcher(userMail).matches()) {
                        editTextMail.setError("Bitte gültige Email angeben");
                        editTextMail.requestFocus();
                        return;
                    }else if(userMail.equals(mail)){
                        editTextMail.setError("eingegebene Mail gleich der Mail");
                        editTextMail.requestFocus();
                        return;
                    }
                    else{
                        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
                        fUser.updateEmail(userMail);
                        fUser.sendEmailVerification();
                    }
                    if(userName.isEmpty()){
                        userName = name;
                    }
                    if(stringWeight.isEmpty()){
                        userWeight = weight;
                    }else{
                        userWeight = Double.parseDouble(stringWeight);
                    }
                }

                progressBar.setVisibility(View.VISIBLE);
                User ChangeDataUser = new User(userName, userMail, userWeight);
                FirebaseDatabase.getInstance().getReference("Users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(ChangeDataUser).addOnCompleteListener(new OnCompleteListener<Void>() {
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