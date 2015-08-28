package student.rmit.edu.au.s3110401mad_assignment.controller;

import android.app.DatePickerDialog;
import android.app.Instrumentation;
import android.app.TimePickerDialog;
import android.support.v4.app.DialogFragment;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;

import java.util.Calendar;

import student.rmit.edu.au.s3110401mad_assignment.R;

/**
 * Created by Michaelsun Baluyos on 26/08/2015.
 */
public class CreatePartyActivityTest extends ActivityInstrumentationTestCase2<CreatePartyActivity> {
    private Instrumentation instrumentation;
    private CreatePartyActivity activity;

    public CreatePartyActivityTest() {
        super(CreatePartyActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        instrumentation = getInstrumentation();
        activity = getActivity();
    }

    @SmallTest
    public void testCheckDatePicker() throws Exception {
        final Button buttonDatePicker = (Button) activity.findViewById(R.id.create_party_date_picker);
        activity.runOnUiThread(new Runnable() {
            public void run() {
                buttonDatePicker.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();
        DialogFragment fragDate = activity.getDate();
        assertTrue("Date Picker should be onResume (true)", fragDate.isResumed());
        final DatePickerDialog dialogDate = (DatePickerDialog)fragDate.onCreateDialog(null);
        dialogDate.getDatePicker().updateDate(2011, 11, 1);
        fragDate.dismiss();
        getInstrumentation().waitForIdleSync();
        assertFalse("Date Picker should be not onResume (false)", fragDate.isResumed());
    }

    @SmallTest
    public void testCheckTimePicker() throws Exception {
        final Button buttonTimePicker = (Button) activity.findViewById(R.id.create_party_time_picker);
        activity.runOnUiThread(new Runnable() {
            public void run() {
                buttonTimePicker.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();
        DialogFragment fragTime = activity.getTime();
        assertTrue("Time Picker should be onResume (true)", fragTime.isResumed());
        final TimePickerDialog dialogTime = (TimePickerDialog)fragTime.onCreateDialog(null);
        dialogTime.updateTime(12, 30);
        fragTime.dismiss();
        getInstrumentation().waitForIdleSync();
        assertFalse("Time Picker should be not onResume (false)", fragTime.isResumed());
    }

    @SmallTest
    public void testMovieSelect() throws Exception {
        final Button button = (Button) activity.findViewById(R.id.create_party_movie_button);
        activity.runOnUiThread(new Runnable() {
            public void run() {
                button.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();
        assertFalse("CreatePartyActivity should not be in focus", getActivity().hasWindowFocus());
    }

    @SmallTest
    public void testInviteesSelect() throws Exception {
        final Button button = (Button) activity.findViewById(R.id.create_party_invitees_button);
        activity.runOnUiThread(new Runnable() {
            public void run() {
                button.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();
        assertFalse("CreatePartyActivity should not be in focus", getActivity().hasWindowFocus());
    }

    @SmallTest
    public void testSumbitSelect() throws Exception {
        Instrumentation.ActivityMonitor activityMonitor =
                instrumentation.addMonitor(MainActivity.class.getName(), null, false);
        final Button button = (Button) activity.findViewById(R.id.create_party_submit);
        activity.runOnUiThread(new Runnable() {
            public void run() {
                button.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();
        assertFalse("CreatePartyActivity should not be in focus", getActivity().hasWindowFocus());
        MainActivity temp = (MainActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5);
        assertNotNull(temp);
        temp.onOptionsItemSelected(null); // should go back
        getInstrumentation().waitForIdleSync();
    }
}