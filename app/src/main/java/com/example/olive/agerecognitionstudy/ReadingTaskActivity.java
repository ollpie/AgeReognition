package com.example.olive.agerecognitionstudy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Point;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

public class ReadingTaskActivity extends AppCompatActivity {

    private static final String TASK_ID = "Reading Task";
    private static final int TEXT_SIZE = 14;
    private static final int[] FIRST_TEXTS = {R.string.regensburg1, R.string.borschtsch};
    private static final int[] SECOND_TEXTS = {R.string.regensburg2, R.string.borschtsch2};
    private static final int[] FIRST_IMAGE = {R.drawable.regensburger_dom, R.drawable.borschtsch};
    private static final int[] SECOND_IMAGE = {0,0};

    ScrollView scrollView;
    Button doneButton;
    TextView textView1;
    TextView textView2;
    ImageView image1;
    ImageView image2;

    private DatabaseHandler database;
    private MotionSensorUtil motionSensorUtil;
    private ReadingTaskDataModel readingTaskModel;
    private String userID;

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

    private int displayHeight;
    private boolean trainigsMode = true;
    private LatinSquareUtil latinSquareUtil;
    private int clickCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
        latinSquareUtil = MainActivity.latinSquareUtil;
        userID = MainActivity.currentUserID;
        database = MainActivity.getDbHandler();
        readingTaskModel = new ReadingTaskDataModel(userID);
        motionSensorUtil = new MotionSensorUtil(userID, TASK_ID, (SensorManager) getSystemService(SENSOR_SERVICE));
    }

    private void setupUI(){
        setContentView(R.layout.activity_reading_task);
        scrollView = findViewById(R.id.scrollView);
        doneButton = findViewById(R.id.doneButton);
        image1 = findViewById(R.id.readingImage1);
        image2 = findViewById(R.id.readingImage2);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView1.setTextSize(TEXT_SIZE);
        textView2.setTextSize(TEXT_SIZE);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        displayHeight = size.y;
    }

    public void buttonClicked (View view){
        if (trainigsMode){
            motionSensorUtil.registerListeners();
        }
        trainigsMode = false;
        nextText();
    }

    private void nextText(){
        if (clickCounter == FIRST_TEXTS.length){
            ReadingTaskActivity.AsyncTaskRunner asyncTask = new ReadingTaskActivity.AsyncTaskRunner();
            asyncTask.execute();
        }else{
            scrollView.scrollTo(0,0);
            textView1.setText(getResources().getString(FIRST_TEXTS[clickCounter]));
            textView2.setText(getResources().getString(SECOND_TEXTS[clickCounter]));
            /*LinearLayout.LayoutParams parameter = (LinearLayout.LayoutParams) image1.getLayoutParams();
            parameter.setMargins(0,0,0,0);
            image1.setLayoutParams(parameter);*/
            image1.setImageResource(FIRST_IMAGE[clickCounter]);

/*            LinearLayout.LayoutParams parameter2 = (LinearLayout.LayoutParams) image2.getLayoutParams();
            parameter.setMargins(0,0,0,0);
            image2.setLayoutParams(parameter2);*/
            image2.setImageResource(SECOND_IMAGE[clickCounter]);
            clickCounter++;
        }
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
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
        return super.dispatchTouchEvent(event);
    }

    private void writeDataIntoLists(String eventType) {
        readingTaskModel.setParticipantId(userID);
        readingTaskModel.setEventType(eventType);
        readingTaskModel.setyViewportTop(yViewPort);
        readingTaskModel.setyViewportBottom(yViewPortBottom);
        readingTaskModel.setFont("Default");
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
                database.createReadingTaskData(readingTaskModel);
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
            progressDialog = ProgressDialog.show(ReadingTaskActivity.this,
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
