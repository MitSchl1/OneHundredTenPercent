package com.example.myfirebaselogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonPrev_howtopullupsteptwo:
                startActivity(new Intent(this, HowToPullUpStepOneActivity.class));
                break;
            case R.id.buttonNext_howtopullupsteptwo:
                startActivity(new Intent(this, OverviewActivity.class));
                break;
        }
    }

}