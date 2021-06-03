package com.example.myfirebaselogin;

import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.O)
public enum Successes {
    CREATEACCOUNT("Firststep",10),
    CREATEFIRSTTRAININGSPLAN("Trainingsplan Rookie",20),
    FIRSTTRAINING("Fittness Rookie", 20),
    ONEHUNDREDPOINTS("100 Punkte", 20),
    TWOHUNDREDFIFTYPOINTS("250 Punkte", 25),
    FIVEHUNDREDPOINTS("500 Punkte",50),
    ONETHOUSANDPOINTS("1000 Punkte", 100),
    TENPERCENTEXTRAWEIGHT("Novize",25 ),
    TWENTYFIVEPERCENTEXTRAWEIGHT("Legionär",50),
    FIFTYPERCENTEXTRAWEIGHT("Spartiat",100),
    SEVENTYFIVEPERCENTEXTRAWEIGHT("Terminator",200),
    ONEHUNDREDPERCENTEXTRAWEIGHT("Halbgott",500),
    ONEHUNDREDTENPERCENTEXTRAWEIGHT("Obelix", 1000)
    ;


    private final String name;
    private final int points;

    Successes(String name, int points){
        this.name = name;
        this.points = points;
    }

    public String getName() {
        return name;
    }
    public int getPoints() {
        return points;
    }

}
