package com.example.myfirebaselogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class StartTrainingActivity extends AppCompatActivity implements View.OnClickListener {
    private long duration;
    private TextView timerTextView;
    private Button startButton, stopButton;

    private DatabaseReference dbReference;
    private String userId;
    private final List<String> listOfTrainingsplanNames = new ArrayList<>();
    private AppCompatSpinner traingsplanNameSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_training);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        dbReference = FirebaseDatabase.getInstance().getReference("Users");
        assert firebaseUser != null;
        userId = firebaseUser.getUid();

        timerTextView = (TextView) findViewById(R.id.timer_starttraining);
        TextView exerciseNameTextView = (TextView) findViewById(R.id.exercisename_starttraining);

        startButton = (Button) findViewById(R.id.startbutton_starttraining);
        startButton.setOnClickListener(this);
        stopButton = (Button) findViewById(R.id.stopbutton_starttraining);
        stopButton.setOnClickListener(this);
        ImageButton menuImageButton = (ImageButton) findViewById(R.id.menubutton_starttraining);
        menuImageButton.setOnClickListener(this);

        listOfTrainingsplanNames.add("Trainingsplan auswählen");
        getTraininngsplanNames();
        traingsplanNameSpinner = (AppCompatSpinner) findViewById(R.id.trainingsplantitlespinner_starttraining);
        ArrayAdapter arrayAdapterTrainingsplanName = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listOfTrainingsplanNames);
        traingsplanNameSpinner.setAdapter(arrayAdapterTrainingsplanName);


    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startbutton_starttraining:
                startTraining();
                break;
            case R.id.stopbutton_starttraining:
                Toast.makeText(StartTrainingActivity.this, "Training frühzeitig beendet. Bitte passe deinen Trainingsplan an", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, OverviewActivity.class));
                break;
            case R.id.menubutton_starttraining:
                startActivity(new Intent(this, OverviewActivity.class));
                break;
        }
    }

    public void startTraining() {

        if(traingsplanNameSpinner.getSelectedItem().equals("Trainingsplan auswählen")){
            Toast.makeText(getApplicationContext(),"Bitte zuerst einen Trainingsplan auswählen",Toast.LENGTH_SHORT).show();
            return;
        }
        dbReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final User userProfile = snapshot.getValue(User.class);
                assert userProfile != null;

                for (final Trainingsplan t : userProfile.getTrainingsplanList()) {
                    if (traingsplanNameSpinner.getSelectedItem().toString().equals("Trainingsplan auswählen")) {
                        startButton.setError("Bitte zuerst Trainingsplan auswählen");
                        startButton.requestFocus();
                        break;
                    }
                    if (t.getName().equals(traingsplanNameSpinner.getSelectedItem().toString())) {
                        int minutes = 0;
                        for (Exercise e : t.getExerciseList()) {
                            minutes += 1;
                        }
                        minutes = minutes * 10;
                        duration = TimeUnit.MINUTES.toMillis(minutes);

                        new CountDownTimer(duration, 1000) {

                            @Override
                            public void onTick(long millisUntilFinished) {
                                String stringDuration = String.format(Locale.ENGLISH, "%02d : %02d", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                                timerTextView.setText(stringDuration);

                            }

                            @Override
                            public void onFinish() {
                                timerTextView.setVisibility(View.GONE);
                                userProfile.setPoints(userProfile.getPoints() + t.getPointSum());
                                Toast.makeText(getApplicationContext(), "Training beendet dir wurden " + t.getPointSum() + " Punkte gutgeschrieben", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(StartTrainingActivity.this, OverviewActivity.class));
                            }
                        }.start();
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        startButton.setVisibility(View.GONE);
        stopButton.setVisibility(View.VISIBLE);

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
}