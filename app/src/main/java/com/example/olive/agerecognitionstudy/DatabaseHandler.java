package com.example.olive.agerecognitionstudy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by olive on 04.01.2018.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "participantsManager";

    // Table Names
    private static final String TABLE_PARTICIPANTS = "participants";
    private static final String TABLE_GENERIC_TASK = "generic_task";
    private static final String TABLE_PIN_TASK = "pin_task";
    private static final String TABLE_SLIDE_PIN_TASK = "slide_pin_task";
    private static final String TABLE_READING_TASK = "reading_task";

    private static final String KEY_ID = "id";

    // PARTICIPANTS Table - column namaes
    private static final String KEY_PARTICIPANT_ID = "participant_id";
    private static final String KEY_AGE = "age";
    private static final String KEY_GENDER = "gender";

    // GENERIC_TASK Table - column names
    private static final String KEY_TARGET_ID = "target_id";
    private static final String KEY_TIMESTAMP = "timestamp";
    private static final String KEY_EVENT_TYPE = "event_type";

    private static final String KEY_X_TARGET = "x_target";
    private static final String KEY_Y_TARGET = "y_target";

    private static final String KEY_X_TOUCH = "x_touch";
    private static final String KEY_Y_TOUCH = "y_touch";

    private static final String KEY_X_ACC = "x_acc";
    private static final String KEY_Y_ACC = "y_acc";
    private static final String KEY_Z_ACC = "z_acc";

    private static final String KEY_X_GRAVITY = "x_gravity";
    private static final String KEY_Y_GRAVITY = "y_gravity";
    private static final String KEY_Z_GRAVITY = "z_gravity";

    private static final String KEY_X_GYRO = "x_gyro";
    private static final String KEY_Y_GYRO = "y_gyro";
    private static final String KEY_Z_GYRO = "z_gyro";

    private static final String KEY_X_ROTATION = "x_rotation";
    private static final String KEY_Y_ROTATION = "y_rotation";
    private static final String KEY_Z_ROTATION = "z_rotation";

    // PIN_TASK Table - column names

    // SLIDE_PIN_TASK

    // READING_TASK

    // Table Create Statements
    // Participants table create statement
    private static final String CREATE_TABLE_PARTICIPANTS = "CREATE TABLE "
            + TABLE_PARTICIPANTS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PARTICIPANT_ID + " TEXT," + KEY_AGE
            + " INTEGER," + KEY_GENDER + " TEXT" + ")";

    // Generic task table create statement
    private static final String CREATE_TABLE_GENERIC_TASK = "CREATE TABLE "
            + TABLE_GENERIC_TASK + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PARTICIPANT_ID + " TEXT," + KEY_TARGET_ID + " INTEGER,"
            + KEY_TIMESTAMP + " REAL," + KEY_EVENT_TYPE + " TEXT,"
            + KEY_X_TARGET + " REAL," + KEY_Y_TARGET + " REAL,"
            + KEY_X_TOUCH + " REAL," + KEY_Y_TOUCH + " REAL,"
            + KEY_X_ACC + " REAL," + KEY_Y_ACC + " REAL," + KEY_Z_ACC + " REAL,"
            + KEY_X_GRAVITY + " REAL," + KEY_Y_GRAVITY + " REAL," + KEY_Z_GRAVITY + " REAL,"
            + KEY_X_GYRO + " REAL," + KEY_Y_GYRO + " REAL," + KEY_Z_GYRO + " REAL,"
            + KEY_X_ROTATION + " REAL," + KEY_Y_ROTATION + " REAL," + KEY_Z_ROTATION + " REAL" + ")";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_GENERIC_TASK);
        db.execSQL(CREATE_TABLE_PARTICIPANTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTICIPANTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GENERIC_TASK);

        // create new tables
        onCreate(db);
    }

    // ------------------------ "participants" table methods ----------------//

    /**
     * Creating Participants
     */
    public long createParticipant(Participant participant) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PARTICIPANT_ID, participant.getId());
        values.put(KEY_AGE, participant.getAge());
        values.put(KEY_GENDER, participant.getGender());

        // insert row
        long participant_id = db.insert(TABLE_PARTICIPANTS, null, values);
        Log.d("Create Participant", "participant created");
        return participant_id;

    }

    /*
 * getting all participants
 * */
    public List<Participant> getAllParticipants() {
        List<Participant> participants = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PARTICIPANTS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Participant pt = new Participant();
                pt.setId(c.getString((c.getColumnIndex(KEY_PARTICIPANT_ID))));
                pt.setAge((c.getInt(c.getColumnIndex(KEY_AGE))));
                pt.setGender(c.getString(c.getColumnIndex(KEY_GENDER)));

                // adding to participant list
                participants.add(pt);
            } while (c.moveToNext());
        }

        return participants;
    }

    // deleting all Participants
    public void deleteAllParticipants() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_PARTICIPANTS);
        db.execSQL("delete from "+ TABLE_GENERIC_TASK);
    }

    public void createGenericTaskEntry(GenericTaskDataModel taskModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < taskModel.length(); i++) {
            ContentValues values = new ContentValues();

            values.put(KEY_PARTICIPANT_ID, taskModel.getUserId().get(i));
            values.put(KEY_TARGET_ID, taskModel.getTargetId().get(i));
            values.put(KEY_TIMESTAMP, taskModel.getTimestamp().get(i));
            values.put(KEY_EVENT_TYPE, taskModel.getEventType().get(i));

            values.put(KEY_X_TARGET, taskModel.getXTarget().get(i));
            values.put(KEY_Y_TARGET, taskModel.getYTarget().get(i));
            values.put(KEY_X_TOUCH, taskModel.getXTouch().get(i));
            values.put(KEY_Y_TOUCH, taskModel.getYTouch().get(i));

            values.put(KEY_X_ACC, taskModel.getXAcc().get(i));
            values.put(KEY_Y_ACC, taskModel.getYAcc().get(i));
            values.put(KEY_Z_ACC, taskModel.getZAcc().get(i));

            values.put(KEY_X_GRAVITY, taskModel.getXGravity().get(i));
            values.put(KEY_Y_GRAVITY, taskModel.getYGravity().get(i));
            values.put(KEY_Z_GRAVITY, taskModel.getZGravity().get(i));

            values.put(KEY_X_GYRO, taskModel.getXGyroscope().get(i));
            values.put(KEY_Y_GYRO, taskModel.getYGyroscope().get(i));
            values.put(KEY_Z_GYRO, taskModel.getZGyroscope().get(i));

            values.put(KEY_X_ROTATION, taskModel.getXRotation().get(i));
            values.put(KEY_Y_ROTATION, taskModel.getYRotation().get(i));
            values.put(KEY_Z_ROTATION, taskModel.getZRotation().get(i));

            db.insert(TABLE_GENERIC_TASK, null, values);
        }
    }

    public GenericTaskDataModel getAllGenericTaskData() {
        GenericTaskDataModel model = new GenericTaskDataModel();
        String selectQuery = "SELECT * FROM " + TABLE_GENERIC_TASK;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                model.setUserId(c.getString((c.getColumnIndex(KEY_PARTICIPANT_ID))));
                model.setTargetId(c.getInt((c.getColumnIndex(KEY_TARGET_ID))));
                model.setTimestamp(c.getLong((c.getColumnIndex(KEY_TIMESTAMP))));
                model.setEventType(c.getString((c.getColumnIndex(KEY_EVENT_TYPE))));
                model.setXTarget(c.getFloat((c.getColumnIndex(KEY_X_TARGET))));
                model.setYTarget(c.getFloat((c.getColumnIndex(KEY_Y_TARGET))));
                model.setXTouch(c.getFloat((c.getColumnIndex(KEY_X_TOUCH))));
                model.setYTouch(c.getFloat((c.getColumnIndex(KEY_Y_TOUCH))));
                model.setXAcc(c.getFloat((c.getColumnIndex(KEY_X_ACC))));
                model.setYAcc(c.getFloat((c.getColumnIndex(KEY_Y_ACC))));
                model.setZAcc(c.getFloat((c.getColumnIndex(KEY_Z_ACC))));
                model.setXGravity(c.getFloat((c.getColumnIndex(KEY_X_GRAVITY))));
                model.setYGravity(c.getFloat((c.getColumnIndex(KEY_Y_GRAVITY))));
                model.setZGravity(c.getFloat((c.getColumnIndex(KEY_Z_GRAVITY))));
                model.setXGyroscope(c.getFloat((c.getColumnIndex(KEY_X_GYRO))));
                model.setYGyroscope(c.getFloat((c.getColumnIndex(KEY_Y_GYRO))));
                model.setZGyroscope(c.getFloat((c.getColumnIndex(KEY_Z_GYRO))));
                model.setXRotation(c.getFloat((c.getColumnIndex(KEY_X_ROTATION))));
                model.setYRotation(c.getFloat((c.getColumnIndex(KEY_Y_ROTATION))));
                model.setZRotation(c.getFloat((c.getColumnIndex(KEY_Z_ROTATION))));
            } while (c.moveToNext());
        }

        return model;
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
