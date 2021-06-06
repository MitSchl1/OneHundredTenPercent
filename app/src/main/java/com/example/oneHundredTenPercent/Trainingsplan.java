package com.example.oneHundredTenPercent;

import java.util.ArrayList;

public class Trainingsplan {
    private ArrayList<Exercise> exerciseList = new ArrayList<>();
    private String name;
    private int pointSum;

    public Trainingsplan() {
    }

    public Trainingsplan(ArrayList<Exercise> exerciseList, String name, int pointSum) {
        this.exerciseList = exerciseList;
        this.name = name;
        this.pointSum = pointSum;
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

    public int getPointSum() {
        return pointSum;
    }

    public void setPointSum(int pointSum) {
        this.pointSum = pointSum;
    }
}
