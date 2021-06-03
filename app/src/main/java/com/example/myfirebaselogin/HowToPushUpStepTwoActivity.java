package com.example.myfirebaselogin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HowToPushUpStepTwoActivity extends AppCompatActivity implements View.OnClickListener {
    Button prevStepButton, nextStepButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_push_up_step_two);

        prevStepButton = (Button) findViewById(R.id.buttonPrev_howtopushupsteptwo);
        prevStepButton.setOnClickListener(this);
        nextStepButton = (Button) findViewById(R.id.buttonNext_howtopushupsteptwo);
        nextStepButton.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonPrev_howtopushupsteptwo:
                startActivity(new Intent(this, HowToPushUpStepOneActivity.class));
                break;
            case R.id.buttonNext_howtopushupsteptwo:
                startActivity(new Intent(this, OverviewActivity.class));
                break;
        }
    }

}