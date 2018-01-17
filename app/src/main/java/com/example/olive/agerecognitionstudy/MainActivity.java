package com.example.olive.agerecognitionstudy;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static DatabaseHandler db;
    private EditText ageEntry;
    private RadioButton male;
    private RadioButton female;
    public static String currentUserID;
    public static int statusbarOffset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
        db = new DatabaseHandler(getApplicationContext());
    }

    public void startSession(View view) {
        if (ageEntry.getText().equals("")) {
            showAlertDialog();
        }else if (!male.isChecked() && !female.isChecked()){
            showAlertDialog();
        }else{
            Participant p = new Participant(Integer.parseInt(ageEntry.getText().toString()), checkGender());
            currentUserID = p.getId();
            db.createParticipant(p);
            db.closeDB();
            ageEntry.setText("");
            Intent intent = new Intent(this, GenericTaskActivity.class);
            startActivity(intent);
        }
    }

    public void logData(View view) {
        // Getting all Participants
        LoggingModule loggingModule = new LoggingModule(db.getAllParticipants(),
                db.getAllGenericTaskData(),
                null,
                null,
                null,
                db.getMotionSensorData());
        loggingModule.generateParticipantExcelFile();
        loggingModule.generateGenericTaskExcelFile();
        loggingModule.generateMotionSensorExcelFile();
        Toast.makeText(getApplication(),
                "Data Exported in a Excel Sheet", Toast.LENGTH_SHORT).show();
    }

    public void clearData(View view) {
        Log.d("Clear Data", "Deleting all Participants");
        db.deleteAllTables();
    }

    private void setupUI() {
        setContentView(R.layout.activity_main);
        ageEntry = findViewById(R.id.editTextAge);
        male = findViewById(R.id.radio_btn_male);
        female = findViewById(R.id.radio_btn_female);

        // calculate statusbar offset
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusbarOffset = getResources().getDimensionPixelSize(resourceId);
        }
    }

    private String checkGender(){
        if (male.isChecked()) {
            return "Männlich";
        } else {
            return "Weiblich";
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new Builder(this);

        // set title
        alertDialogBuilder.setTitle("Obacht!");

        // set dialog message
        alertDialogBuilder
                .setMessage("Bitte Alter und Geschlecht auswählen.")
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static DatabaseHandler getDbHandler() {
        return db;
    }
}
