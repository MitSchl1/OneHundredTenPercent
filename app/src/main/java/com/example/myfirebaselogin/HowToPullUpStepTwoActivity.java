package com.example.myfirebaselogin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class HowToPullUpStepTwoActivity extends AppCompatActivity implements View.OnClickListener {
    Button prevStepButton, nextStepButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_pull_up_step_two);

        prevStepButton = (Button) findViewById(R.id.buttonPrev_howtopullupsteptwo);
        prevStepButton.setOnClickListener(this);
        nextStepButton = (Button) findViewById(R.id.buttonNext_howtopullupsteptwo);
        nextStepButton.setOnClickListener(this);
        ImageButton menuImageButton = (ImageButton) findViewById(R.id.menubutton_howtopullupsteptwo);
        menuImageButton.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonPrev_howtopullupsteptwo:
                startActivity(new Intent(this, HowToPullUpStepOneActivity.class));
                break;
            case R.id.buttonNext_howtopullupsteptwo:
                startActivity(new Intent(this, OverviewActivity.class));
                break;
            case R.id.menubutton_howtopullupsteptwo:
                startActivity(new Intent(this, OverviewActivity.class));
                break;
        }
    }

}