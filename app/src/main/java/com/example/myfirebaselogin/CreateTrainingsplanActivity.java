package com.example.myfirebaselogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CreateTrainingsplanActivity extends AppCompatActivity implements View.OnClickListener {
    Button buttonAdd;
    Button buttonSubmit;
    LinearLayout layoutList;

    List<String> exerciseList = new ArrayList<>();
    ArrayList<Exercise> exerciseList2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trainingsplan);

        buttonAdd = (Button) findViewById(R.id.addbutton);
        buttonSubmit = (Button) findViewById(R.id.submitbutton);
        layoutList = findViewById(R.id.layout_list);

        buttonAdd.setOnClickListener(this);
        buttonSubmit.setOnClickListener(this);

        exerciseList.add("Ficken");
        exerciseList.add("Wie");
        exerciseList.add("einfach");
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.addbutton:
                addView();
                break;
            case R.id.submitbutton:
                if(checkIfValidAndRead()) {
                    Intent intent = new Intent(CreateTrainingsplanActivity.this, ExerciseActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("list", exerciseList2);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
        }

    }

    private boolean checkIfValidAndRead() {
        exerciseList2.clear();
        boolean result = true;

        for(int i=0;i<layoutList.getChildCount();i++){

            View exerciseView = layoutList.getChildAt(i);

            EditText editTextRepetition = (EditText)exerciseView.findViewById(R.id.edit_name);
            AppCompatSpinner spinnerExercise = (AppCompatSpinner)exerciseView.findViewById(R.id.exercise_name);

            Exercise exercise = new Exercise();

            if(!editTextRepetition.getText().toString().equals("")){
                exercise.setRepetitions(Integer.parseInt(editTextRepetition.getText().toString()));
                //exercise.setRepetitions(5);
            }else {
                result = false;
                break;
            }

            if(spinnerExercise.getSelectedItemPosition()!=0){
                exercise.setName(exerciseList.get(spinnerExercise.getSelectedItemPosition()));
            }else {
                result = false;
                break;
            }

            exerciseList2.add(exercise);

        }

        if(exerciseList2.size()==0){
            result = false;
            Toast.makeText(this, "Add Cricketers First!", Toast.LENGTH_SHORT).show();
        }else if(!result){
            Toast.makeText(this, "Enter All Details Correctly!", Toast.LENGTH_SHORT).show();
        }


        return result;
    }

    private void addView() {
        final View exerciseView = getLayoutInflater().inflate(R.layout.row_add_exercise,null,false);

        EditText editText = (EditText)exerciseView.findViewById(R.id.edit_name);
        AppCompatSpinner spinnerExercise = (AppCompatSpinner)exerciseView.findViewById(R.id.exercise_name);


        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,exerciseList);
        spinnerExercise.setAdapter(arrayAdapter);

        layoutList.addView(exerciseView);
    }
}