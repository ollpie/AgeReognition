package com.example.olive.agerecognitionstudy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.PatternLockView.Dot;
import com.andrognito.patternlockview.PatternLockView.PatternViewMode;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.andrognito.patternlockview.utils.ResourceUtils;

import java.util.List;

//, "03412", "24630", "01247"
public class UnlockActivityTask extends AppCompatActivity {

    private static final String TASK_ID = "Unlock Pattern";
    private static final int DOT_MATRIX_DIMENSION = 3;
    private static final String[] PINS = {"15476", "25873"};
    private static final int CORRECT_REPETITIONS = 2;

    private MotionSensorUtil motionSensorUtil;
    private ImageView cross;
    private TextView patternHint;
    private String userID;
    private DatabaseHandler database;
    private UnlockTaskDataModel unlockDataModel;
    private String patternHintFirstPart;
    private String patternHintSecondPart;

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

    private PatternLockView mPatternLockView;
    private PatternLockViewListener mPatternLockViewListener;
    private LatinSquareUtil latinSquareUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
        latinSquareUtil = MainActivity.latinSquareUtil;
        userID = MainActivity.currentUserID;
        database = MainActivity.getDbHandler();
        unlockDataModel = new UnlockTaskDataModel(userID);
        setupPatternLockViewListener();
        setupPatternUnlock();
        motionSensorUtil = new MotionSensorUtil(userID, TASK_ID, (SensorManager) getSystemService(SENSOR_SERVICE));
    }

    private void setupUI(){
        setContentView(R.layout.activity_unlock_task);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        cross = findViewById(R.id.cross);
        patternHint = findViewById(R.id.pattern_hint);
        Resources res = UnlockActivityTask.this.getResources();
        patternHintFirstPart = res.getString(R.string.pattern_hint);
        patternHintSecondPart = res.getString(R.string.pin_counter);
        patternHint.setText(patternHintFirstPart + patternHintSecondPart + " " + (CORRECT_REPETITIONS-logicRepetitionCount) + " mal eingeben");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        calculateDotPositions();
        if (hasFocus) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    OnTouchListener listener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            int eventAction = event.getAction();

            if (progressBegan && sequenceCorrect) {
                xTarget = dotPositions[currentPattern.get(target-1).getColumn()][currentPattern.get(target-1).getRow()].x;
                yTarget = dotPositions[currentPattern.get(target-1).getColumn()][currentPattern.get(target-1).getRow()].y;
            }else {
                xTarget = dotPositions[currentPattern.get(target).getColumn()][currentPattern.get(target).getRow()].x;
                yTarget = dotPositions[currentPattern.get(target).getColumn()][currentPattern.get(target).getRow()].y;
            }
            //cross.setX(xTarget);
            //cross.setY(yTarget);
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
                    break;

                case MotionEvent.ACTION_UP:
                    writeDataIntoLists("Up");
                    break;
                default:
                    break;
            }
            return false;
        }
    };

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

    private void setupPatternUnlock () {
        mPatternLockView = findViewById(R.id.pattern_lock_view);
        mPatternLockView.setDotCount(3);
        mPatternLockView.setDotNormalSize((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_dot_size));
        mPatternLockView.setDotSelectedSize((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_dot_selected_size));
        mPatternLockView.setPathWidth((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_path_width));
        mPatternLockView.setDotAnimationDuration(10);
        mPatternLockView.setPathEndAnimationDuration(10);
        mPatternLockView.setInStealthMode(false);
        mPatternLockView.setInputEnabled(true);
        mPatternLockView.addPatternLockListener(mPatternLockViewListener);
        mPatternLockView.setOnTouchListener(listener);
        dotProgress = PatternLockUtils.stringToPattern(mPatternLockView, PINS[0]);
        currentPattern = PatternLockUtils.stringToPattern(mPatternLockView, PINS[0]);
        mPatternLockView.setPattern(PatternViewMode.AUTO_DRAW, PatternLockUtils.stringToPattern(mPatternLockView, PINS[pinIndex]));
    }

    void setupPatternLockViewListener() {
        mPatternLockViewListener = new PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<Dot> progressPattern) {
                progress = PatternLockUtils.patternToString(mPatternLockView, progressPattern);
                dotProgress = progressPattern;
                if (dotProgress.size()<PINS[0].length()) {
                    if (sequenceCorrect && (dotProgress.get(dotProgress.size() - 1) == PatternLockUtils.stringToPattern(mPatternLockView, PINS[pinIndex]).get(target))) {
                        target++;
                    } else {
                        sequenceCorrect = false;
                    }
                    progressBegan = true;
                }
            }

            @Override
            public void onComplete(List<Dot> pattern) {
                target = 0;
                if (PatternLockUtils.patternToString(mPatternLockView, pattern).equals(PINS[pinIndex])) {
                    if ((PatternLockUtils.patternToString(mPatternLockView, pattern).equals(PINS[PINS.length-1])) && (logicRepetitionCount == CORRECT_REPETITIONS-1)){
                        logicRepetitionCount = 0;
                        pinIndex = 0;
                        progress = "";
                        UnlockActivityTask.AsyncTaskRunner runner = new UnlockActivityTask.AsyncTaskRunner();
                        runner.execute();
                    }
                    mPatternLockView.clearPattern();
                    if (logicRepetitionCount == CORRECT_REPETITIONS-1){
                        logicRepetitionCount = 0;
                        actualRepetitionCount = 0;
                        pinIndex++;
                        progress = "";
                    } else {
                        logicRepetitionCount++;
                        progress = "";
                    }
                    Toast.makeText(getApplication(),
                            "Eingabe korrekt.", Toast.LENGTH_SHORT).show();
                    patternHint.setText(patternHintFirstPart + patternHintSecondPart + " " + (CORRECT_REPETITIONS-logicRepetitionCount) + " mal eingeben");
                } else {
                    progress = "";
                    sequenceCorrect = false;
                    Toast.makeText(getApplication(),
                            "Eingabe falsch.", Toast.LENGTH_SHORT).show();
                }

                actualRepetitionCount++;
                setSequenceCorrectness();
                mPatternLockView.setPattern(PatternViewMode.AUTO_DRAW, PatternLockUtils.stringToPattern(mPatternLockView, PINS[pinIndex]));
                dotProgress = PatternLockUtils.stringToPattern(mPatternLockView, PINS[pinIndex]);
                currentPattern = PatternLockUtils.stringToPattern(mPatternLockView, PINS[pinIndex]);
                sequenceCorrect = true;
                progressBegan = false;
            }
            @Override
            public void onCleared() {
                //Log.d(getClass().getName(), "Pattern has been cleared");
            }
        };
    }

    private void calculateDotPositions(){
        dotPositions = new Point[DOT_MATRIX_DIMENSION][DOT_MATRIX_DIMENSION];
        for (float i = 1.0f; i < DOT_MATRIX_DIMENSION+1; i++){
            for (float j = 1.0f; j < DOT_MATRIX_DIMENSION+1; j++){
                float xPos = mPatternLockView.getX() + (mPatternLockView.getWidth() * (i/DOT_MATRIX_DIMENSION)) - ((1.0f/DOT_MATRIX_DIMENSION) * mPatternLockView.getWidth()/2);
                float yPos = mPatternLockView.getY() + (mPatternLockView.getHeight() * (j/DOT_MATRIX_DIMENSION)) - ((1.0f/DOT_MATRIX_DIMENSION) * mPatternLockView.getHeight()/2);
                dotPositions[(int) i-1][(int) j-1] = new Point((int) xPos, (int) yPos);
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

    private void startIntent(){
        int activity = latinSquareUtil.getNext();
        Intent intent;
        switch (activity){
            case 0:
                intent = new Intent(this, GenericTaskActivity.class);
                startActivity(intent);
                break;
            case 1:
                intent = new Intent(this, PinTaskActivity.class);
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(this, UnlockActivityTask.class);
                startActivity(intent);
                break;
            case 3:
                intent = new Intent(this, ReadingTaskActivity.class);
                startActivity(intent);
                break;
            default:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {

    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            try {
                database.createUnlockTaskData(unlockDataModel);
                database.createMotionSensorData(motionSensorUtil.getMotionSensorData());
                database.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String params) {
            progressDialog.dismiss();
            startIntent();
        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(UnlockActivityTask.this,
                    "Sichere Daten...", "Bitte warten.");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        motionSensorUtil.registerListeners();
    }

    @Override
    protected void onPause() {
        super.onPause();
        motionSensorUtil.stop();
    }
}
