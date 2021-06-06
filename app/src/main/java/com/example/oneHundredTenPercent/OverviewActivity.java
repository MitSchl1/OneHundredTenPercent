package com.example.oneHundredTenPercent;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class OverviewActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        Button userdataButton = (Button) findViewById(R.id.userdatabutton_overview);
        userdataButton.setOnClickListener(this);
        Button explainExerciseButton = (Button) findViewById(R.id.explainexercisebutton_overview);
        explainExerciseButton.setOnClickListener(this);
        Button createTrainingsplanButton = (Button) findViewById(R.id.createTrainingsplanbutton_overview);
        createTrainingsplanButton.setOnClickListener(this);
        Button showTrainingsplanButton = (Button) findViewById(R.id.showTrainingsplanbutton_overview);
        showTrainingsplanButton.setOnClickListener(this);
        Button startTrainingButton = (Button) findViewById(R.id.starttrainingbutton_overview);
        startTrainingButton.setOnClickListener(this);
        Button logoutButton = (Button) findViewById(R.id.signoutbutton_overview);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(OverviewActivity.this, MainActivity.class));
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userdatabutton_overview:
                startActivity(new Intent(this, ProfileActivty.class));
                break;
            case R.id.explainexercisebutton_overview:
                startActivity(new Intent(this, HowToOverviewActivity.class));
                break;
            case R.id.createTrainingsplanbutton_overview:
                startActivity(new Intent(this, CreateTrainingsplanActivity.class));
                break;
            case R.id.showTrainingsplanbutton_overview:
                startActivity(new Intent(this, ShowTrainingsplanActivity.class));
                break;
            case R.id.starttrainingbutton_overview:
                startActivity(new Intent(this, StartTrainingActivity.class));
                break;

        }
    }
}