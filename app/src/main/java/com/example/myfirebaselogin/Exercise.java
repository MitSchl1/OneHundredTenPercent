package com.example.myfirebaselogin;

import java.io.Serializable;

public class Exercise implements Serializable {
    private String name;
    private double extraWeight;
    private int points;
    private String day;

    public Exercise(){

    }

    public Exercise(String name, double extraWeight, int points, String day) {
        this.name = name;
        this.extraWeight = extraWeight;
        this.points = points;
        this.day = day;
    }

    public String getName() {
        return name;
    }

    public double getExtraWeight() {
        return extraWeight;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExtraWeight(double extraWeight) {
        this.extraWeight = extraWeight;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
