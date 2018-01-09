package com.example.olive.agerecognitionstudy;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;

public class PinTaskActivity extends AppCompatActivity {

    private static final int REPETITIONS = 5;
    private static final String[] PINS = {"0537", "8683", "3915", "4710", "6327"};

    Point btnCenter;
    private Point touchPoint;
    private GridLayout layout;
    double euclideanDistance;
    int manhattenDistance;
    View btnCenterYLine;
    View btnCenterXLine;
    View touchCenterYLine;
    View touchCenterXLine;

    private int repetitionCount = 0;

    DatabaseHandler database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_task);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_pin_task, null);
        layout = findViewById(R.id.gridLayout);

        database = MainActivity.getDbHandler();

        btnCenterXLine = new View(this);
        btnCenterXLine.setBackgroundColor(0xFFFFFFFF);
        this.addContentView(btnCenterXLine, new ViewGroup.LayoutParams( 1, ViewGroup.LayoutParams.FILL_PARENT));

        btnCenterYLine = new View(this);
        btnCenterYLine.setBackgroundColor(0xFFFFFFFF);
        this.addContentView(btnCenterYLine, new ViewGroup.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, 1));

        touchCenterYLine = new View(this);
        touchCenterYLine.setBackgroundColor(0xFFF00000);
        this.addContentView(touchCenterYLine, new ViewGroup.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, 1));

        touchCenterXLine = new View(this);
        touchCenterXLine.setBackgroundColor(0xFFF00000);
        this.addContentView(touchCenterXLine, new ViewGroup.LayoutParams( 1, ViewGroup.LayoutParams.FILL_PARENT));
        showAlertDialog();
    }

    public void buttonPressed(View view) {
        btnCenter = new Point();
        Button b = (Button) view;
        touchPoint.y -= b.getHeight();
        if (b.getText().equals("Done")) {
            Intent intent = new Intent(this, DragAndDropTask.class);
            startActivity(intent);
        }

        btnCenter.x = (int) (layout.getX() + b.getX() + b.getWidth()/2);
        btnCenter.y = (int) (layout.getY() + b.getY() + b.getHeight()/2);
        euclideanDistance = getEuclideanDistance(touchPoint, btnCenter);
        manhattenDistance = getManhattanDistance(touchPoint, btnCenter);

        btnCenterXLine.setX(btnCenter.x);
        btnCenterYLine.setY(btnCenter.y);
        touchCenterYLine.setY(touchPoint.y);
        touchCenterXLine.setX(touchPoint.x);

        Log.d("Button pressed was", b.getText().toString());
        Log.d("Button Center is", String.valueOf(btnCenter));
        Log.d("Touch Point", String.valueOf(touchPoint));
        Log.d("Euclidean Distance", String.valueOf(euclideanDistance));
        Log.d("Manhatten Distance", String.valueOf(manhattenDistance));
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();

        switch(eventAction) {
            case MotionEvent.ACTION_DOWN:
                touchPoint = new Point((int) event.getX(), (int) event.getY());
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    double getEuclideanDistance(Point touch, Point center) {
        int deltaX = touch.x - center.x;
        int deltaY = touch.y - center.y;
        double result = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
        return result;
    }

    int getManhattanDistance(Point touch, Point center) {
        return Math.abs(touch.x-center.x) + Math.abs(touch.y-center.y);
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new Builder(this);

        // set title
        alertDialogBuilder.setTitle("Die einzugebene Pin lautet:");

        // set dialog message
        alertDialogBuilder
                .setMessage(PINS[repetitionCount])
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

}
