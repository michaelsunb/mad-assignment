package student.rmit.edu.au.s3110401mad_assignment;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.Assert;

import student.rmit.edu.au.s3110401mad_assignment.db.DatabaseHelper;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    @SmallTest
    public void dropTables() {
        DatabaseHelper edbHelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = edbHelper.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseHelper.MOVIE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseHelper.PARTY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseHelper.PARTY_INVITEE_TABLE_NAME);
        Assert.assertNotNull(db);
        db.close();
    }
}