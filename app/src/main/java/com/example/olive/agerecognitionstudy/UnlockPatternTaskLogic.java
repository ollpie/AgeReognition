package com.example.olive.agerecognitionstudy;

import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.PatternLockView.Dot;
import com.andrognito.patternlockview.PatternLockView.PatternViewMode;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.util.List;

/**
 * Created by olive on 16.07.2018.
 */

public class UnlockPatternTaskLogic {

    private static final int DOT_MATRIX_DIMENSION = 3;
    private static final String[] PINS = {"15476", "25873", "03412", "24630", "01247"};
    private static final String[] TRAININGS_PINS = {"01258", "048"};
    private static final int CORRECT_REPETITIONS = 1;
    private static final int TRAININGS_REPETITIONS = 2;

    static String userID;
    private DatabaseHandler database;
    private UnlockTaskDataModel unlockDataModel;
    private PatternLockView mPatternLockView;
    private int logicRepetitionCount = 0;
    private int actualRepetitionCount = 0;
    private String progress = "";
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
    private int pinIndex = 0;
    private int target = 0;
    private List<Dot> dotProgress;
    private List<Dot> currentPattern;
    private boolean sequenceCorrect = true;
    private boolean progressBegan = false;
    private int formerSize = 0;
    private Point[][] dotPositions;
    static LatinSquareUtil latinSquareUtil;
    static boolean trainingMode = true;
    private int trainingsCount = 0;
    private int trainingsIndex = 0;
    private UnlockActivityTask activity;

    public UnlockPatternTaskLogic(UnlockActivityTask activity){
        latinSquareUtil = MainActivity.latinSquareUtil;
        userID = MainActivity.currentUserID;
        database = MainActivity.getDbHandler();
        unlockDataModel = new UnlockTaskDataModel(userID);
        this.activity = activity;
    }

    public void handleTouch(View view, MotionEvent event){
        int eventAction = event.getAction();
        if(!trainingMode) {
            if (progressBegan && sequenceCorrect) {
                xTarget = dotPositions[currentPattern.get(target - 1).getColumn()][currentPattern.get(target - 1).getRow()].x;
                yTarget = dotPositions[currentPattern.get(target - 1).getColumn()][currentPattern.get(target - 1).getRow()].y;
            } else {
                xTarget = dotPositions[currentPattern.get(target).getColumn()][currentPattern.get(target).getRow()].x;
                yTarget = dotPositions[currentPattern.get(target).getColumn()][currentPattern.get(target).getRow()].y;
            }
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
                    writeDataIntoLists("Down");
                    break;
                case MotionEvent.ACTION_MOVE:
                    writeDataIntoLists("Move");
                    break;
                case MotionEvent.ACTION_UP:
                    writeDataIntoLists("Up");
                    break;
                default:
                    break;
            }
        }
    }

    private void writeDataIntoLists(String eventType) {
        unlockDataModel.setParticipantId(userID);
        unlockDataModel.setPattern(PINS[pinIndex]);
        unlockDataModel.setEventType(eventType);
        unlockDataModel.setLogicRepetition(logicRepetitionCount);
        unlockDataModel.setActualRepetition(actualRepetitionCount);
        unlockDataModel.setProgress(progress);
        unlockDataModel.setxButtonCenter(xTarget);
        unlockDataModel.setyButtonCenter(yTarget);
        unlockDataModel.setxTouch(xTouch);
        unlockDataModel.setyTouch(yTouch);
        unlockDataModel.setTouchPressure(touchPressure);
        unlockDataModel.setTouchSize(touchSize);
        unlockDataModel.setTouchOrientation(touchOrientation);
        unlockDataModel.setTouchMajor(touchMajor);
        unlockDataModel.setTouchMinor(touchMinor);
        unlockDataModel.setTimestamp(timestamp);
    }

    public void updateProgress(List<Dot> progressPattern){
        if(!trainingMode) {
            progress = PatternLockUtils.patternToString(mPatternLockView, progressPattern);
            dotProgress = progressPattern;
            if (dotProgress.size() < PINS[0].length()) {
                if (sequenceCorrect && (dotProgress.get(dotProgress.size() - 1) == PatternLockUtils.stringToPattern(mPatternLockView, PINS[pinIndex]).get(target))) {
                    target++;
                } else {
                    sequenceCorrect = false;
                }
                progressBegan = true;
            }
        }
    }

    public void onPatternComplete(List<Dot> pattern) {
        if (!trainingMode) {
            target = 0;
            actualRepetitionCount++;
            if (PatternLockUtils.patternToString(mPatternLockView, pattern).equals(PINS[pinIndex])) {
                if ((PatternLockUtils.patternToString(mPatternLockView, pattern).equals(PINS[PINS.length - 1])) && (logicRepetitionCount == CORRECT_REPETITIONS - 1)) {
                    logicRepetitionCount = 0;
                    pinIndex = 0;
                    progress = "";
                    activity.startAsyncTask();
                }
                mPatternLockView.clearPattern();
                if (logicRepetitionCount == CORRECT_REPETITIONS - 1) {
                    logicRepetitionCount = 0;
                    actualRepetitionCount = 0;
                    pinIndex++;
                    progress = "";
                } else {
                    logicRepetitionCount++;
                    progress = "";
                }
                activity.makeToast("Eingabe korrekt.");
                activity.onUpdatePatternHint(" " + (CORRECT_REPETITIONS - logicRepetitionCount) + " mal eingeben");
            } else {
                progress = "";
                sequenceCorrect = false;
                activity.makeToast("Eingabe falsch.");
            }
            setSequenceCorrectness();
            mPatternLockView.setPattern(PatternViewMode.AUTO_DRAW, PatternLockUtils.stringToPattern(mPatternLockView, PINS[pinIndex]));
            dotProgress = PatternLockUtils.stringToPattern(mPatternLockView, PINS[pinIndex]);
            currentPattern = PatternLockUtils.stringToPattern(mPatternLockView, PINS[pinIndex]);
            sequenceCorrect = true;
            progressBegan = false;
        }else{
            if (PatternLockUtils.patternToString(mPatternLockView, pattern).equals(TRAININGS_PINS[trainingsIndex])){
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
            }
            mPatternLockView.setPattern(PatternViewMode.AUTO_DRAW, PatternLockUtils.stringToPattern(mPatternLockView, TRAININGS_PINS[trainingsIndex]));
            activity.onUpdatePatternHint(" " + (TRAININGS_REPETITIONS - trainingsCount) + " mal eingeben");
        }
    }

    public void calculateDotPositions(){
        dotPositions = new Point[DOT_MATRIX_DIMENSION][DOT_MATRIX_DIMENSION];
        for (float i = 1.0f; i < DOT_MATRIX_DIMENSION+1; i++){
            for (float j = 1.0f; j < DOT_MATRIX_DIMENSION+1; j++){
                float xPos = mPatternLockView.getX() + (mPatternLockView.getWidth() * (i/DOT_MATRIX_DIMENSION)) - ((1.0f/DOT_MATRIX_DIMENSION) * mPatternLockView.getWidth()/2);
                float yPos = mPatternLockView.getY() + (mPatternLockView.getHeight() * (j/DOT_MATRIX_DIMENSION)) - ((1.0f/DOT_MATRIX_DIMENSION) * mPatternLockView.getHeight()/2);
                dotPositions[(int) i-1][(int) j-1] = new Point((int) xPos, (int) yPos);
                Log.d("Position", "Position "+ String.valueOf(i-1) + ": " + String.valueOf(dotPositions[(int) i-1][(int) j-1]));
            }
        }
    }

    private void setSequenceCorrectness(){
        int size = unlockDataModel.pattern.size();
        for (int i = 0; i < size-formerSize; i++){
            if(!sequenceCorrect){
                unlockDataModel.setSequenceCorrect("false");
            }else {
                unlockDataModel.setSequenceCorrect("true");
            }
        }
        formerSize = size;
    }

    public String getInitialHintMessage(){
        return " " + (TRAININGS_REPETITIONS - trainingsCount) + " mal eingeben";
    }

    public String getHintMessageAfterTraining(){
        return " " + (CORRECT_REPETITIONS-logicRepetitionCount) + " mal eingeben";
    }

    public String getPattern(){
        return PINS[pinIndex];
    }

    public String getTrainingPattern(){
        return TRAININGS_PINS[trainingsIndex];
    }

    public void setDotProgressAndPattern() {
        dotProgress = PatternLockUtils.stringToPattern(mPatternLockView, PINS[0]);
        currentPattern = PatternLockUtils.stringToPattern(mPatternLockView, PINS[0]);
    }

    public void setLockView(PatternLockView lockView){
        this.mPatternLockView = lockView;
    }

    public void writeUnlockPatternData(MotionSensorDataModel motionData){
        database.createUnlockTaskData(unlockDataModel);
        database.createMotionSensorData(motionData);
        database.close();
    }
}
