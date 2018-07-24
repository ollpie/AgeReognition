package com.example.olive.agerecognitionstudy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.PatternLockView.Dot;
import com.andrognito.patternlockview.PatternLockView.PatternViewMode;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.andrognito.patternlockview.utils.ResourceUtils;

import java.util.List;

//
public class UnlockActivityTask extends AppCompatActivity {

    private static final String TASK_ID = "Unlock Pattern";

    private MotionSensorUtil motionSensorUtil;
    private TextView patternHint;
    private PatternLockView mPatternLockView;
    private PatternLockViewListener mPatternLockViewListener;
    private UnlockPatternTaskLogic unlockPatternTaskLogic;
    private String patternHintFirstPart;
    private String patternHintSecondPart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unlockPatternTaskLogic = new UnlockPatternTaskLogic(UnlockActivityTask.this);
        setupUI();
        setupPatternLockViewListener();
        setupPatternUnlock();
        motionSensorUtil = new MotionSensorUtil(unlockPatternTaskLogic.userID, TASK_ID, (SensorManager) getSystemService(SENSOR_SERVICE));
    }

    private void setupUI(){
        setContentView(R.layout.activity_unlock_task);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        patternHint = findViewById(R.id.pattern_hint);
        Resources res = UnlockActivityTask.this.getResources();
        patternHintFirstPart = res.getString(R.string.pattern_hint);
        patternHintSecondPart = res.getString(R.string.pin_counter);
        patternHint.setText(patternHintFirstPart + patternHintSecondPart + unlockPatternTaskLogic.getInitialHintMessage());
    }

    public void onUnlockTrainingEnd(View view){
        unlockPatternTaskLogic.trainingMode = false;
        mPatternLockView.setPattern(PatternViewMode.AUTO_DRAW, PatternLockUtils.stringToPattern(mPatternLockView, unlockPatternTaskLogic.getPattern()));
        patternHint.setText(patternHintFirstPart + patternHintSecondPart + unlockPatternTaskLogic.getHintMessageAfterTraining());
        view.setVisibility(View.GONE);
        motionSensorUtil.registerListeners();
    }

    public void makeToast(String message){
        Toast.makeText(getApplication(),
                message, Toast.LENGTH_SHORT).show();
    }

    public void startAsyncTask(){
        motionSensorUtil.stop();
        UnlockActivityTask.AsyncTaskRunner runner = new UnlockActivityTask.AsyncTaskRunner();
        runner.execute();
    }

    public void onUpdatePatternHint(String text) {
        patternHint.setText(patternHintFirstPart + patternHintSecondPart + text);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        unlockPatternTaskLogic.calculateDotPositions();
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
            unlockPatternTaskLogic.handleTouch(view, event);
            return false;
        }
    };

    private void setupPatternUnlock () {
        mPatternLockView = findViewById(R.id.pattern_lock_view);
        unlockPatternTaskLogic.setLockView(mPatternLockView);
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
        unlockPatternTaskLogic.setDotProgressAndPattern();
        mPatternLockView.setPattern(PatternViewMode.AUTO_DRAW, PatternLockUtils.stringToPattern(mPatternLockView, unlockPatternTaskLogic.getTrainingPattern()));
    }

    void setupPatternLockViewListener() {
        mPatternLockViewListener = new PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<Dot> progressPattern) {
                unlockPatternTaskLogic.updateProgress(progressPattern);
            }

            @Override
            public void onComplete(List<Dot> pattern) {
                unlockPatternTaskLogic.onPatternComplete(pattern);
            }

            @Override
            public void onCleared() {
            }
        };
    }

    private void startIntent(){
        int activity = unlockPatternTaskLogic.latinSquareUtil.getNext();
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
                unlockPatternTaskLogic.writeUnlockPatternData(motionSensorUtil.getMotionSensorData());
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        motionSensorUtil.stop();
    }
}
