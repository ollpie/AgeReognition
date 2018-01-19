package com.example.olive.agerecognitionstudy;

import android.os.Environment;

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

/**
 * Created by olive on 17.01.2018.
 */

public class LoggingModule {

    private List<Participant> participantList;
    private GenericTaskDataModel genericModel;
    private PinTaskDataModel pinModel;
    private UnlockTaskDataModel unlockModel;
    private ReadingTaskDataModel readingModel;
    private MotionSensorDataModel motionSensorModel;

    public LoggingModule(List<Participant> participantList, GenericTaskDataModel genericModel, PinTaskDataModel pinModel,
                         UnlockTaskDataModel unlockModel, ReadingTaskDataModel readingModel,
                         MotionSensorDataModel motionSensorModel) {
        this.participantList = participantList;
        this.genericModel = genericModel;
        this.pinModel = pinModel;
        this.unlockModel = unlockModel;
        this.readingModel = readingModel;
        this.motionSensorModel = motionSensorModel;
    }

    public void generateParticipantExcelFile() {
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

            List<Participant> allParticipants = participantList;
            int i = 1;
            for (Participant participant : allParticipants) {
                sheet.addCell(new Label(0, i, participant.getId()));
                sheet.addCell(new Label(1, i, String.valueOf(participant.getAge())));
                sheet.addCell(new Label(2, i, participant.getGender()));
                i++;
            }

            workbook.write();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }


    void generateGenericTaskExcelFile() {
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
            WritableSheet sheet = workbook.createSheet("genericDataList", 0);
            // column and row
            sheet.addCell(new Label(0, 0, "Participant ID"));
            sheet.addCell(new Label(1, 0, "Target ID"));
            sheet.addCell(new Label(2, 0, "Event Type"));
            sheet.addCell(new Label(3, 0, "X Target"));
            sheet.addCell(new Label(4, 0, "Y Target"));
            sheet.addCell(new Label(5, 0, "X Touch"));
            sheet.addCell(new Label(6, 0, "Y Touch"));
            sheet.addCell(new Label(7, 0, "Touch Pressure"));
            sheet.addCell(new Label(8, 0, "Touch Size"));
            sheet.addCell(new Label(9, 0, "Touch Orientation"));
            sheet.addCell(new Label(10, 0, "Touch Major"));
            sheet.addCell(new Label(11, 0, "Touch Minor"));
            sheet.addCell(new Label(12, 0, "Timestamp"));

            GenericTaskDataModel model = genericModel;

            for (int i = 0; i < model.length(); i++) {
                sheet.addCell(new Label(0, i + 1, model.getParticipantList().get(i)));
                sheet.addCell(new Label(1, i + 1, String.valueOf(model.getTargetId().get(i))));
                sheet.addCell(new Label(2, i + 1, model.getEventType().get(i)));
                sheet.addCell(new Label(3, i + 1, String.valueOf(model.getXTarget().get(i))));
                sheet.addCell(new Label(4, i + 1, String.valueOf(model.getYTarget().get(i))));
                sheet.addCell(new Label(5, i + 1, String.valueOf(model.getXTouch().get(i))));
                sheet.addCell(new Label(6, i + 1, String.valueOf(model.getYTouch().get(i))));
                sheet.addCell(new Label(7, i + 1, String.valueOf(model.getTouchPressure().get(i))));
                sheet.addCell(new Label(8, i + 1, String.valueOf(model.getTouchSize().get(i))));
                if (model.getTouchOrientation().size() == 0) {
                    sheet.addCell(new Label(9, i + 1, "N.A."));
                } else {
                    sheet.addCell(new Label(9, i + 1, String.valueOf(model.getTouchOrientation().get(i))));
                }
                sheet.addCell(new Label(10, i + 1, String.valueOf(model.getTouchMajor().get(i))));
                sheet.addCell(new Label(11, i + 1, String.valueOf(model.getTouchMinor().get(i))));
                sheet.addCell(new Label(12, i + 1, String.valueOf(model.getTimestamp().get(i))));

            }

            workbook.write();
            workbook.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    void generatePinTaskExcelFile() {
        File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String csvFile = "Pin Task.xls";

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
            WritableSheet sheet = workbook.createSheet("pinDataList", 0);
            // column and row
            sheet.addCell(new Label(0, 0, "Participant ID"));
            sheet.addCell(new Label(1, 0, "Pin"));
            sheet.addCell(new Label(2, 0, "Event Type"));
            sheet.addCell(new Label(3, 0, "Repetition"));
            sheet.addCell(new Label(4, 0, "Progress"));
            sheet.addCell(new Label(5, 0, "Current Digit"));
            sheet.addCell(new Label(6, 0, "Actual Digit"));
            sheet.addCell(new Label(7, 0, "Sequence Correctnness"));
            sheet.addCell(new Label(8, 0, "X Button Center"));
            sheet.addCell(new Label(9, 0, "Y Button Center"));
            sheet.addCell(new Label(10, 0, "X Touch"));
            sheet.addCell(new Label(11, 0, "Y Touch"));
            sheet.addCell(new Label(12, 0, "Touch Pressure"));
            sheet.addCell(new Label(13, 0, "Touch Size"));
            sheet.addCell(new Label(14, 0, "Touch Orientation"));
            sheet.addCell(new Label(15, 0, "Touch Major"));
            sheet.addCell(new Label(16, 0, "Touch Minor"));
            sheet.addCell(new Label(17, 0, "Timestamp"));

            PinTaskDataModel model = pinModel;

            for (int i = 0; i < model.length(); i++) {
                sheet.addCell(new Label(0, i + 1, model.getParticipantList().get(i)));
                sheet.addCell(new Label(1, i + 1, String.valueOf(model.getPin().get(i))));
                sheet.addCell(new Label(2, i + 1, model.getEventType().get(i)));
                sheet.addCell(new Label(3, i + 1, String.valueOf(model.getRepetition().get(i))));
                sheet.addCell(new Label(4, i + 1, model.getProgress().get(i)));
                sheet.addCell(new Label(5, i + 1, String.valueOf(model.getCurrentDigit().get(i))));
                sheet.addCell(new Label(6, i + 1, String.valueOf(model.getActualDigit().get(i))));
                sheet.addCell(new Label(7, i + 1, model.getSequenceCorrect().get(i)));
                sheet.addCell(new Label(8, i + 1, String.valueOf(model.getxButtonCenter().get(i))));
                sheet.addCell(new Label(9, i + 1, String.valueOf(model.getyButtonCenter().get(i))));
                sheet.addCell(new Label(10, i + 1, String.valueOf(model.getxTouch().get(i))));
                sheet.addCell(new Label(11, i + 1, String.valueOf(model.getyTouch().get(i))));
                sheet.addCell(new Label(12, i + 1, String.valueOf(model.getTouchPressure().get(i))));
                sheet.addCell(new Label(13, i + 1, String.valueOf(model.getTouchSize().get(i))));
                if (model.getTouchOrientation().size() == 0) {
                    sheet.addCell(new Label(14, i + 1, "N.A."));
                } else {
                    sheet.addCell(new Label(14, i + 1, String.valueOf(model.getTouchOrientation().get(i))));
                }
                sheet.addCell(new Label(15, i + 1, String.valueOf(model.getTouchMajor().get(i))));
                sheet.addCell(new Label(16, i + 1, String.valueOf(model.getTouchMinor().get(i))));
                sheet.addCell(new Label(17, i + 1, String.valueOf(model.getTimestamp().get(i))));

            }

            workbook.write();
            workbook.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    void generateMotionSensorExcelFile () {
        File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String csvFile = "Motion Sensor Daten.xls";

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
            WritableSheet sheet = workbook.createSheet("motionSensorDataList", 0);
            // column and row
            sheet.addCell(new Label(0, 0, "Participant ID"));
            sheet.addCell(new Label(1, 0, "Task ID"));
            sheet.addCell(new Label(2, 0, "Timestamp"));
            sheet.addCell(new Label(3, 0, "X Acc"));
            sheet.addCell(new Label(4, 0, "Y Acc"));
            sheet.addCell(new Label(5, 0, "Z Acc"));
            sheet.addCell(new Label(6, 0, "X Grav"));
            sheet.addCell(new Label(7, 0, "Y Grav"));
            sheet.addCell(new Label(8, 0, "Z Grav"));
            sheet.addCell(new Label(9, 0, "X Gyros"));
            sheet.addCell(new Label(10, 0, "Y Gyros"));
            sheet.addCell(new Label(11, 0, "Z Gyros"));
            sheet.addCell(new Label(12, 0, "X Rotation"));
            sheet.addCell(new Label(13, 0, "Y Rotation"));
            sheet.addCell(new Label(14, 0, "Z Rotation"));

            MotionSensorDataModel model = motionSensorModel;

            for (int i = 0; i < model.length(); i++) {
                sheet.addCell(new Label(0, i+1, model.getParticipantList().get(i)));
                sheet.addCell(new Label(1, i+1, model.getTaskIDList().get(i)));
                sheet.addCell(new Label(2, i+1, String.valueOf(model.getTimestamp().get(i))));
                sheet.addCell(new Label(3, i+1, String.valueOf(model.getXAcc().get(i))));
                sheet.addCell(new Label(4, i+1, String.valueOf(model.getYAcc().get(i))));
                sheet.addCell(new Label(5, i+1, String.valueOf(model.getZAcc().get(i))));
                sheet.addCell(new Label(6, i+1, String.valueOf(model.getXGravity().get(i))));
                sheet.addCell(new Label(7, i+1, String.valueOf(model.getYGravity().get(i))));
                sheet.addCell(new Label(8, i+1, String.valueOf(model.getZGravity().get(i))));
                sheet.addCell(new Label(9, i+1, String.valueOf(model.getXGyroscope().get(i))));
                sheet.addCell(new Label(10, i+1, String.valueOf(model.getYGyroscope().get(i))));
                sheet.addCell(new Label(11, i+1, String.valueOf(model.getZGyroscope().get(i))));
                sheet.addCell(new Label(12, i+1, String.valueOf(model.getXRotation().get(i))));
                sheet.addCell(new Label(13, i+1, String.valueOf(model.getYRotation().get(i))));
                sheet.addCell(new Label(14, i+1, String.valueOf(model.getZRotation().get(i))));
            }

            workbook.write();
            workbook.close();

        } catch(IOException e){
            e.printStackTrace();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }
}
