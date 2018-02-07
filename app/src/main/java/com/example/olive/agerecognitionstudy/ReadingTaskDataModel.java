package com.example.olive.agerecognitionstudy;

import java.util.ArrayList;

/**
 * Created by olive on 15.01.2018.
 */

public class ReadingTaskDataModel {
    String participantId;
    ArrayList<String> participantIDList;
    ArrayList<String> eventType;
    ArrayList<Float> xTouch;
    ArrayList<Float> yTouch;
    ArrayList<Float> xViewport;
    ArrayList<Float> yViewport;
    ArrayList<Integer> text;
    ArrayList<Integer> fontSize;
    ArrayList<Float> touchPressure;
    ArrayList<Float> touchSize;
    ArrayList<Float> touchOrientation;
    ArrayList<Float> touchMajor;
    ArrayList<Float> touchMinor;
    ArrayList<Long> timestamp;

    public ReadingTaskDataModel (String participantID) {
        this.participantId = participantID;
        this.participantIDList = new ArrayList<>();
        this.eventType = new ArrayList<>();
        this.xTouch = new ArrayList<>();
        this.yTouch = new ArrayList<>();
        this.xViewport = new ArrayList<>();
        this.yViewport = new ArrayList<>();
        this.text = new ArrayList<>();
        this.fontSize = new ArrayList<>();
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

    public void setEventType(String eventType) {
        this.eventType.add(eventType);
    }

    public void setyViewportTop(Float x) {
        this.xViewport.add(x);
    }

    public void setyViewportBottom(Float y) {
        this.yViewport.add(y);
    }

    public void setText(int fontType) {
        this.text.add(fontType);
    }

    public void setFontSize(int size) {
        this.fontSize.add(size);
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
    public String getParticipantId() {
        return participantId;
    }

    public ArrayList<String> getEventType() {
        return eventType;
    }

    public ArrayList<Float> getxTouch() {
        return xTouch;
    }

    public ArrayList<Float> getyTouch() {
        return yTouch;
    }

    public ArrayList<Float> getyViewport() {
        return xViewport;
    }

    public ArrayList<Float> getyViewportBottom() {
        return yViewport;
    }

    public ArrayList<Integer> getText() {
        return text;
    }

    public ArrayList<Integer> getFontSize() {
        return fontSize;
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

    public ArrayList<Long> getTimestamp() {
        return timestamp;
    }

    public int length() {
        return eventType.size();
    }
}
