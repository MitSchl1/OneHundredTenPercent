package com.example.myfirebaselogin;

import java.util.ArrayList;

public class User {
    private String name, mail;
    private double weight;
    private ArrayList<Trainingsplan> trainingsplanList = new ArrayList<Trainingsplan>();
    private ArrayList<Successes> successes = new ArrayList<>();

    public User(){

    }
    public User(String name, String mail, double weight){
        this.name = name;
        this.mail = mail;
        this.weight = weight;
        //this.trainingsplanList = new ArrayList<Trainingsplan>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public  ArrayList<Trainingsplan> getTrainingsplanList() {
        return trainingsplanList;
    }

    public void setTrainingsplanList( ArrayList<Trainingsplan>trainingsplanList) {
        this.trainingsplanList = trainingsplanList;
    }
    public void addTrainingsplanToList( Trainingsplan trainingsplan){
        this.trainingsplanList.add(trainingsplan);
    }
    public void removeTrainingsplanFromList(int index){
        this.trainingsplanList.remove(index);
    }
    public void editTrainingsplan ( int index,Trainingsplan trainingsplan){
        this.trainingsplanList.set(index,trainingsplan);
    }

    public ArrayList<Successes> getSuccesses() {
        return successes;
    }

    public void setSuccesses(ArrayList<Successes> successes) {
        this.successes = successes;
    }

    public void addSuccess( Successes s ){
        this.successes.add(s);
    }
}
