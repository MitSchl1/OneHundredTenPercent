package com.example.myfirebaselogin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class ExerciseActivity extends AppCompatActivity {

    RecyclerView recyclerExercise;
    ArrayList<Exercise> exerciseList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        recyclerExercise = findViewById(R.id.recycler_exercise);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerExercise.setLayoutManager(layoutManager);

        exerciseList = (ArrayList<Exercise>) getIntent().getExtras().getSerializable("list");

        recyclerExercise.setAdapter(new ExerciseAdapter(exerciseList));

    }
}