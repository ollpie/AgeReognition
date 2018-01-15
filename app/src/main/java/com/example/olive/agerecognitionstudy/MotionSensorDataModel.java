package com.example.olive.agerecognitionstudy;

import java.util.ArrayList;

/**
 * Created by olive on 15.01.2018.
 */

public class MotionSensorDataModel {

    String participantID;
    String taskID;
    ArrayList<Long> timestamp;

    ArrayList<Float> xAcc;
    ArrayList<Float> yAcc;
    ArrayList<Float> zAcc;

    ArrayList<Float> xGravity;
    ArrayList<Float> yGravity;
    ArrayList<Float> zGravity;

    ArrayList<Float> xGyroscope;
    ArrayList<Float> yGyroscope;
    ArrayList<Float> zGyroscope;

    ArrayList<Float> xRotation;
    ArrayList<Float> yRotation;
    ArrayList<Float> zRotation;

    public MotionSensorDataModel() {
        this.timestamp = new ArrayList<>();
        this.xAcc = new ArrayList<>();
        this.yAcc = new ArrayList<>();
        this.zAcc = new ArrayList<>();

        this.xGravity = new ArrayList<>();
        this.yGravity = new ArrayList<>();
        this.zGravity = new ArrayList<>();

        this.xGyroscope = new ArrayList<>();
        this.yGyroscope = new ArrayList<>();
        this.zGyroscope = new ArrayList<>();

        this.xRotation = new ArrayList<>();
        this.yRotation = new ArrayList<>();
        this.zRotation = new ArrayList<>();
    }

    public MotionSensorDataModel(String participantID, String taskID) {
        this.participantID = participantID;
        this.taskID = taskID;

        this.timestamp = new ArrayList<>();
        this.xAcc = new ArrayList<>();
        this.yAcc = new ArrayList<>();
        this.zAcc = new ArrayList<>();

        this.xGravity = new ArrayList<>();
        this.yGravity = new ArrayList<>();
        this.zGravity = new ArrayList<>();

        this.xGyroscope = new ArrayList<>();
        this.yGyroscope = new ArrayList<>();
        this.zGyroscope = new ArrayList<>();

        this.xRotation = new ArrayList<>();
        this.yRotation = new ArrayList<>();
        this.zRotation = new ArrayList<>();
    }

    //Setter methods
    public void setXAcc (Float x){
        this.xAcc.add(x);
    }

    public void setYAcc (Float y){
        this.yAcc.add(y);
    }

    public void setZAcc (Float z){
        this.zAcc.add(z);
    }

    public void setXGravity (Float x){
        this.xGravity.add(x);
    }

    public void setYGravity (Float y){
        this.yGravity.add(y);
    }

    public void setZGravity (Float z){
        this.zGravity.add(z);
    }

    public void setXGyroscope (Float x){
        this.xGyroscope.add(x);
    }

    public void setYGyroscope (Float y){
        this.yGyroscope.add(y);
    }

    public void setZGyroscope (Float z){
        this.zGyroscope.add(z);
    }

    public void setXRotation (Float x){
        this.xRotation.add(x);
    }

    public void setYRotation (Float y){
        this.yRotation.add(y);
    }

    public void setZRotation (Float z){
        this.zRotation.add(z);
    }

    public void setTimestamp (Long timestamp){
        this.timestamp.add(timestamp);
    }

    public void setParticipantID (String participantID){
        this.participantID = participantID;
    }

    public void setTaskID (String taskID){
        this.participantID = taskID;
    }

    // Getter methods
    public ArrayList<Float> getXAcc (){
        return this.xAcc;
    }

    public ArrayList<Float> getYAcc (){
        return this.yAcc;
    }

    public ArrayList<Float> getZAcc (){
        return this.zAcc;
    }

    public ArrayList<Float> getXGravity (){
        return this.xGravity;
    }

    public ArrayList<Float> getYGravity (){
        return this.yGravity;
    }

    public ArrayList<Float> getZGravity (){
        return this.zGravity;
    }

    public ArrayList<Float> getXGyroscope (){
        return this.xGyroscope;
    }

    public ArrayList<Float> getYGyroscope (){
        return this.yGyroscope;
    }

    public ArrayList<Float> getZGyroscope (){
        return this.zGyroscope;
    }

    public ArrayList<Float> getXRotation (){
        return this.xRotation;
    }

    public ArrayList<Float> getYRotation (){
        return this.yRotation;
    }

    public ArrayList<Float> getZRotation (){
        return this.zRotation;
    }

    public ArrayList<Long> getTimestamp (){
        return this.timestamp;
    }

    public String getParticipantID (){
        return this.participantID;
    }

    public String getTaskID (){
        return this.taskID;
    }

    public int length() {
        return xAcc.size();
    }
}
