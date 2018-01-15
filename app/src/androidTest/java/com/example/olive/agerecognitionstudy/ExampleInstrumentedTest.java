package com.example.olive.agerecognitionstudy;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    SQLiteDatabase db;

    @Before
    public void clearDatabase() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        db = new DatabaseHandler(appContext).getWritableDatabase();
        db.close();
        appContext.deleteDatabase("participantsManager");
        db = new DatabaseHandler(appContext).getWritableDatabase();
    }

    @Test
    public void testDatabase() throws Exception {
        // Context of the app under test.

        assertEquals(true, db.isOpen());

    }
}
