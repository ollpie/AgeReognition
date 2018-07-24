package com.example.olive.agerecognitionstudy;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * Created by olive on 16.07.2018.
 */

public class PinTaskLogic {

    private static final int REPETITIONS = 1;
    private static final int TRAININGS_REPETITIONS = 2;
    private static final String[] PINS = {"0537", "8683", "5465", "0954", "1243"};
    private static final String[] TRAININGS_PINS = {"7530", "1234"};

    private int logicRepetitionCount = 0;
    private int actualRepetitionCount = 0;
    private int currentIndexCount = 0;
    private String receivedDigits = "";
    private int pinIndex = 0;
    private float xTarget;
    private float yTarget;
    private float xTouch = 0.0F;
    private float yTouch = 0.0F;
    private float touchPressure = 0.0F;
    private float touchSize = 0.0F;
    private float touchOrientation = 0.0F;
    private float touchMajor = 0.0F;
    private float touchMinor = 0.0F;
    private long timestamp;

    static String userID = "";
    private char actualDigit;
    private char currentDigit;
    private boolean sequenceCorrect = true;
    private int formerSize = 0;
    private boolean writeSequence = false;
    private boolean end = false;
    private int deleteID;
    private int doneID;

    DatabaseHandler database;
    static LatinSquareUtil latinSquareUtil;

    private boolean trainingsMode = true;
    private int trainingsCount = 0;
    private int trainingsIndex = 0;

    private PinTaskDataModel pinTaskDataModel;
    private PinTaskActivity activity;

    public PinTaskLogic(PinTaskActivity activity) {
        latinSquareUtil = MainActivity.latinSquareUtil;
        userID = MainActivity.currentUserID;
        database = MainActivity.getDbHandler();
        pinTaskDataModel = new PinTaskDataModel(userID);
        currentDigit = PINS[pinIndex].charAt(0);
        deleteID = R.id.buttonDelete;
        doneID = R.id.buttonDone;
        this.activity = activity;
        this.activity.onUpdatePinDisplay("Pin: " + TRAININGS_PINS[trainingsIndex] + this.activity.pinFillerString + " " + (TRAININGS_REPETITIONS-trainingsCount) + " mal eingeben");
    }

    public void handleTouch(View view, MotionEvent event, float x, float y){
        int eventAction = event.getAction();
        Button b = (Button) view;
        if (!trainingsMode) {
            xTarget = (x + b.getX() + b.getWidth() / 2);
            yTarget = (y + b.getY() + b.getHeight() / 2);
            xTouch = event.getRawX();
            yTouch = event.getRawY() - MainActivity.statusbarOffset;
            touchPressure = event.getPressure();
            touchSize = event.getSize();
            touchOrientation = event.getOrientation();
            touchMajor = event.getTouchMajor();
            touchMinor = event.getTouchMinor();
            timestamp = event.getEventTime();
            switch (eventAction) {
                case MotionEvent.ACTION_DOWN:
                    processButtonPress(b);
                    writeDataIntoLists("Down");
                    break;
                case MotionEvent.ACTION_MOVE:
                    writeDataIntoLists("Move");
                    break;
                case MotionEvent.ACTION_UP:
                    writeDataIntoLists("Up");
                    if (writeSequence) {
                        setSequenceCorrectness();
                        sequenceCorrect = true;
                        writeSequence = false;
                    }
                    if (end) {
                        activity.onUpdatePinView("FERTIG");
                        activity.startAsyncTask();
                    }
                    break;
                default:
                    break;
            }
        }else{
            if (eventAction == MotionEvent.ACTION_DOWN){
                if (b.getId() == doneID){
                    activity.onUpdatePinView("");
                    receivedDigits = "";

                    if (trainingsCount == TRAININGS_REPETITIONS-1){
                        if(TRAININGS_PINS.length-1 == trainingsIndex){
                            trainingsIndex = 0;
                        }else {
                            trainingsIndex++;
                        }
                        trainingsCount = 0;
                    }else {
                        trainingsCount++;
                    }
                    activity.onUpdatePinDisplay("Pin: " + TRAININGS_PINS[trainingsIndex] + activity.pinFillerString + " " + (TRAININGS_REPETITIONS-trainingsCount) + " mal eingeben");
                }else{
                    if (b.getId() == deleteID && !receivedDigits.equals("")){
                        receivedDigits = receivedDigits.substring(0, receivedDigits.length()-1);
                        activity.onUpdatePinView(receivedDigits);
                    }else {
                        receivedDigits += b.getText();
                        activity.onUpdatePinView(receivedDigits);
                    }
                }
            }
        }
    }

    private void processButtonPress(Button b) {
        if (!(receivedDigits.length() > PINS[0].length()-1) || b.getId() == deleteID || b.getId() == doneID){
            if (b.getId() != doneID && b.getId() != deleteID){
                currentDigit = PINS[pinIndex].charAt(currentIndexCount);
            }
            if (b.getId() == deleteID && !receivedDigits.equals("")){
                receivedDigits = receivedDigits.substring(0, receivedDigits.length()-1);
                activity.onUpdatePinView(receivedDigits);
                actualDigit = 'D';
                currentDigit = 'D';
                currentIndexCount--;
                sequenceCorrect = false;
            }
            if (b.getId() == doneID) {
                if (receivedDigits.equals(PINS[PINS.length-1]) && logicRepetitionCount == REPETITIONS-1 && sequenceCorrect) {
                    writeSequence = true;
                    end = true;
                }
                if (receivedDigits.equals(PINS[pinIndex])){
                    actualRepetitionCount++;
                    if (sequenceCorrect) {
                        logicRepetitionCount++;
                    }
                    if (logicRepetitionCount == REPETITIONS){
                        logicRepetitionCount = 0;
                        actualRepetitionCount = 0;
                        if (pinIndex != PINS.length - 1) {
                            pinIndex++;
                        }
                    }
                    writeSequence = true;
                    currentIndexCount = 0;
                    receivedDigits = "";
                    currentDigit = 'd';
                    actualDigit = 'd';
                    activity.onUpdatePinView(receivedDigits);
                    activity.onUpdatePinDisplay("Pin: " + PINS[pinIndex] + activity.pinFillerString + " " + (REPETITIONS-logicRepetitionCount) + " mal eingeben");
                    activity.makeToast("Eingabe korrekt.");
                }else{
                    actualRepetitionCount++;
                    sequenceCorrect = false;
                    writeSequence = true;
                    currentIndexCount = 0;
                    receivedDigits = "";
                    currentDigit = 'd';
                    actualDigit = 'd';
                    activity.onUpdatePinView(receivedDigits);
                    activity.makeToast("Eingabe falsch.");
                }

            }else{
                if(b.getId() != deleteID && b.getId() != doneID){
                    currentIndexCount++;
                    receivedDigits += b.getText();
                    actualDigit = b.getText().charAt(0);
                    activity.onUpdatePinView(receivedDigits);
                }
            }
        }
    }

    public void endTraining(){
        receivedDigits = "";
        activity.onUpdatePinView(receivedDigits);
        activity.onUpdatePinDisplay("Pin: " + PINS[pinIndex] + activity.pinFillerString + " " + (REPETITIONS-logicRepetitionCount) + " mal eingeben");
        trainingsMode = false;
    }

    private void setSequenceCorrectness(){
        int size = pinTaskDataModel.pin.size();
        for (int i = 0; i < size-formerSize; i++){
            if(!sequenceCorrect){
                pinTaskDataModel.setSequenceCorrect("false");
            }else {
                pinTaskDataModel.setSequenceCorrect("true");
            }
        }
        formerSize = size;
    }

    private void writeDataIntoLists(String eventType) {
        pinTaskDataModel.setParticipantId(userID);
        pinTaskDataModel.setPin(PINS[pinIndex]);
        pinTaskDataModel.setEventType(eventType);
        pinTaskDataModel.setLogicRepetition(logicRepetitionCount);
        pinTaskDataModel.setActualRepetition(actualRepetitionCount);
        pinTaskDataModel.setProgress(receivedDigits);
        pinTaskDataModel.setCurrentDigit(currentDigit);
        pinTaskDataModel.setActualDigit(actualDigit);
        pinTaskDataModel.setxButtonCenter(xTarget);
        pinTaskDataModel.setyButtonCenter(yTarget);
        pinTaskDataModel.setxTouch(xTouch);
        pinTaskDataModel.setyTouch(yTouch);
        pinTaskDataModel.setTouchPressure(touchPressure);
        pinTaskDataModel.setTouchSize(touchSize);
        pinTaskDataModel.setTouchOrientation(touchOrientation);
        pinTaskDataModel.setTouchMajor(touchMajor);
        pinTaskDataModel.setTouchMinor(touchMinor);
        pinTaskDataModel.setTimestamp(timestamp);
    }

    public void writePinTaskData(MotionSensorDataModel motionData){
        database.createPinTaskData(pinTaskDataModel);
        database.createMotionSensorData(motionData);
        database.close();
    }
}
