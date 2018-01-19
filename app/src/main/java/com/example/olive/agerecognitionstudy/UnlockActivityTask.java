package com.example.olive.agerecognitionstudy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.PatternLockView.Dot;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.ResourceUtils;

import java.util.List;

public class UnlockActivityTask extends AppCompatActivity {

    private static final String TASK_ID = "Unlock Pattern";

    private MotionSensorUtil motionSensorUtil;
    private ImageView cross;
    private String userID;
    private DatabaseHandler database;
    private UnlockTaskDataModel unlockDataModel;

    private int repetitionCount = 0;
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

    private PatternLockView mPatternLockView;
    private PatternLockViewListener mPatternLockViewListener = new PatternLockViewListener() {
        @Override
        public void onStarted() {
            //Log.d(getClass().getName(), "Pattern drawing started");
        }

        @Override
        public void onProgress(List<Dot> progressPattern) {
            /*Log.d(getClass().getName(), "Pattern progress: " +
                    PatternLockUtils.patternToString(mPatternLockView, progressPattern));*/
            Dot d = progressPattern.get(0);
            Log.d("Dot", "Row: " + String.valueOf(d.getRow()) + ", Column: " + String.valueOf(d.getColumn()));
        }

        @Override
        public void onComplete(List<Dot> pattern) {
            /*Log.d(getClass().getName(), "Pattern complete: " +
                    PatternLockUtils.patternToString(mPatternLockView, pattern));*/
        }

        @Override
        public void onCleared() {
            //Log.d(getClass().getName(), "Pattern has been cleared");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock_task);
        cross = findViewById(R.id.cross);
        userID = MainActivity.currentUserID;
        database = MainActivity.getDbHandler();
        unlockDataModel = new UnlockTaskDataModel(userID);
        setupPatternUnlock();
        //motionSensorUtil = new MotionSensorUtil(userID, TASK_ID, (SensorManager) getSystemService(SENSOR_SERVICE));
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();
        xTouch = event.getRawX();
        yTouch = event.getRawY()-MainActivity.statusbarOffset;
        touchPressure = event.getPressure();
        touchSize = event.getSize();
        touchOrientation = event.getOrientation();
        touchMajor = event.getTouchMajor();
        touchMinor = event.getTouchMinor();
        timestamp = event.getEventTime();
        switch(eventAction) {
            case MotionEvent.ACTION_DOWN:
                writeDataIntoLists("Down");
                break;

            case MotionEvent.ACTION_MOVE:
                writeDataIntoLists("Move");
                cross.setX(event.getX()- 13);
                cross.setY(event.getY() - MainActivity.statusbarOffset -13);
                break;

            case MotionEvent.ACTION_UP:
                writeDataIntoLists("Up");
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    private void writeDataIntoLists(String eventType) {
        unlockDataModel.setParticipantId(userID);
        unlockDataModel.setPattern("");
        unlockDataModel.setEventType(eventType);
        unlockDataModel.setRepetition(repetitionCount);
        unlockDataModel.setProgress(progress);
        unlockDataModel.setSequenceCorrect("true");
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

    private void setupPatternUnlock () {
        mPatternLockView = findViewById(R.id.pattern_lock_view);
        mPatternLockView.setDotCount(3);
        mPatternLockView.setDotNormalSize((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_dot_size));
        mPatternLockView.setDotSelectedSize((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_dot_selected_size));
        mPatternLockView.setPathWidth((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_path_width));
        mPatternLockView.setAspectRatioEnabled(true);
        mPatternLockView.setAspectRatio(PatternLockView.AspectRatio.ASPECT_RATIO_HEIGHT_BIAS);
        mPatternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
        mPatternLockView.setDotAnimationDuration(150);
        mPatternLockView.setPathEndAnimationDuration(100);
        mPatternLockView.setCorrectStateColor(ResourceUtils.getColor(this, R.color.pomegranate));
        mPatternLockView.setInStealthMode(false);
        mPatternLockView.setTactileFeedbackEnabled(true);
        mPatternLockView.setInputEnabled(true);
        mPatternLockView.addPatternLockListener(mPatternLockViewListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //motionSensorUtil.registerListeners();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //motionSensorUtil.stop();
    }
}
