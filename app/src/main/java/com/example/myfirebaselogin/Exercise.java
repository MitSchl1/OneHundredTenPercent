package com.example.myfirebaselogin;

import java.io.Serializable;
import java.util.Date;

public class Exercise implements Serializable {
    private String name;
    private int minutes;
    private int points;
    private String day;

    public Exercise(){

    }

    public Exercise(String name, int minutes, int points, String day) {
        this.name = name;
        this.minutes = minutes;
        this.points = points;
        this.day = day;
    }

    public String getName() {
        return name;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
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
