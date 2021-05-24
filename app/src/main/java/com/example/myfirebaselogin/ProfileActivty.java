package com.example.myfirebaselogin;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.collection.LLRBNode;

public class ProfileActivty extends AppCompatActivity implements View.OnClickListener {
    private TextView changePassword;
    private Button logout;
    private FirebaseUser user;
    private DatabaseReference dbReference;
    private String userId;
    private Button editProfile;

    private GridLayout gridLayoutList;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_activty);

        logout = (Button) findViewById(R.id.signoutbutton_profile);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivty.this, MainActivity.class));

            }
        });

        gridLayoutList = findViewById(R.id.layoutlist_profile);
        gridLayoutList.setColumnCount(3);
        changePassword = (TextView) findViewById(R.id.changePassword_profile);
        changePassword.setOnClickListener(this);
        editProfile = (Button) findViewById(R.id.editProfilebutton_profile);
        editProfile.setOnClickListener(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        dbReference = FirebaseDatabase.getInstance().getReference("Users");
        userId = user.getUid();

        final TextView nameTextView = (TextView) findViewById(R.id.username_profile);
        final TextView emailTextView = (TextView) findViewById(R.id.useremailAdress_profile);
        final TextView weightTextView = (TextView) findViewById(R.id.userweight_profile);

        dbReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    String name = userProfile.getName();
                    String mail = userProfile.getMail();
                    String weight = String.valueOf(userProfile.getWeight());

                    nameTextView.setText(name);
                    emailTextView.setText(mail);
                    weightTextView.setText(weight);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivty.this, "Da ist wohl was schiefgelaufen", Toast.LENGTH_LONG).show();
            }
        });
        getAllSuccesses();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editProfilebutton_profile:
                startActivity(new Intent(this, EditProfileActivity.class));
                break;

            case R.id.changePassword_profile:
                startActivity(new Intent(this, ForgotPassword.class));
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getAllSuccesses(){

        for(Successes s : Successes.values()){
            final View successesView = getLayoutInflater().inflate(R.layout.row_successes, null, false);

            TextView successName = successesView.findViewById(R.id.success_rowsuccesses);
            successName.setText(s.getName());

            gridLayoutList.addView(successesView);
        }

    }
}