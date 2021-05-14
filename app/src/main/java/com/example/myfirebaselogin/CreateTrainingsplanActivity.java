package com.example.myfirebaselogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CreateTrainingsplanActivity extends AppCompatActivity implements View.OnClickListener {
    private Button buttonAdd;
    private Button buttonSubmit;
    private LinearLayout layoutList;

    private DatabaseReference dbReference;
    private String userId;
    private FirebaseUser user;

    private List<String> exerciseNames = new ArrayList<>();
    private ArrayList<Exercise> exerciseList = new ArrayList<>();
    private EditText editTextTrainingsplantitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trainingsplan);

        buttonAdd = (Button) findViewById(R.id.addbutton);
        buttonSubmit = (Button) findViewById(R.id.submitbutton);
        layoutList = findViewById(R.id.layout_list);
        editTextTrainingsplantitle = (EditText) findViewById(R.id.trainingsplantitle);
        buttonAdd.setOnClickListener(this);
        buttonSubmit.setOnClickListener(this);

        exerciseNames.add("Ficken");
        exerciseNames.add("Wie");
        exerciseNames.add("einfach");

        user = FirebaseAuth.getInstance().getCurrentUser();
        dbReference = FirebaseDatabase.getInstance().getReference("Users");
        userId = user.getUid();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.addbutton:
                addView();
                break;  
            case R.id.submitbutton:
               /*if(checkIfValidAndRead()) {
                    /*Intent intent = new Intent(CreateTrainingsplanActivity.this, ExerciseActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("list", exerciseList);
                    intent.putExtras(bundle);
                    startActivity(intent);


               }*/
                createTrainingsplan();
                break;
        }

    }



    private void createTrainingsplan() {
        dbReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                assert userProfile != null;
                //ArrayList<Exercise> xx = new ArrayList<>();
                //Exercise e = new Exercise("nice",2);
                //xx.add(e);
                final String trainingsplantitle = editTextTrainingsplantitle.getText().toString().trim();
                for(int i=0;i<layoutList.getChildCount();i++){

                    View exerciseView = layoutList.getChildAt(i);

                    EditText editTextRepetition = (EditText)exerciseView.findViewById(R.id.edit_minute);
                    AppCompatSpinner spinnerExercise = (AppCompatSpinner)exerciseView.findViewById(R.id.exercise_name);

                    Exercise exercise = new Exercise();

                    if(!editTextRepetition.getText().toString().equals("")){
                        exercise.setRepetitions(Integer.parseInt(editTextRepetition.getText().toString()));
                    }else{
                        editTextRepetition.setError("Bitte Minuten angeben");
                        editTextRepetition.requestFocus();
                        return;
                    }

                    exercise.setName(exerciseNames.get(spinnerExercise.getSelectedItemPosition()));
                    exerciseList.add(exercise);
                }
                if(exerciseList.size()==0){
                    buttonAdd.setError("Bitte zuerst eine Uebung hinzufuegen");
                    buttonAdd.requestFocus();
                    return;
                }
                if(trainingsplantitle.isEmpty()){
                    editTextTrainingsplantitle.setError("Bitte gib einen Titel ein");
                    editTextTrainingsplantitle.requestFocus();
                    return;
                }
                Trainingsplan trainingsplan = new Trainingsplan(exerciseList,trainingsplantitle);
                userProfile.addTrainingsplanToList(trainingsplan);

                FirebaseDatabase.getInstance().getReference("Users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(userProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CreateTrainingsplanActivity.this, "Nutzer erfolgreich registriert", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(CreateTrainingsplanActivity.this, ProfileActivty.class));

                        } else {
                            Toast.makeText(CreateTrainingsplanActivity.this, "Registrierung fehlgeschlagen! Probiers nochmal", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private boolean checkIfValidAndRead() {
        exerciseList.clear();
        boolean result = true;

        for(int i=0;i<layoutList.getChildCount();i++){

            View exerciseView = layoutList.getChildAt(i);

            EditText editTextRepetition = (EditText)exerciseView.findViewById(R.id.edit_minute);
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
                exercise.setName(exerciseNames.get(spinnerExercise.getSelectedItemPosition()));
            }else {
                result = false;
                break;
            }

            exerciseList.add(exercise);

        }

        if(exerciseList.size()==0){
            result = false;
            Toast.makeText(this, "Add Cricketers First!", Toast.LENGTH_SHORT).show();
        }else if(!result){
            Toast.makeText(this, "Enter All Details Correctly!", Toast.LENGTH_SHORT).show();
        }


        return result;
    }

    private void addView() {
        final View exerciseView = getLayoutInflater().inflate(R.layout.row_add_exercise,null,false);

        EditText editText = (EditText)exerciseView.findViewById(R.id.edit_minute);
        AppCompatSpinner spinnerExercise = (AppCompatSpinner)exerciseView.findViewById(R.id.exercise_name);
        TextView closeX = (TextView) exerciseView.findViewById(R.id.button_remove);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, exerciseNames);
        spinnerExercise.setAdapter(arrayAdapter);
        closeX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView(exerciseView);
            }
        });
        layoutList.addView(exerciseView);
    }
    private void removeView(View view) {
        layoutList.removeView(view);
    }
}