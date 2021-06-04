package com.example.myfirebaselogin;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.Objects;

public class ShowTrainingsplanActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout layoutList;

    private DatabaseReference dbReference;
    private String userId;
    private final List<String> listOfTrainingsplanNames = new ArrayList<>();
    private AppCompatSpinner traingsplanNameSpinner;

    private final List<String> exerciseNames = new ArrayList<>();
    private final List<String> exerciseDays = new ArrayList<>();

    private Button editButton;
    private Button safeButton;
    private Button addButton;
    private Button breakButton;
    private Button deleteButton;
    private EditText trainingsplantitleEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_trainingsplan);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        dbReference = FirebaseDatabase.getInstance().getReference("Users");
        assert firebaseUser != null;
        userId = firebaseUser.getUid();
        layoutList = findViewById(R.id.layoutlist__showtrainingsplan);

        listOfTrainingsplanNames.add("Trainingsplan auswählen");
        getTraininngsplanNames();
        traingsplanNameSpinner = (AppCompatSpinner) findViewById(R.id.trainingsplantitlespinner_showtrainingsplan);
        ArrayAdapter arrayAdapterTrainingsplanName = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listOfTrainingsplanNames);
        traingsplanNameSpinner.setAdapter(arrayAdapterTrainingsplanName);

        Button showButton = (Button) findViewById(R.id.showbutton_showtrainingsplan);
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
        ImageButton menuImageButton = (ImageButton) findViewById(R.id.menubutton_showtrainingsplan);
        menuImageButton.setOnClickListener(this);

        trainingsplantitleEditText = (EditText) findViewById(R.id.edittrainingsplantitle_showtrainingsplan);

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

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showbutton_showtrainingsplan:
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
            case R.id.menubutton_showtrainingsplan:
                startActivity(new Intent(this, OverviewActivity.class));
                break;
        }
    }

    private void deleteTrainingsplan() {
        dbReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                assert userProfile != null;

                int currentTrainingsplanIndex = 0;
                for (Trainingsplan t : userProfile.getTrainingsplanList()) {
                    if (t.getName().equals(traingsplanNameSpinner.getSelectedItem().toString())) {
                        currentTrainingsplanIndex = userProfile.getTrainingsplanList().indexOf(t);
                    }
                }

                userProfile.removeTrainingsplanFromList(currentTrainingsplanIndex);

                FirebaseDatabase.getInstance().getReference("Users")
                        .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void selectedTrainingsplan() {
        int layoutListLength = layoutList.getChildCount();
        for (int i = 0; i <= layoutListLength; i++) {
            layoutList.removeView(layoutList.getChildAt(0));
        }

        dbReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                assert userProfile != null;


                for (Trainingsplan t : userProfile.getTrainingsplanList()) {

                    if (t.getName().equals(traingsplanNameSpinner.getSelectedItem().toString())) {
                        for (Exercise e : t.getExerciseList()) {
                            @SuppressLint("InflateParams") final View trainingsplanView = getLayoutInflater().inflate(R.layout.row_trainingsplan, null, false);

                            TextView trainingsplanDay = trainingsplanView.findViewById(R.id.exerciseday_rowtrainingsplan);
                            TextView trainingsplanExercise = trainingsplanView.findViewById(R.id.exercisename_rowtrainingsplan);
                            TextView trainingsplanExtraWeight = trainingsplanView.findViewById(R.id.extraweight_rowtrainingsplan);

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

        trainingsplantitleEditText.setText(traingsplanNameSpinner.getSelectedItem().toString());

        int layoutListLength = layoutList.getChildCount();
        for (int i = 0; i <= layoutListLength; i++) {
            layoutList.removeView(layoutList.getChildAt(0));
        }

        final ArrayAdapter arrayAdapterExerciseName = new ArrayAdapter(this, android.R.layout.simple_spinner_item, exerciseNames);
        final ArrayAdapter arrayAdapterDayName = new ArrayAdapter(this, android.R.layout.simple_spinner_item, exerciseDays);
        dbReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                assert userProfile != null;


                for (Trainingsplan t : userProfile.getTrainingsplanList()) {

                    if (t.getName().equals(traingsplanNameSpinner.getSelectedItem().toString())) {
                        for (Exercise e : t.getExerciseList()) {
                            @SuppressLint("InflateParams") final View exerciseView = getLayoutInflater().inflate(R.layout.row_add_exercise, null, false);

                            EditText editText = (EditText) exerciseView.findViewById(R.id.editextraweight_rowaddexercise);
                            editText.setText(String.valueOf(e.getExtraWeight()));
                            AppCompatSpinner spinnerExercise = (AppCompatSpinner) exerciseView.findViewById(R.id.exercisename_rowaddexercise);
                            AppCompatSpinner spinnerDay = (AppCompatSpinner) exerciseView.findViewById(R.id.dayname_rowaddexercise);
                            TextView closeX = (TextView) exerciseView.findViewById(R.id.removebutton_rowaddexercise);


                            spinnerExercise.setAdapter(arrayAdapterExerciseName);
                            spinnerDay.setAdapter(arrayAdapterDayName);
                            for (int i = 0; i < 8; i++) {
                                if (spinnerDay.getItemAtPosition(i).toString().equals(e.getDay())) {
                                    spinnerDay.setSelection(i);
                                }
                            }
                            for (int i = 0; i < 4; i++) {
                                if (spinnerExercise.getItemAtPosition(i).toString().equals(e.getName())) {
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
        deleteButton.setVisibility(View.GONE);
        trainingsplantitleEditText.setVisibility(View.VISIBLE);

    }


    private void addView() {
        @SuppressLint("InflateParams") final View exerciseView = getLayoutInflater().inflate(R.layout.row_add_exercise, null, false);

        EditText editText = (EditText) exerciseView.findViewById(R.id.editextraweight_rowaddexercise);
        AppCompatSpinner spinnerExercise = (AppCompatSpinner) exerciseView.findViewById(R.id.exercisename_rowaddexercise);
        AppCompatSpinner spinnerDay = (AppCompatSpinner) exerciseView.findViewById(R.id.dayname_rowaddexercise);
        TextView closeX = (TextView) exerciseView.findViewById(R.id.removebutton_rowaddexercise);

        ArrayAdapter arrayAdapterExerciseName = new ArrayAdapter(this, android.R.layout.simple_spinner_item, exerciseNames);
        ArrayAdapter arrayAdapterDayName = new ArrayAdapter(this, android.R.layout.simple_spinner_item, exerciseDays);

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
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
                for (Trainingsplan t : userProfile.getTrainingsplanList()) {
                    if (t.getName().equals(traingsplanNameSpinner.getSelectedItem().toString())) {
                        currentTrainingsplanIndex = userProfile.getTrainingsplanList().indexOf(t);
                    }
                }
                final String trainingsplantitle = trainingsplantitleEditText.getText().toString().trim();

                for (int i = 0; i < layoutList.getChildCount(); i++) {

                    View exerciseView = layoutList.getChildAt(i);

                    EditText editTextExtraWeight = (EditText) exerciseView.findViewById(R.id.editextraweight_rowaddexercise);
                    AppCompatSpinner spinnerExercise = (AppCompatSpinner) exerciseView.findViewById(R.id.exercisename_rowaddexercise);
                    AppCompatSpinner spinnerDays = (AppCompatSpinner) exerciseView.findViewById(R.id.dayname_rowaddexercise);

                    Exercise exercise = new Exercise();

                    if (!editTextExtraWeight.getText().toString().equals("")) {
                        exercise.setExtraWeight(Double.parseDouble(editTextExtraWeight.getText().toString()));
                    } else {
                        exercise.setExtraWeight(0);

                    }
                    if (spinnerExercise.getSelectedItemPosition() == 0) {
                        editTextExtraWeight.setError("Bitte Übung auswählen");
                        editTextExtraWeight.requestFocus();
                        return;
                    }
                    if (spinnerDays.getSelectedItemPosition() == 0) {
                        editTextExtraWeight.setError("Bitte Tag auswählen");
                        editTextExtraWeight.requestFocus();
                        return;
                    }


                    exercise.setName(exerciseNames.get(spinnerExercise.getSelectedItemPosition()));
                    exercise.setDay(exerciseDays.get(spinnerDays.getSelectedItemPosition()));
                    if (exercise.getName().equals("Liegestütze")) {
                        exercisePoints = (int) (pushUps * (1 / userWeight * (userWeight + exercise.getExtraWeight())));
                        exercise.setPoints(exercisePoints);
                    }
                    if (exercise.getName().equals("Kniebeugen")) {
                        exercisePoints = (int) (squats * (1 / userWeight * (userWeight + exercise.getExtraWeight())));
                        exercise.setPoints(exercisePoints);
                    }
                    if (exercise.getName().equals("Klimmzüge")) {
                        exercisePoints = (int) (pullUps * (1 / userWeight * (userWeight + exercise.getExtraWeight())));
                        exercise.setPoints(exercisePoints);
                    }
                    exerciseList.add(exercise);
                }

                if (exerciseList.size() == 0) {
                    addButton.setError("Bitte zuerst eine Uebung hinzufuegen");
                    addButton.requestFocus();
                    return;
                }
                if (trainingsplantitle.isEmpty()) {
                    trainingsplantitleEditText.setError("Bitte gib einen Titel ein");
                    trainingsplantitleEditText.requestFocus();
                    return;
                }

                for (Exercise e : exerciseList) {
                    pointSum += e.getPoints();
                }
                Trainingsplan trainingsplan = new Trainingsplan(exerciseList, trainingsplantitle, pointSum);
                userProfile.editTrainingsplan(currentTrainingsplanIndex, trainingsplan);
                addButton.setVisibility(View.GONE);
                breakButton.setVisibility(View.GONE);
                safeButton.setVisibility(View.GONE);
                editButton.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
                trainingsplantitleEditText.setVisibility(View.GONE);

                FirebaseDatabase.getInstance().getReference("Users")
                        .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
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
        deleteButton.setVisibility(View.VISIBLE);
        trainingsplantitleEditText.setVisibility(View.GONE);
        selectedTrainingsplan();
    }
}