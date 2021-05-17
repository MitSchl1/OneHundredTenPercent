package com.example.myfirebaselogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
        Button showButton = (Button) findViewById(R.id.showbutton);
        showButton.setOnClickListener(this);

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
        layoutList.removeView(layoutList.getChildAt(0));
        dbReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                assert userProfile != null;



                    for(Trainingsplan t : userProfile.getTrainingsplanList()){
                        final View trainingsplanView = getLayoutInflater().inflate(R.layout.row_trainingsplan, null, false);
                        TextView trainingsplanTitle = trainingsplanView.findViewById(R.id.name_trainingsplan);

                        trainingsplanTitle.setText(t.getName());
                        layoutList.addView(trainingsplanView);

                    }

                    /*
                    if (t.getName().equals(spinnerTraingsplanName.getSelectedItem().toString())) {
                        trainingsplanTitle.setText(t.getName());
                        layoutList.addView(trainingsplanView);

                    }*/

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showView(View v, ArrayList<Trainingsplan> t, TextView textview) {
        for (Trainingsplan trainingsplan : t) {
            textview.setText(trainingsplan.getName());
            layoutList.addView(v);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showbutton:
                selectedTrainingsplan();
                break;
        }
    }
}