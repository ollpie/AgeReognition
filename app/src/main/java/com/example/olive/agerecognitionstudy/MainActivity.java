package com.example.olive.agerecognitionstudy;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class MainActivity extends AppCompatActivity {

    private static final String TABLE_GENERIC_TASK = "generic_task";
    private static final String TABLE_PIN_TASK = "pin_task";

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
        Log.d("Get Participants", "Getting All Participants");
        generateParticipantExcelFile();
        generateGenericTaskExcelFile();

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

    void generateParticipantExcelFile () {
        File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String csvFile = "Teilnehmer.xls";

        File directory = new File(sd.getAbsolutePath());
        //create directory if not exist
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }
        try {

            //file path
            File file = new File(directory, csvFile);
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;
            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("userList", 0);
            // column and row
            sheet.addCell(new Label(0, 0, "User ID"));
            sheet.addCell(new Label(1, 0, "Alter"));
            sheet.addCell(new Label(2, 0, "Geschlecht"));

            List<Participant> allParticipants = db.getAllParticipants();
            int i = 1;
            for (Participant participant : allParticipants) {
                sheet.addCell(new Label(0, i, participant.getId()));
                sheet.addCell(new Label(1, i, String.valueOf(participant.getAge())));
                sheet.addCell(new Label(2, i, participant.getGender()));
                i++;
            }

            workbook.write();
            workbook.close();
            Toast.makeText(getApplication(),
                    "Data Exported in a Excel Sheet", Toast.LENGTH_SHORT).show();
        } catch(IOException e){
            e.printStackTrace();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    void generateGenericTaskExcelFile () {
        File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String csvFile = "Generischer Task.xls";

        File directory = new File(sd.getAbsolutePath());
        //create directory if not exist
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }
        try {

            //file path
            File file = new File(directory, csvFile);
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;
            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("userList", 0);
            // column and row
            sheet.addCell(new Label(0, 0, "User ID"));
            sheet.addCell(new Label(1, 0, "Target ID"));
            sheet.addCell(new Label(3, 0, "Timestamp"));
            sheet.addCell(new Label(4, 0, "Event Type"));
            sheet.addCell(new Label(5, 0, "X Target"));
            sheet.addCell(new Label(6, 0, "Y Target"));
            sheet.addCell(new Label(7, 0, "X Touch"));
            sheet.addCell(new Label(8, 0, "Y Touch"));
            sheet.addCell(new Label(9, 0, "X Acc"));
            sheet.addCell(new Label(10, 0, "Y Acc"));
            sheet.addCell(new Label(11, 0, "Z Acc"));
            sheet.addCell(new Label(12, 0, "X Grav"));
            sheet.addCell(new Label(13, 0, "Y Grav"));
            sheet.addCell(new Label(14, 0, "Z Grav"));
            sheet.addCell(new Label(15, 0, "X Gyro"));
            sheet.addCell(new Label(16, 0, "Y Gyro"));
            sheet.addCell(new Label(17, 0, "Z Gyro"));
            sheet.addCell(new Label(18, 0, "X Rot"));
            sheet.addCell(new Label(19, 0, "Y Rot"));
            sheet.addCell(new Label(20, 0, "Z Rot"));



            GenericTaskDataModel model = db.getAllGenericTaskData(TABLE_PIN_TASK);

            for (int i = 0; i < model.length(); i++) {
                sheet.addCell(new Label(0, i+1, model.getUserId().get(i)));
                sheet.addCell(new Label(1, i+1, String.valueOf(model.getTargetId().get(i))));
                sheet.addCell(new Label(3, i+1, String.valueOf(model.getTimestamp().get(i))));
                sheet.addCell(new Label(4, i+1, model.getEventType().get(i)));
                sheet.addCell(new Label(5, i+1, String.valueOf(model.getXTarget().get(i))));
                sheet.addCell(new Label(6, i+1, String.valueOf(model.getYTarget().get(i))));
                sheet.addCell(new Label(7, i+1, String.valueOf(model.getXTouch().get(i))));
                sheet.addCell(new Label(8, i+1, String.valueOf(model.getYTouch().get(i))));
                sheet.addCell(new Label(9, i+1, String.valueOf(model.getXAcc().get(i))));
                sheet.addCell(new Label(10, i+1, String.valueOf(model.getYAcc().get(i))));
                sheet.addCell(new Label(11, i+1, String.valueOf(model.getZAcc().get(i))));
                sheet.addCell(new Label(12, i+1, String.valueOf(model.getXGravity().get(i))));
                sheet.addCell(new Label(13, i+1, String.valueOf(model.getYGravity().get(i))));
                sheet.addCell(new Label(14, i+1, String.valueOf(model.getZGravity().get(i))));
                sheet.addCell(new Label(15, i+1, String.valueOf(model.getXGyroscope().get(i))));
                sheet.addCell(new Label(16, i+1, String.valueOf(model.getYGyroscope().get(i))));
                sheet.addCell(new Label(17, i+1, String.valueOf(model.getZGyroscope().get(i))));
                sheet.addCell(new Label(18, i+1, String.valueOf(model.getXRotation().get(i))));
                sheet.addCell(new Label(19, i+1, String.valueOf(model.getYRotation().get(i))));
                sheet.addCell(new Label(20, i+1, String.valueOf(model.getZRotation().get(i))));
            }

            workbook.write();
            workbook.close();
            Toast.makeText(getApplication(),
                    "Data Exported in a Excel Sheet", Toast.LENGTH_SHORT).show();
        } catch(IOException e){
            e.printStackTrace();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }
}
