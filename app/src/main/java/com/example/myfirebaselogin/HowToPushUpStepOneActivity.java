package com.example.myfirebaselogin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class HowToPushUpStepOneActivity extends AppCompatActivity implements View.OnClickListener {
    Button prevStepButton, nextStepButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_push_up_step_one);

        prevStepButton = (Button) findViewById(R.id.buttonPrev_howtopushupstepone);
        prevStepButton.setOnClickListener(this);
        nextStepButton = (Button) findViewById(R.id.buttonNext_howtopushupstepone);
        nextStepButton.setOnClickListener(this);
        ImageButton menuImageButton = (ImageButton) findViewById(R.id.menubutton_howtopushupstepone);
        menuImageButton.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonPrev_howtopushupstepone:
                startActivity(new Intent(this, HowToOverviewActivity.class));
                break;
            case R.id.buttonNext_howtopushupstepone:
                startActivity(new Intent(this, HowToPushUpStepTwoActivity.class));
                break;
            case R.id.menubutton_howtopushupstepone:
                startActivity(new Intent(this, OverviewActivity.class));
                break;
        }
    }

}