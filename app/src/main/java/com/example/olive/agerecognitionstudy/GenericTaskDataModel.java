package com.example.olive.agerecognitionstudy;

import java.util.ArrayList;

/**
 * Created by olive on 05.01.2018.
 */

public class GenericTaskDataModel {

    String participantId;
    ArrayList<String> participantList;
    ArrayList<Integer> targetId;
    ArrayList<String> eventType;
    ArrayList<Float> xTarget;
    ArrayList<Float> yTarget;
    ArrayList<Float> xTouch;
    ArrayList<Float> yTouch;
    ArrayList<Float> touchPressure;
    ArrayList<Float> touchSize;
    ArrayList<Float> touchOrientation;
    ArrayList<Float> touchMajor;
    ArrayList<Float> touchMinor;
    ArrayList<Long> timestamp;

    public GenericTaskDataModel () {
        this.participantList = new ArrayList<>();
        this.targetId = new ArrayList<>();
        this.eventType = new ArrayList<>();
        this.xTarget = new ArrayList<>();
        this.yTarget = new ArrayList<>();
        this.xTouch = new ArrayList<>();
        this.yTouch = new ArrayList<>();
        this.touchPressure = new ArrayList<>();
        this.touchSize = new ArrayList<>();
        this.touchOrientation = new ArrayList<>();
        this.touchMajor = new ArrayList<>();
        this.touchMinor = new ArrayList<>();
        this.timestamp = new ArrayList<>();
    }

    public GenericTaskDataModel (String participantID) {
        this.participantList = new ArrayList<>();
        this.participantId = participantID;
        this.targetId = new ArrayList<>();
        this.eventType = new ArrayList<>();
        this.xTarget = new ArrayList<>();
        this.yTarget = new ArrayList<>();
        this.xTouch = new ArrayList<>();
        this.yTouch = new ArrayList<>();
        this.touchPressure = new ArrayList<>();
        this.touchSize = new ArrayList<>();
        this.touchOrientation = new ArrayList<>();
        this.touchMajor = new ArrayList<>();
        this.touchMinor = new ArrayList<>();
        this.timestamp = new ArrayList<>();
    }

    //Setter Methods
    public void setParticipantId(String id){
        this.participantId = id;
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

    public void setTouchPressure (Float pressure){
        this.touchPressure.add(pressure);
    }

    public void setTouchSize (Float size){
        this.touchSize.add(size);
    }

    public void setTouchOrientation (Float orientation){
        this.touchOrientation.add(orientation);
    }

    public void setTouchMajor (Float major){
        this.touchMajor.add(major);
    }

    public void setTouchMinor (Float minor){
        this.touchMinor.add(minor);
    }

    public void setParticipantList (String participant){
        this.participantList.add(participant);
    }


    //Getter Methods
    public String getParticipantId(){
        return this.participantId;
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

    public ArrayList<Float> getTouchPressure () {
        return this.touchPressure;
    }

    public ArrayList<Float> getTouchSize (){
        return this.touchSize;
    }

    public ArrayList<Float> getTouchOrientation () {
        return this.touchOrientation;
    }

    public ArrayList<Float> getTouchMajor (){
        return this.touchMajor;
    }

    public ArrayList<Float> getTouchMinor (){
        return this.touchMinor;
    }

    public ArrayList<String> getParticipantList (){
        return this.participantList;
    }


    public int length() {
        return targetId.size();
    }
}
