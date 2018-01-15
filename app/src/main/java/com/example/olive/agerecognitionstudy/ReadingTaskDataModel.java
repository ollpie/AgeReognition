package com.example.olive.agerecognitionstudy;

import java.util.ArrayList;

/**
 * Created by olive on 15.01.2018.
 */

public class ReadingTaskDataModel {
    String participantId;
    ArrayList<String> eventType;
    ArrayList<Float> xTouch;
    ArrayList<Float> yTouch;
    ArrayList<Float> xViewport;
    ArrayList<Float> yViewport;
    ArrayList<String> font;
    ArrayList<Integer> fontSize;
    ArrayList<Float> touchPressure;
    ArrayList<Float> touchSize;
    ArrayList<Float> touchOrientation;
    ArrayList<Float> touchMajor;
    ArrayList<Float> touchMinor;
    ArrayList<Long> timestamp;

    public ReadingTaskDataModel () {
        this.eventType = new ArrayList<>();
        this.xTouch = new ArrayList<>();
        this.yTouch = new ArrayList<>();
        this.xViewport = new ArrayList<>();
        this.yViewport = new ArrayList<>();
        this.font = new ArrayList<>();
        this.fontSize = new ArrayList<>();
        this.touchPressure = new ArrayList<>();
        this.touchSize = new ArrayList<>();
        this.touchOrientation = new ArrayList<>();
        this.touchMajor = new ArrayList<>();
        this.touchMinor = new ArrayList<>();
        this.timestamp = new ArrayList<>();
    }

    public ReadingTaskDataModel (String participantID) {
        this.participantId = participantID;
        this.eventType = new ArrayList<>();
        this.xTouch = new ArrayList<>();
        this.yTouch = new ArrayList<>();
        this.xViewport = new ArrayList<>();
        this.yViewport = new ArrayList<>();
        this.font = new ArrayList<>();
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

    public void setxViewport(Float x) {
        this.xViewport.add(x);
    }

    public void setyViewport(Float y) {
        this.yViewport.add(y);
    }

    public void setFont(String fontType) {
        this.font.add(fontType);
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

    public ArrayList<Float> getxViewport() {
        return xViewport;
    }

    public ArrayList<Float> getyViewport() {
        return yViewport;
    }

    public ArrayList<String> getFont() {
        return font;
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