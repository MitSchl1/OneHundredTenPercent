package com.example.myfirebaselogin;

import java.util.ArrayList;

public class Trainingsplan {
    private ArrayList<Exercise> exerciseList = new ArrayList<>();
    private String name;

    public Trainingsplan() {
    }

    public Trainingsplan(ArrayList<Exercise> exerciseList, String name) {
        this.exerciseList = exerciseList;
        this.name = name;
    }

    public ArrayList<Exercise> getExerciseList() {
        return exerciseList;
    }

    public void setExerciseList(ArrayList<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
