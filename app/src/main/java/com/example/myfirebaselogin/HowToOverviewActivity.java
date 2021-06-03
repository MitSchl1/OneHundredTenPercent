package com.example.myfirebaselogin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class HowToOverviewActivity extends AppCompatActivity implements View.OnClickListener {
    TextView squatTextView, pushupTextView, pullupTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_overview);

        squatTextView = (TextView) findViewById(R.id.squat_howtooverview);
        squatTextView.setOnClickListener(this);
        pushupTextView = (TextView) findViewById(R.id.pushup_howtooverview);
        pushupTextView.setOnClickListener(this);
        pullupTextView = (TextView) findViewById(R.id.pullup_howtooverview);
        pullupTextView.setOnClickListener(this);


    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.squat_howtooverview:
                startActivity(new Intent(this, HowToSquatStepOneActivity.class));
                break;
            case R.id.pushup_howtooverview:
                startActivity(new Intent(this, HowToPushUpStepOneActivity.class));
                break;
            case R.id.pullup_howtooverview:
                startActivity(new Intent(this, HowToPullUpStepOneActivity.class));
                break;


        }
    }
}