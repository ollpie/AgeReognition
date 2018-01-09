package com.example.olive.agerecognitionstudy;

import java.util.ArrayList;

/**
 * Created by olive on 05.01.2018.
 */

public class GenericTaskDataModel {

    ArrayList<String> userId;
    ArrayList<Integer> targetId;
    ArrayList<Long> timestamp;
    ArrayList<String> eventType;

    ArrayList<Float> xTarget;
    ArrayList<Float> yTarget;

    ArrayList<Float> xTouch;
    ArrayList<Float> yTouch;

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

    public GenericTaskDataModel () {
        this.userId = new ArrayList<>();
        this.targetId = new ArrayList<>();
        this.timestamp = new ArrayList<>();
        this.eventType = new ArrayList<>();

        this.xTarget = new ArrayList<>();
        this.yTarget = new ArrayList<>();

        this.xTouch = new ArrayList<>();
        this.yTouch = new ArrayList<>();

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

    public void setUserId (String id){
        this.userId.add(id);
    }

    public void setTargetId (int id){
        this.targetId.add(id);
    }

    public void setTimestamp (Long timestmp){
        this.timestamp.add(timestmp);
    }

    public void setEventType (String type){
        this.eventType.add(type);
    }

    public void setXTouch (Float x){
        this.xTouch.add(x);
    }

    public void setYTouch (Float y){
        this.yTouch.add(y);
    }

    public void setXTarget (Float x){
        this.xTarget.add(x);
    }

    public void setYTarget (Float y){
        this.yTarget.add(y);
    }

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


    public ArrayList<String> getUserId (){
        return this.userId;
    }

    public ArrayList<Integer> getTargetId (){
        return this.targetId;
    }

    public ArrayList<Long> getTimestamp () {
       return this.timestamp;
    }

    public ArrayList<String> getEventType () {
        return this.eventType;
    }

    public ArrayList<Float> getXTouch (){
        return this.xTouch;
    }

    public ArrayList<Float> getYTouch () {
        return this.yTouch;
    }

    public ArrayList<Float> getXTarget (){
        return this.xTarget;
    }

    public ArrayList<Float> getYTarget (){
        return this.yTarget;
    }

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

    public int length() {
        return userId.size();
    }

}
