package com.example.olive.agerecognitionstudy;

import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by olive on 19.01.2018.
 */

public class ReadingTaskLogic {

    private static final int TEXT_SIZE = 9;

    public static boolean trainigsMode = true;
    private DatabaseHandler database;
    private ReadingTaskDataModel readingTaskModel;
    public static String userID;
    private float yViewPort;
    private float yViewPortBottom;
    private float xTouch = 0.0F;
    private float yTouch = 0.0F;
    private float touchPressure = 0.0F;
    private float touchSize = 0.0F;
    private float touchOrientation = 0.0F;
    private float touchMajor = 0.0F;
    private float touchMinor = 0.0F;
    private long timestamp;
    public static LatinSquareUtil latinSquareUtil;
    public static int textCount = 0;

    public ReadingTaskLogic() {
        latinSquareUtil = MainActivity.latinSquareUtil;
        userID = MainActivity.currentUserID;
        database = MainActivity.getDbHandler();
        readingTaskModel = new ReadingTaskDataModel(userID);
    }

    public void handleTouch(MotionEvent event, ScrollView scrollView, int displayHeight){
        if (!trainigsMode) {
            int eventAction = event.getAction();
            yViewPort = scrollView.getScrollY();
            yViewPortBottom = yViewPort + displayHeight - MainActivity.statusbarOffset;
            xTouch = event.getX();
            yTouch = event.getY() - MainActivity.statusbarOffset;
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
        readingTaskModel.setParticipantId(userID);
        readingTaskModel.setEventType(eventType);
        readingTaskModel.setyViewportTop(yViewPort);
        readingTaskModel.setyViewportBottom(yViewPortBottom);
        readingTaskModel.setText(textCount);
        readingTaskModel.setFontSize(TEXT_SIZE);
        readingTaskModel.setxTouch(xTouch);
        readingTaskModel.setyTouch(yTouch);
        readingTaskModel.setTouchPressure(touchPressure);
        readingTaskModel.setTouchSize(touchSize);
        readingTaskModel.setTouchOrientation(touchOrientation);
        readingTaskModel.setTouchMajor(touchMajor);
        readingTaskModel.setTouchMinor(touchMinor);
        readingTaskModel.setTimestamp(timestamp);
    }

    public void writeReadingData(MotionSensorDataModel motionData){
        database.createReadingTaskData(readingTaskModel);
        database.createMotionSensorData(motionData);
        database.close();
    }

    public ReadingTaskDataModel getReadingTaskModel(){
        return readingTaskModel;
    }
}
