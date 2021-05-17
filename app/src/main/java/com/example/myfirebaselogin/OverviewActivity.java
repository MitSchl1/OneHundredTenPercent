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

        Button userdata = (Button) findViewById(R.id.userdata);
        userdata.setOnClickListener(this);
        Button explainExercise = (Button) findViewById(R.id.explainexercise);
        explainExercise.setOnClickListener(this);
        Button createTrainingsplan = (Button) findViewById(R.id.createTrainingsplan);
        createTrainingsplan.setOnClickListener(this);
        Button showTrainingsplan = (Button) findViewById(R.id.showTrainingsplan);
        showTrainingsplan.setOnClickListener(this);
        Button logout = (Button) findViewById(R.id.signout);
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
            case R.id.userdata:
                startActivity(new Intent(this, ProfileActivty.class));
                break;
            case R.id.explainexercise:
                startActivity(new Intent (this, ExplainExerciseBenchPressStepOneActivity.class));
                break;
            case R.id.createTrainingsplan:
                startActivity(new Intent (this, CreateTrainingsplanActivity.class));
                break;
            case R.id.showTrainingsplan:
                startActivity(new Intent (this, ShowTrainingsplanActivity.class));
                break;

        }
    }
}