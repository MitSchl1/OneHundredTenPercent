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

public class CreateTrainingsplanActivity extends AppCompatActivity implements View.OnClickListener {
    private Button addButton;
    private LinearLayout layoutList;

    private final int pushUps = 6;
    private final int pullUps = 10;
    private final int squats = 8;


    private DatabaseReference dbReference;
    private String userId;

    private final List<String> exerciseNames = new ArrayList<>();
    private final List<String> exerciseDays = new ArrayList<>();
    private final ArrayList<Exercise> exerciseList = new ArrayList<>();
    private EditText trainingsplantitleEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trainingsplan);

        addButton = (Button) findViewById(R.id.addbutton_createtrainingsplan);
        Button submitButton = (Button) findViewById(R.id.submitbutton_createtrainingsplan);
        ImageButton menuImageButton = (ImageButton) findViewById(R.id.menubutton_createtrainingsplan);
        addButton.setOnClickListener(this);
        submitButton.setOnClickListener(this);
        menuImageButton.setOnClickListener(this);

        layoutList = findViewById(R.id.layoutlist_createtrainingsplan);
        trainingsplantitleEditText = (EditText) findViewById(R.id.edittrainingsplantitle_createtrainingsplan);


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
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        dbReference = FirebaseDatabase.getInstance().getReference("Users");
        assert firebaseUser != null;
        userId = firebaseUser.getUid();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addbutton_createtrainingsplan:
                addView();
                break;
            case R.id.submitbutton_createtrainingsplan:
                createTrainingsplan();
                break;
            case R.id.menubutton_createtrainingsplan:
                startActivity(new Intent(this, OverviewActivity.class));
                break;
        }

    }


    private void createTrainingsplan() {
        dbReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                assert userProfile != null;
                double userWeight = userProfile.getWeight();
                int exercisePoints;
                int pointSum = 0;
                boolean existSuccessCreateFirstTrainingsplan = false;
                boolean existSuccessTenPercentExtraWeight = true;
                boolean existSuccessTwentyFivePercentExtraWeight = true;
                boolean existSuccessFiftyPercentExtraWeight = true;
                boolean existSuccessSeventyFivePercentExtraWeight = true;
                boolean existSuccessOneHundredPercentExtraWeight = true;
                boolean existSuccessOneHundredTenPercentExtraWeight = true;

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
                        exerciseList.clear();
                        editTextExtraWeight.setError("Bitte Übung auswählen");
                        editTextExtraWeight.requestFocus();
                        return;
                    }
                    if (spinnerDays.getSelectedItemPosition() == 0) {
                        exerciseList.clear();
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

                    if (exercise.getExtraWeight() >= userWeight * 0.1) {
                        existSuccessTenPercentExtraWeight = false;
                    }
                    if (exercise.getExtraWeight() >= userWeight * 0.25) {
                        existSuccessTwentyFivePercentExtraWeight = false;
                    }
                    if (exercise.getExtraWeight() >= userWeight * 0.5) {
                        existSuccessFiftyPercentExtraWeight = false;
                    }
                    if (exercise.getExtraWeight() >= userWeight * 0.75) {
                        existSuccessSeventyFivePercentExtraWeight = false;
                    }
                    if (exercise.getExtraWeight() >= userWeight) {
                        existSuccessOneHundredPercentExtraWeight = false;
                    }
                    if (exercise.getExtraWeight() >= userWeight * 1.1) {
                        existSuccessOneHundredTenPercentExtraWeight = false;
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
                userProfile.addTrainingsplanToList(trainingsplan);


                for (Successes s : userProfile.getSuccesses()) {
                    if (s.equals(Successes.CREATEFIRSTTRAININGSPLAN)) {
                        existSuccessCreateFirstTrainingsplan = true;
                    } else if (s.equals(Successes.TENPERCENTEXTRAWEIGHT)) {
                        existSuccessTenPercentExtraWeight = true;
                    } else if (s.equals(Successes.TWENTYFIVEPERCENTEXTRAWEIGHT)) {
                        existSuccessTwentyFivePercentExtraWeight = true;
                    } else if (s.equals(Successes.FIFTYPERCENTEXTRAWEIGHT)) {
                        existSuccessFiftyPercentExtraWeight = true;
                    } else if (s.equals(Successes.SEVENTYFIVEPERCENTEXTRAWEIGHT)) {
                        existSuccessSeventyFivePercentExtraWeight = true;
                    } else if (s.equals(Successes.ONEHUNDREDPERCENTEXTRAWEIGHT)) {
                        existSuccessOneHundredPercentExtraWeight = true;
                    } else if (s.equals(Successes.ONEHUNDREDTENPERCENTEXTRAWEIGHT)) {
                        existSuccessOneHundredTenPercentExtraWeight = true;
                    }

                }
                if (!existSuccessCreateFirstTrainingsplan) {
                    userProfile.addSuccess(Successes.CREATEFIRSTTRAININGSPLAN);
                    userProfile.setPoints(userProfile.getPoints() + Successes.CREATEFIRSTTRAININGSPLAN.getPoints());
                    Toast.makeText(CreateTrainingsplanActivity.this, "Neuer Erfolg freigeschalten", Toast.LENGTH_LONG).show();
                }
                if (!existSuccessTenPercentExtraWeight) {
                    userProfile.addSuccess(Successes.TENPERCENTEXTRAWEIGHT);
                    userProfile.setPoints(userProfile.getPoints() + Successes.TENPERCENTEXTRAWEIGHT.getPoints());
                    Toast.makeText(CreateTrainingsplanActivity.this, "Neuer Erfolg freigeschalten", Toast.LENGTH_LONG).show();
                }
                if (!existSuccessTwentyFivePercentExtraWeight) {
                    userProfile.addSuccess(Successes.TWENTYFIVEPERCENTEXTRAWEIGHT);
                    userProfile.setPoints(userProfile.getPoints() + Successes.TWENTYFIVEPERCENTEXTRAWEIGHT.getPoints());
                    Toast.makeText(CreateTrainingsplanActivity.this, "Neuer Erfolg freigeschalten", Toast.LENGTH_LONG).show();
                }
                if (!existSuccessFiftyPercentExtraWeight) {
                    userProfile.addSuccess(Successes.FIFTYPERCENTEXTRAWEIGHT);
                    userProfile.setPoints(userProfile.getPoints() + Successes.FIFTYPERCENTEXTRAWEIGHT.getPoints());
                    Toast.makeText(CreateTrainingsplanActivity.this, "Neuer Erfolg freigeschalten", Toast.LENGTH_LONG).show();
                }
                if (!existSuccessSeventyFivePercentExtraWeight) {
                    userProfile.addSuccess(Successes.SEVENTYFIVEPERCENTEXTRAWEIGHT);
                    userProfile.setPoints(userProfile.getPoints() + Successes.SEVENTYFIVEPERCENTEXTRAWEIGHT.getPoints());
                    Toast.makeText(CreateTrainingsplanActivity.this, "Neuer Erfolg freigeschalten", Toast.LENGTH_LONG).show();
                }
                if (!existSuccessOneHundredPercentExtraWeight) {
                    userProfile.addSuccess(Successes.ONEHUNDREDPERCENTEXTRAWEIGHT);
                    userProfile.setPoints(userProfile.getPoints() + Successes.ONEHUNDREDPERCENTEXTRAWEIGHT.getPoints());
                    Toast.makeText(CreateTrainingsplanActivity.this, "Neuer Erfolg freigeschalten", Toast.LENGTH_LONG).show();
                }
                if (!existSuccessOneHundredTenPercentExtraWeight) {
                    userProfile.addSuccess(Successes.ONEHUNDREDTENPERCENTEXTRAWEIGHT);
                    userProfile.setPoints(userProfile.getPoints() + Successes.ONEHUNDREDTENPERCENTEXTRAWEIGHT.getPoints());
                    Toast.makeText(CreateTrainingsplanActivity.this, "Neuer Erfolg freigeschalten", Toast.LENGTH_LONG).show();
                }
                FirebaseDatabase.getInstance().getReference("Users")
                        .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                        .setValue(userProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CreateTrainingsplanActivity.this, "Trainingsplan Erfolgreich angelegt", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(CreateTrainingsplanActivity.this, OverviewActivity.class));

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
        @SuppressLint("InflateParams") final View exerciseView = getLayoutInflater().inflate(R.layout.row_add_exercise, null, false);

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
}