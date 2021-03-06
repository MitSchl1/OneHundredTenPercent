package com.example.oneHundredTenPercent;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
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

public class ProfileActivty extends AppCompatActivity implements View.OnClickListener {

    private GridLayout gridLayoutList;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_activty);

        gridLayoutList = findViewById(R.id.layoutlist_profile);
        gridLayoutList.setColumnCount(3);

        Button editProfileButton = (Button) findViewById(R.id.editProfilebutton_profile);
        editProfileButton.setOnClickListener(this);
        ImageButton menuImageButton = (ImageButton) findViewById(R.id.menubutton_profile);
        menuImageButton.setOnClickListener(this);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("Users");
        assert firebaseUser != null;
        String userId = firebaseUser.getUid();

        final TextView nameTextView = (TextView) findViewById(R.id.username_profile);
        final TextView emailTextView = (TextView) findViewById(R.id.useremailAdress_profile);
        final TextView weightTextView = (TextView) findViewById(R.id.userweight_profile);
        final TextView pointTextView = (TextView) findViewById(R.id.userpoints_profile);

        dbReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    String name = userProfile.getName();
                    String mail = userProfile.getMail();
                    String weight = String.valueOf(userProfile.getWeight());
                    String points = String.valueOf(userProfile.getPoints());

                    nameTextView.setText(name);
                    emailTextView.setText(mail);
                    weightTextView.setText(weight);
                    pointTextView.setText(points);

                    getAllSuccesses(userProfile);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivty.this, "Da ist wohl was schiefgelaufen.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editProfilebutton_profile:
                startActivity(new Intent(this, EditProfileActivity.class));
                break;
            case R.id.menubutton_profile:
                startActivity(new Intent(this, OverviewActivity.class));
                break;

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getAllSuccesses(User user) {
        boolean existSuccessOneHundredPoints = false;
        boolean existSuccessTwoHundredFiftyPoints = false;
        boolean existSuccessFiveHundredPoints = false;
        boolean existSuccessOneThousandPoints = false;

        for (Successes s : user.getSuccesses()) {
            if (s.getName().equals(Successes.ONEHUNDREDPOINTS.getName())) {
                existSuccessOneHundredPoints = true;
            }
            if (s.getName().equals(Successes.TWOHUNDREDFIFTYPOINTS.getName())) {
                existSuccessTwoHundredFiftyPoints = true;
            }
            if (s.getName().equals(Successes.FIVEHUNDREDPOINTS.getName())) {
                existSuccessFiveHundredPoints = true;
            }
            if (s.getName().equals(Successes.ONETHOUSANDPOINTS.getName())) {
                existSuccessOneThousandPoints = true;
            }
        }

        if (user.getPoints() >= 100) {
            if (!existSuccessOneHundredPoints) {
                user.addSuccess(Successes.ONEHUNDREDPOINTS);
                user.setPoints(user.getPoints() + Successes.ONEHUNDREDPOINTS.getPoints());
                Toast.makeText(ProfileActivty.this, "Neuer Erfolg freigeschalten", Toast.LENGTH_SHORT).show();
            }
        }
        if (user.getPoints() >= 250) {
            if (!existSuccessTwoHundredFiftyPoints) {
                user.addSuccess(Successes.TWOHUNDREDFIFTYPOINTS);
                user.setPoints(user.getPoints() + Successes.TWOHUNDREDFIFTYPOINTS.getPoints());
                Toast.makeText(ProfileActivty.this, "Neuer Erfolg freigeschalten", Toast.LENGTH_SHORT).show();
            }
        }
        if (user.getPoints() >= 500) {
            if (!existSuccessFiveHundredPoints) {
                user.addSuccess(Successes.FIVEHUNDREDPOINTS);
                user.setPoints(user.getPoints() + Successes.FIVEHUNDREDPOINTS.getPoints());
                Toast.makeText(ProfileActivty.this, "Neuer Erfolg freigeschalten", Toast.LENGTH_SHORT).show();
            }
        }
        if (user.getPoints() >= 1000) {
            if (!existSuccessOneThousandPoints) {
                user.addSuccess(Successes.ONETHOUSANDPOINTS);
                user.setPoints(user.getPoints() + Successes.ONETHOUSANDPOINTS.getPoints());
                Toast.makeText(ProfileActivty.this, "Neuer Erfolg freigeschalten", Toast.LENGTH_SHORT).show();
            }
        }
        FirebaseDatabase.getInstance().getReference("Users")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(ProfileActivty.this, "Da ist wohl was Schief gelaufen", Toast.LENGTH_LONG).show();
                }
            }
        });
        for (Successes s : user.getSuccesses()) {
            @SuppressLint("InflateParams") final View successesView = getLayoutInflater().inflate(R.layout.row_successes, null, false);

            TextView successName = successesView.findViewById(R.id.success_rowsuccesses);
            successName.setText(s.getName());
            gridLayoutList.addView(successesView);
        }

    }
}