package com.example.olive.agerecognitionstudy;

import java.util.ArrayList;

/**
 * Created by olive on 15.01.2018.
 */

public class PinTaskDataModel {
    String participantId;
    ArrayList<String> participantList;
    ArrayList<String> pin;
    ArrayList<String> eventType;
    ArrayList<Integer> logicRepetition;
    ArrayList<Integer> actualRepetition;
    ArrayList<String> progress;
    ArrayList<Character> currentDigit;
    ArrayList<Character> actualDigit;
    ArrayList<String> sequenceCorrect;
    ArrayList<Float> xButtonCenter;
    ArrayList<Float> yButtonCenter;
    ArrayList<Float> xTouch;
    ArrayList<Float> yTouch;
    ArrayList<Float> touchPressure;
    ArrayList<Float> touchSize;
    ArrayList<Float> touchOrientation;
    ArrayList<Float> touchMajor;
    ArrayList<Float> touchMinor;
    ArrayList<Long> timestamp;

    public PinTaskDataModel (String participantID) {
        this.participantId = participantID;
        this.participantList = new ArrayList<>();
        this.pin = new ArrayList<>();
        this.eventType = new ArrayList<>();
        this.logicRepetition = new ArrayList<>();
        this.actualRepetition = new ArrayList<>();
        this.progress = new ArrayList<>();
        this.currentDigit = new ArrayList<>();
        this.actualDigit = new ArrayList<>();
        this.sequenceCorrect = new ArrayList<>();
        this.xButtonCenter = new ArrayList<>();
        this.yButtonCenter = new ArrayList<>();
        this.xTouch = new ArrayList<>();
        this.yTouch = new ArrayList<>();
        this.touchPressure = new ArrayList<>();
        this.touchSize = new ArrayList<>();
        this.touchOrientation = new ArrayList<>();
        this.touchMajor = new ArrayList<>();
        this.touchMinor = new ArrayList<>();
        this.timestamp = new ArrayList<>();
    }

    //SETTER
    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public void setPin(String pin) {
        this.pin.add(pin);
    }

    public void setEventType(String eventType) {
        this.eventType.add(eventType);
    }

    public void setLogicRepetition(Integer logicRepetition) {
        this.logicRepetition.add(logicRepetition);
    }

    public void setActualRepetition(Integer actualRepetition) {
        this.actualRepetition.add(actualRepetition);
    }

    public void setProgress(String progress) {
        this.progress.add(progress);
    }

    public void setCurrentDigit(Character currentDigit) {
        this.currentDigit.add(currentDigit);
    }

    public void setActualDigit(Character actualDigit) {
        this.actualDigit.add(actualDigit);
    }

    public void setSequenceCorrect(String value) {
        this.sequenceCorrect.add(value);
    }

    public void setxButtonCenter(Float xButtonCenter) {
        this.xButtonCenter.add(xButtonCenter);
    }

    public void setyButtonCenter(Float yButtonCenter) {
        this.yButtonCenter.add(yButtonCenter);
    }

    public void setxTouch(Float xTouch) {
        this.xTouch.add(xTouch);
    }

    public void setyTouch(Float yTouch) {
        this.yTouch.add(yTouch);
    }

    public void setTouchPressure(Float touchPressure) {
        this.touchPressure.add(touchPressure);
    }

    public void setTouchSize(Float touchSize) {
        this.touchSize.add(touchSize);
    }

    public void setTouchOrientation(Float touchOrientation) {
        this.touchOrientation.add(touchOrientation);
    }

    public void setTouchMajor(Float touchMajor) {
        this.touchMajor.add(touchMajor);
    }

    public void setTouchMinor(Float touchMinor) {
        this.touchMinor.add(touchMinor);
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp.add(timestamp);
    }

    //GETTER
    public ArrayList<Long> getTimestamp() {
        return timestamp;
    }

    public String getParticipantId() {
        return participantId;
    }

    public ArrayList<String> getPin() {
        return pin;
    }

    public ArrayList<String> getEventType() {
        return eventType;
    }

    public ArrayList<Integer> getLogicRepetition() {
        return logicRepetition;
    }

    public ArrayList<Integer> getActualRepetition() {
        return actualRepetition;
    }

    public ArrayList<String> getProgress() {
        return progress;
    }

    public ArrayList<Character> getCurrentDigit() {
        return currentDigit;
    }

    public ArrayList<Character> getActualDigit() {
        return actualDigit;
    }

    public ArrayList<String> getSequenceCorrect() {
        return sequenceCorrect;
    }

    public ArrayList<Float> getxButtonCenter() {
        return xButtonCenter;
    }

    public ArrayList<Float> getyButtonCenter() {
        return yButtonCenter;
    }

    public ArrayList<Float> getxTouch() {
        return xTouch;
    }

    public ArrayList<Float> getyTouch() {
        return yTouch;
    }

    public ArrayList<Float> getTouchPressure() {
        return touchPressure;
    }

    public ArrayList<Float> getTouchSize() {
        return touchSize;
    }

    public ArrayList<Float> getTouchOrientation() {
        return touchOrientation;
    }

    public ArrayList<Float> getTouchMajor() {
        return touchMajor;
    }

    public ArrayList<Float> getTouchMinor() {
        return touchMinor;
    }

    public int length() {
        return pin.size();
    }
}
