package com.example.myfirebaselogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class OverviewActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        Button userdata = (Button) findViewById(R.id.userdatabutton_overview);
        userdata.setOnClickListener(this);
        Button explainExercise = (Button) findViewById(R.id.explainexercisebutton_overview);
        explainExercise.setOnClickListener(this);
        Button createTrainingsplan = (Button) findViewById(R.id.createTrainingsplanbutton_overview);
        createTrainingsplan.setOnClickListener(this);
        Button showTrainingsplan = (Button) findViewById(R.id.showTrainingsplanbutton_overview);
        showTrainingsplan.setOnClickListener(this);
        Button logout = (Button) findViewById(R.id.signoutbutton_overview);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(OverviewActivity.this, MainActivity.class));
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userdatabutton_overview:
                startActivity(new Intent(this, ProfileActivty.class));
                break;
            case R.id.explainexercisebutton_overview:
                startActivity(new Intent (this, ExplainExerciseBenchPressStepOneActivity.class));
                break;
            case R.id.createTrainingsplanbutton_overview:
                startActivity(new Intent (this, CreateTrainingsplanActivity.class));
                break;
            case R.id.showTrainingsplanbutton_overview:
                startActivity(new Intent (this, ShowTrainingsplanActivity.class));
                break;

        }
    }
}