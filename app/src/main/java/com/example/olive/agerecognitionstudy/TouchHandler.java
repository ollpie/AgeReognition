package com.example.olive.agerecognitionstudy;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by olive on 07.01.2018.
 */

public class TouchHandler {

    private View view;

    public TouchHandler (View view) {
        this.view = view;
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();

        switch(eventAction) {
            case MotionEvent.ACTION_DOWN:
                Log.d("Touch Handler", "Hi!");
                break;

            case MotionEvent.ACTION_MOVE:

                break;

            case MotionEvent.ACTION_UP:

                break;
            default:
                break;
        }

        return view.dispatchTouchEvent(event);
    }
}
