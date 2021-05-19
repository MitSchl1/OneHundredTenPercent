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

public class ShowTrainingsplanActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout layoutList;

    private DatabaseReference dbReference;
    private String userId;
    private FirebaseUser user;
    private List<String> listOfTrainingsplanNames = new ArrayList<>();
    private AppCompatSpinner spinnerTraingsplanName;

    private List<String> exerciseNames = new ArrayList<>();
    private List<String> exerciseDays = new ArrayList<>();

    private Button showButton,editButton,safeButton, addButton, breakButton, deleteButton;
    EditText editTextTrainingsplantitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_trainingsplan);

        user = FirebaseAuth.getInstance().getCurrentUser();
        dbReference = FirebaseDatabase.getInstance().getReference("Users");
        userId = user.getUid();
        layoutList = findViewById(R.id.layout_list_trainingsplan);

        listOfTrainingsplanNames.add("Trainingsplan auswählen");
        getTraininngsplanNames();
        spinnerTraingsplanName = (AppCompatSpinner) findViewById(R.id.spinner_trainingsplanlist);
        ArrayAdapter arrayAdapterTrainingsplanName = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listOfTrainingsplanNames);
        spinnerTraingsplanName.setAdapter(arrayAdapterTrainingsplanName);

        showButton = (Button) findViewById(R.id.showbutton);
        showButton.setOnClickListener(this);
        editButton = (Button) findViewById(R.id.editbutton_showtrainingsplan);
        editButton.setOnClickListener(this);
        safeButton = (Button) findViewById(R.id.safebutton_showtrainingsplan);
        safeButton.setOnClickListener(this);
        addButton = (Button) findViewById(R.id.addbutton_showtrainingsplan);
        addButton.setOnClickListener(this);
        breakButton = (Button) findViewById(R.id.breakbutton_showtrainingsplan);
        breakButton.setOnClickListener(this);
        deleteButton = (Button) findViewById(R.id.deletebutton_showtrainingsplan);
        deleteButton.setOnClickListener(this);

        editTextTrainingsplantitle = (EditText) findViewById(R.id.edittrainingsplantitle_showtrainingsplan);

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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showbutton:
                selectedTrainingsplan();
                break;
            case R.id.editbutton_showtrainingsplan:
                editCurrentTrainingsplan();
                break;
            case R.id.safebutton_showtrainingsplan:
                safeEditedTrainingsplan();
                //selectedTrainingsplan();
                break;
            case R.id.addbutton_showtrainingsplan:
                addView();
                break;
            case R.id.breakbutton_showtrainingsplan:
                breakEditingTrainingsplan();
                break;
            case R.id.deletebutton_showtrainingsplan:
                deleteTrainingsplan();
                break;
        }
    }

    private void deleteTrainingsplan() {
        dbReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                assert userProfile != null;

                int currentTrainingsplanIndex = 0;
                for(Trainingsplan t : userProfile.getTrainingsplanList()){
                    if(t.getName().equals(spinnerTraingsplanName.getSelectedItem().toString())){
                        currentTrainingsplanIndex = userProfile.getTrainingsplanList().indexOf(t);
                    }
                }

                userProfile.removeTrainingsplanFromList(currentTrainingsplanIndex);

                FirebaseDatabase.getInstance().getReference("Users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(userProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ShowTrainingsplanActivity.this, "Trainingsplan erfolgreich bearbeitet", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(ShowTrainingsplanActivity.this, ShowTrainingsplanActivity.class));

                        } else {
                            Toast.makeText(ShowTrainingsplanActivity.this, "Bearbeitung fehlgeschlagen! Probiers nochmal", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void getTraininngsplanNames() {
        dbReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                assert userProfile != null;
                for (Trainingsplan t : userProfile.getTrainingsplanList()) {
                    listOfTrainingsplanNames.add(t.getName());
                }
                //final View trainingsplanView = getLayoutInflater().inflate(R.layout.row_trainingsplan,null,false);
                //TextView trainingsplanTitle = trainingsplanView.findViewById(R.id.name_trainingsplan);
                //showView(trainingsplanView,userProfile.getTrainingsplanList(),trainingsplanTitle);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void selectedTrainingsplan() {
        int layoutListLength = layoutList.getChildCount();
        for(int i=0;i <= layoutListLength;i++){
            layoutList.removeView(layoutList.getChildAt(0));
        }

        dbReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                assert userProfile != null;


                for (Trainingsplan t : userProfile.getTrainingsplanList()) {

                    if (t.getName().equals(spinnerTraingsplanName.getSelectedItem().toString())) {
                        for (Exercise e : t.getExerciseList()) {
                            final View trainingsplanView = getLayoutInflater().inflate(R.layout.row_trainingsplan, null, false);

                            TextView trainingsplanDay = trainingsplanView.findViewById(R.id.day_trainingsplan);
                            TextView trainingsplanExercise = trainingsplanView.findViewById(R.id.exercise_name_trainingsplan);
                            TextView trainingsplanExtraWeight = trainingsplanView.findViewById(R.id.extraweight_trainingsplan);

                            String extraWeightString = String.valueOf(e.getExtraWeight()) + " kg";
                            trainingsplanDay.setText(e.getDay());
                            trainingsplanExercise.setText(e.getName());
                            trainingsplanExtraWeight.setText(extraWeightString);

                            layoutList.addView(trainingsplanView);
                        }

                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void editCurrentTrainingsplan() {

        editTextTrainingsplantitle.setText(spinnerTraingsplanName.getSelectedItem().toString());

        int layoutListLength = layoutList.getChildCount();
        for(int i=0;i <= layoutListLength;i++){
            layoutList.removeView(layoutList.getChildAt(0));
        }

        final ArrayAdapter arrayAdapterExerciseName = new ArrayAdapter(this,android.R.layout.simple_spinner_item, exerciseNames);
        final ArrayAdapter arrayAdapterDayName = new ArrayAdapter(this,android.R.layout.simple_spinner_item, exerciseDays);
        dbReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                assert userProfile != null;


                for (Trainingsplan t : userProfile.getTrainingsplanList()) {

                    if (t.getName().equals(spinnerTraingsplanName.getSelectedItem().toString())) {
                        for (Exercise e : t.getExerciseList()) {
                            final View exerciseView = getLayoutInflater().inflate(R.layout.row_add_exercise,null,false);

                            EditText editText = (EditText)exerciseView.findViewById(R.id.edit_extraweight);
                            editText.setText(String.valueOf(e.getExtraWeight()));
                            AppCompatSpinner spinnerExercise = (AppCompatSpinner)exerciseView.findViewById(R.id.exercise_name);
                            AppCompatSpinner spinnerDay = (AppCompatSpinner)exerciseView.findViewById(R.id.day_name);
                            TextView closeX = (TextView) exerciseView.findViewById(R.id.button_remove);


                            spinnerExercise.setAdapter(arrayAdapterExerciseName);
                            spinnerDay.setAdapter(arrayAdapterDayName);
                            for(int i =0 ; i<8;i++){
                                if(spinnerDay.getItemAtPosition(i).toString().equals(e.getDay())){
                                    spinnerDay.setSelection(i);
                                }
                            }
                            for(int i = 0; i<4; i++){
                                if(spinnerExercise.getItemAtPosition(i).toString().equals(e.getName())){
                                    spinnerExercise.setSelection(i);
                                }
                            }
                            closeX.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    removeView(exerciseView);
                                }
                            });
                            layoutList.addView(exerciseView);
                        }

                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        addButton.setVisibility(View.VISIBLE);
        breakButton.setVisibility(View.VISIBLE);
        safeButton.setVisibility(View.VISIBLE);
        editButton.setVisibility(View.GONE);
        editTextTrainingsplantitle.setVisibility(View.VISIBLE);

    }





    private void addView() {
        final View exerciseView = getLayoutInflater().inflate(R.layout.row_add_exercise,null,false);

        EditText editText = (EditText)exerciseView.findViewById(R.id.edit_extraweight);
        AppCompatSpinner spinnerExercise = (AppCompatSpinner)exerciseView.findViewById(R.id.exercise_name);
        AppCompatSpinner spinnerDay = (AppCompatSpinner)exerciseView.findViewById(R.id.day_name);
        TextView closeX = (TextView) exerciseView.findViewById(R.id.button_remove);

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
    private void safeEditedTrainingsplan() {
        dbReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                assert userProfile != null;
                double userWeight = userProfile.getWeight();
                int exercisePoints;
                int pointSum = 0;
                int pushUps = 6;
                int pullUps = 10;
                int squats = 7;

                ArrayList<Exercise> exerciseList = new ArrayList<>();
                int currentTrainingsplanIndex = 0;
                for(Trainingsplan t : userProfile.getTrainingsplanList()){
                    if(t.getName().equals(spinnerTraingsplanName.getSelectedItem().toString())){
                        currentTrainingsplanIndex = userProfile.getTrainingsplanList().indexOf(t);
                    }
                }
                final String trainingsplantitle = editTextTrainingsplantitle.getText().toString().trim();

                for(int i=0;i<layoutList.getChildCount();i++){

                    View exerciseView = layoutList.getChildAt(i);

                    EditText editTextExtraWeight = (EditText)exerciseView.findViewById(R.id.edit_extraweight);
                    AppCompatSpinner spinnerExercise = (AppCompatSpinner)exerciseView.findViewById(R.id.exercise_name);
                    AppCompatSpinner spinnerDays = (AppCompatSpinner) exerciseView.findViewById(R.id.day_name);

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
                    addButton.setError("Bitte zuerst eine Uebung hinzufuegen");
                    addButton.requestFocus();
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
                userProfile.editTrainingsplan(currentTrainingsplanIndex,trainingsplan);
                addButton.setVisibility(View.GONE);
                breakButton.setVisibility(View.GONE);
                safeButton.setVisibility(View.GONE);
                editButton.setVisibility(View.VISIBLE);
                editTextTrainingsplantitle.setVisibility(View.GONE);

                FirebaseDatabase.getInstance().getReference("Users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(userProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ShowTrainingsplanActivity.this, "Trainingsplan erfolgreich bearbeitet", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(ShowTrainingsplanActivity.this, ShowTrainingsplanActivity.class));

                        } else {
                            Toast.makeText(ShowTrainingsplanActivity.this, "Bearbeitung fehlgeschlagen! Probiers nochmal", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void breakEditingTrainingsplan() {
        addButton.setVisibility(View.GONE);
        breakButton.setVisibility(View.GONE);
        safeButton.setVisibility(View.GONE);
        editButton.setVisibility(View.VISIBLE);
        editTextTrainingsplantitle.setVisibility(View.GONE);
        selectedTrainingsplan();
    }
}