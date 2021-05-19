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

    private int pushUps = 6;
    private int pullUps = 10;
    private int squats = 8;


    private DatabaseReference dbReference;
    private String userId;
    private FirebaseUser user;

    private List<String> exerciseNames = new ArrayList<>();
    private List<String> exerciseDays = new ArrayList<>();
    private ArrayList<Exercise> exerciseList = new ArrayList<>();
    private EditText editTextTrainingsplantitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trainingsplan);

        buttonAdd = (Button) findViewById(R.id.addbutton_createtrainingsplan);
        buttonSubmit = (Button) findViewById(R.id.submitbutton_createtrainingsplan);
        layoutList = findViewById(R.id.layoutlist_createtrainingsplan);
        editTextTrainingsplantitle = (EditText) findViewById(R.id.edittrainingsplantitle_createtrainingsplan);
        buttonAdd.setOnClickListener(this);
        buttonSubmit.setOnClickListener(this);

        exerciseNames.add("Übung");
        exerciseNames.add("Liegestütze");
        exerciseNames.add("Kniebeugen");
        exerciseNames.add("Klimmzüge");

        exerciseDays.add("Tag");
        exerciseDays.add("Montag");
        exerciseDays.add("Dienstag");
        exerciseDays.add("Mittwoch");
        exerciseDays.add("Donnerstag");
        exerciseDays.add("Freitag");
        exerciseDays.add("Samstag");
        exerciseDays.add("Sonntag");
        user = FirebaseAuth.getInstance().getCurrentUser();
        dbReference = FirebaseDatabase.getInstance().getReference("Users");
        userId = user.getUid();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.addbutton_createtrainingsplan:
                addView();
                break;  
            case R.id.submitbutton_createtrainingsplan:
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
                double userWeight = userProfile.getWeight();
                int exercisePoints;
                int pointSum = 0;


                final String trainingsplantitle = editTextTrainingsplantitle.getText().toString().trim();
                for(int i=0;i<layoutList.getChildCount();i++){

                    View exerciseView = layoutList.getChildAt(i);

                    EditText editTextExtraWeight = (EditText)exerciseView.findViewById(R.id.editextraweight_rowaddexercise);
                    AppCompatSpinner spinnerExercise = (AppCompatSpinner)exerciseView.findViewById(R.id.exercisename_rowaddexercise);
                    AppCompatSpinner spinnerDays = (AppCompatSpinner) exerciseView.findViewById(R.id.dayname_rowaddexercise);

                    Exercise exercise = new Exercise();

                    if(!editTextExtraWeight.getText().toString().equals("")){
                        exercise.setExtraWeight(Double.parseDouble(editTextExtraWeight.getText().toString()));
                    }else{
                        exercise.setExtraWeight(0);

                    }
                    if(spinnerExercise.getSelectedItemPosition()==0){
                        editTextExtraWeight.setError("Bitte Übung auswählen");
                        editTextExtraWeight.requestFocus();
                        return;
                    }
                    if(spinnerDays.getSelectedItemPosition()==0){
                        editTextExtraWeight.setError("Bitte Tag auswählen");
                        editTextExtraWeight.requestFocus();
                        return;
                    }


                    exercise.setName(exerciseNames.get(spinnerExercise.getSelectedItemPosition()));
                    exercise.setDay(exerciseDays.get(spinnerDays.getSelectedItemPosition()));
                    if(exercise.getName().equals("Liegestütze")){
                        exercisePoints = (int) (pushUps*(1/userWeight*(userWeight+exercise.getExtraWeight())));
                        exercise.setPoints(exercisePoints);
                    }
                    if(exercise.getName().equals("Kniebeugen")){
                        exercisePoints = (int) (squats*(1/userWeight*(userWeight+exercise.getExtraWeight())));
                        exercise.setPoints(exercisePoints);
                    }
                    if(exercise.getName().equals("Klimmzüge")){
                        exercisePoints = (int) (pullUps*(1/userWeight*(userWeight+exercise.getExtraWeight())));
                        exercise.setPoints(exercisePoints);
                    }
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

                for(Exercise e : exerciseList){
                    pointSum += e.getPoints();
                }
                Trainingsplan trainingsplan = new Trainingsplan(exerciseList,trainingsplantitle,pointSum);
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

    private void addView() {
        final View exerciseView = getLayoutInflater().inflate(R.layout.row_add_exercise,null,false);

        EditText editText = (EditText)exerciseView.findViewById(R.id.editextraweight_rowaddexercise);
        AppCompatSpinner spinnerExercise = (AppCompatSpinner)exerciseView.findViewById(R.id.exercisename_rowaddexercise);
        AppCompatSpinner spinnerDay = (AppCompatSpinner)exerciseView.findViewById(R.id.dayname_rowaddexercise);
        TextView closeX = (TextView) exerciseView.findViewById(R.id.removebutton_rowaddexercise);

        ArrayAdapter arrayAdapterExerciseName = new ArrayAdapter(this,android.R.layout.simple_spinner_item, exerciseNames);
        ArrayAdapter arrayAdapterDayName = new ArrayAdapter(this,android.R.layout.simple_spinner_item, exerciseDays);

        spinnerExercise.setAdapter(arrayAdapterExerciseName);
        spinnerDay.setAdapter(arrayAdapterDayName);
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