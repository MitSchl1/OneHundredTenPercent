package com.example.myfirebaselogin;

import java.io.Serializable;

public class Exercise implements Serializable {
    public String name;
    public int repetitions;

    public Exercise(){

    }

    public Exercise(String name, int repetitions) {
        this.name = name;
        this.repetitions = repetitions;
    }

    public String getName() {
        return name;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }
}
