package com.example.olive.agerecognitionstudy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TABLE_GENERIC_TASK = "generic_task";
    private static final String TABLE_PIN_TASK = "pin_task";

    private static DatabaseHandler db;
    private EditText ageEntry;
    private RadioButton male;
    private RadioButton female;
    public static String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupUI();
        db = new DatabaseHandler(getApplicationContext());



    }

    public void startSession(View view) {
        Participant p = new Participant(Integer.parseInt(ageEntry.getText().toString()), checkGender());
        currentUserID = p.getId();
        db.createParticipant(p);
        db.closeDB();
        Intent intent = new Intent(this, GenericTaskActivity.class);
        startActivity(intent);
    }

    public void logData(View view) {
        // Getting all Participants
        Log.d("Get Participants", "Getting All Participants");

        List<Participant> allParticipants = db.getAllParticipants();
        for (Participant participant : allParticipants) {
            Log.d("ID", participant.getId());
            Log.d("Age", String.valueOf(participant.getAge()));
            Log.d("Gender", participant.getGender());
        }

        GenericTaskDataModel model = db.getAllGenericTaskData(TABLE_GENERIC_TASK);
        for (int i = 0; i < model.length(); i++) {
            Log.d("Entry Start", "--------------------------------------");
            Log.d("User ID", model.getUserId().get(i));
            Log.d("Target ID", String.valueOf(model.getTargetId().get(i)));
            Log.d("Timestamp", String.valueOf(model.getTimestamp().get(i)));
            Log.d("Event Type", model.getEventType().get(i));
            Log.d("X Target", String.valueOf(model.getXTarget().get(i)));
            Log.d("Y Target", String.valueOf(model.getYTarget().get(i)));
            Log.d("X Touch", String.valueOf(model.getXTouch().get(i)));
            Log.d("Y Touch", String.valueOf(model.getYTouch().get(i)));
            Log.d("X Acc", String.valueOf(model.getXAcc().get(i)));
            Log.d("Y Acc", String.valueOf(model.getYAcc().get(i)));
            Log.d("Z Acc", String.valueOf(model.getZAcc().get(i)));
            Log.d("X Grav", String.valueOf(model.getXGravity().get(i)));
            Log.d("Y Grav", String.valueOf(model.getYGravity().get(i)));
            Log.d("Z Grav", String.valueOf(model.getZGravity().get(i)));
            Log.d("X Gyro", String.valueOf(model.getXGyroscope().get(i)));
            Log.d("Y Gyro", String.valueOf(model.getYGyroscope().get(i)));
            Log.d("Z Gyro", String.valueOf(model.getZGyroscope().get(i)));
            Log.d("X Rot", String.valueOf(model.getXRotation().get(i)));
            Log.d("Y Rot", String.valueOf(model.getYRotation().get(i)));
            Log.d("Z Rot", String.valueOf(model.getZRotation().get(i)));
        }
    }

    public void clearData(View view) {
        Log.d("Clear Data", "Deleting all Participants");
        db.deleteAllParticipants();
    }

    private void setupUI() {
        setContentView(R.layout.activity_main);
        ageEntry = findViewById(R.id.editTextAge);
        male = findViewById(R.id.radio_btn_male);
        female = findViewById(R.id.radio_btn_female);
    }

    private String checkGender(){
        if (male.isChecked()) {
            return "Männlich";
        } else {
            return "Weiblich";
        }
    }

    public static DatabaseHandler getDbHandler() {
        return db;
    }
}
