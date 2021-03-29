package com.example.myfirebaselogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ExplainExerciseBenchPressStepTwoActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explain_exercise_bench_press_step_two);


        Button nextStep = (Button) findViewById(R.id.buttonNext);
        nextStep.setOnClickListener(this);

        Button prevStep = (Button) findViewById(R.id.buttonPrev);
        prevStep.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.buttonNext:
                startActivity(new Intent(this, ExplainExerciseBenchPressStepThreeActivity.class));
                break;

            case R.id.buttonPrev:
                startActivity(new Intent(this,ExplainExerciseBenchPressStepOneActivity.class));
                break;
        }

    }
}