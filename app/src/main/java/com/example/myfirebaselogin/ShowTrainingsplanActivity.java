package com.example.myfirebaselogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    private Button showButton,editButton,safeButton;

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
                break;
        }
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
        editButton.setVisibility(View.GONE);
        safeButton.setVisibility(View.VISIBLE);

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


    }
    private void safeEditedTrainingsplan() {
        editButton.setVisibility(View.VISIBLE);
        safeButton.setVisibility(View.GONE);
    }


    public void showView(View v, ArrayList<Trainingsplan> t, TextView textview) {
        for (Trainingsplan trainingsplan : t) {
            textview.setText(trainingsplan.getName());
            layoutList.addView(v);
        }
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

}