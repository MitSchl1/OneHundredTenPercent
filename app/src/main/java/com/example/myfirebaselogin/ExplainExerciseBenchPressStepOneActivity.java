package com.example.myfirebaselogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ExplainExerciseBenchPressStepOneActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explain_exercise);

        Button nextStep = (Button) findViewById(R.id.buttonNext);
        nextStep.setOnClickListener(this);

        Button prevStep = (Button) findViewById(R.id.buttonPrev);
        prevStep.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.buttonNext:
                startActivity(new Intent(this, ExplainExerciseBenchPressStepTwoActivity.class));
                break;

            case R.id.buttonPrev:
                startActivity(new Intent(this,OverviewActivity.class));
                break;
        }

    }
}