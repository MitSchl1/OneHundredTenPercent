package com.example.myfirebaselogin;

import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.O)
public enum Successes {
    CREATEACCOUNT("Account erstellt",10,Color.YELLOW),
    CREATEACCOUN("Account erstellt",10, Color.YELLOW),
    CREATEACCOUT("Account erstellt",10, Color.YELLOW),
    CREATEACCONT("Account erstellt",10, Color.YELLOW),
    CREATEACCUNT("Account erstellt",10, Color.YELLOW),
    CRETEACCOUNT("Account erstellt",10, Color.YELLOW),
    CREATEFIRSTTRAININGSPLAN("Ersten Trainingsplan erstellt",50, Color.RED);


    private String name;
    private  int points;
    private int color;

    Successes(String name, int points, int color){
        this.name = name;
        this.points = points;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
