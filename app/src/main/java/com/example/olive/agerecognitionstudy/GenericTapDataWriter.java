package com.example.olive.agerecognitionstudy;

import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by olive on 17.06.2018.
 */

public class GenericTapDataWriter {

    private static final int MIN = 0;
    private static final int X_AMOUNT = 10;
    private static final int Y_AMOUNT = 15;
    private static final int PADDING = 220;

    private ImageView target;
    private TextView counterDisplay;
    private Display display;

    private Random randomX;
    private Random randomY;
    private int randomXValue;
    private int randomYValue;

    private float xTarget = 0.0F;
    private float yTarget = 0.0F;
    private float xTouch = 0.0F;
    private float yTouch = 0.0F;

    private float touchPressure = 0.0F;
    private float touchSize = 0.0F;
    private float touchOrientation = 0.0F;
    private float touchMajor = 0.0F;
    private float touchMinor = 0.0F;
    private long timestamp;

    private int touch_counter = 0;
    private int xOffset = 0;
    private int yOffset = 0;

    private float xPositions[];
    private float yPositions[];
    private int maxX;
    private int maxY;
    static LatinSquareUtil latinSquareUtil;
    private DatabaseHandler database;
    static String userID = "";
    private GenericTaskDataModel taskmodel;

    private boolean positionChecker[][] = new boolean[X_AMOUNT][Y_AMOUNT];
    private boolean trainingsMode = true;

    public GenericTapDataWriter(ImageView target, TextView counterDisplay, Display display){
        this.target = target;
        this.counterDisplay = counterDisplay;
        this.display = display;
        initPositions();
        initPositionChecker();
        latinSquareUtil = MainActivity.latinSquareUtil;
        maxX = xPositions.length-1;
        maxY = yPositions.length-1;
        userID = MainActivity.currentUserID;
        database = MainActivity.getDbHandler();
        taskmodel = new GenericTaskDataModel(userID);
    }

    public void handleTouch(MotionEvent event, View target) {
        int eventAction = event.getAction();
        if (!trainingsMode) {
            xTarget = target.getX();
            yTarget = target.getY();
            xTouch = event.getX();
            yTouch = event.getY();
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
                    targetClicked();
                    break;
                default:
                    break;
            }
        }else{
            if (eventAction == MotionEvent.ACTION_DOWN){

                randomXValue = randomX.nextInt(((maxX - MIN) +1)+MIN);
                randomYValue = randomY.nextInt((((maxY-3) - MIN) +1)+MIN);
                target.setX(xPositions[randomXValue]-xOffset);
                target.setY(yPositions[randomYValue]-yOffset);
            }
        }
    }

    public void targetClicked() {
        if (didTouchEveryPosition()){
            target.setVisibility(View.GONE);

        } else {
            randomXValue = randomX.nextInt(((maxX - MIN) +1)+MIN);
            randomYValue = randomY.nextInt(((maxY - MIN) +1)+MIN);
            if (positionChecker[randomXValue][randomYValue]){
                do {
                    randomXValue = randomX.nextInt(((maxX - MIN) +1)+MIN);
                    randomYValue = randomY.nextInt(((maxY - MIN) +1)+MIN);
                } while (positionChecker[randomXValue][randomYValue]);
            }
            positionChecker[randomXValue][randomYValue] = true;
            touch_counter++;
            Log.d("Positions", String.valueOf(touch_counter));
            target.setX(xPositions[randomXValue]-xOffset);
            target.setY(yPositions[randomYValue]-yOffset);
            Log.d("X, Y", String.valueOf(target.getX()) + ", " + String.valueOf(target.getY()));
        }
        counterDisplay.setText("Noch " + ((xPositions.length*yPositions.length)-touch_counter) + " mal tippen");
    }

    private void writeDataIntoLists(String eventType) {
        taskmodel.setParticipantId(userID);
        taskmodel.setTargetId(touch_counter);
        taskmodel.setTimestamp(timestamp);
        taskmodel.setXTarget(xTarget);
        taskmodel.setYTarget(yTarget);
        taskmodel.setXTouch(xTouch);
        taskmodel.setYTouch(yTouch);
        taskmodel.setTouchPressure(touchPressure);
        taskmodel.setTouchSize(touchSize);
        taskmodel.setTouchOrientation(touchOrientation);
        taskmodel.setTouchMajor(touchMajor);
        taskmodel.setTouchMinor(touchMinor);
        taskmodel.setEventType(eventType);
    }

    private void initPositions(){
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y-MainActivity.statusbarOffset;
        Log.d("Size", String.valueOf(width) + " x " + String.valueOf(height));
        xPositions = new float[X_AMOUNT];
        yPositions = new float[Y_AMOUNT];
        xPositions[0] = PADDING;
        yPositions[0] = PADDING;
        int xStep = (width-2*PADDING)/(X_AMOUNT-1);
        int yStep = (height-2*PADDING)/(Y_AMOUNT-1);
        Log.d("X Werte", String.valueOf(xPositions[0]));
        for (int i = 1; i<X_AMOUNT; i++) {
            xPositions[i] = xPositions[i - 1] + xStep;
            Log.d("X Werte", String.valueOf(xPositions[i]));
        }
        Log.d("Y Werte", String.valueOf(yPositions[0]));
        for (int i = 1; i<Y_AMOUNT; i++){
            yPositions[i] = yPositions[i-1] + yStep;
            Log.d("Y Werte", String.valueOf(yPositions[i]));
        }
    }

    private void initPositionChecker() {
        for (int i = 0; i<X_AMOUNT; i++){
            for (int j = 0; j<Y_AMOUNT; j++){
                positionChecker[i][j] = false;
            }
        }
    }

    private boolean didTouchEveryPosition() {
        for (int i = 0; i<X_AMOUNT; i++){
            for (int j = 0; j<Y_AMOUNT; j++){
                if (!positionChecker[i][j]){
                    return false;
                }
            }
        }
        return true;
    }

    public void windowFocusChanged(){
        xOffset = target.getWidth()/2;
        yOffset = target.getHeight()/2;

        randomX = new Random();
        randomY = new Random();
        randomXValue = randomX.nextInt(((maxX - MIN) +1)+MIN);
        randomYValue = randomY.nextInt((((maxY-2) - MIN) +1)+MIN);
        target.setX(xPositions[randomXValue]);
        target.setY(yPositions[randomYValue]);
    }

    public void endTraining(){
        randomXValue = randomX.nextInt(((maxX - MIN) +1)+MIN);
        randomYValue = randomY.nextInt(((maxY - MIN) +1)+MIN);
        target.setX(xPositions[randomXValue]);
        target.setY(yPositions[randomYValue]);
        positionChecker[randomXValue][randomYValue] = true;
        counterDisplay.setText("Noch " + ((xPositions.length*yPositions.length)-touch_counter) + " mal tippen");
        trainingsMode = false;
    }

}
