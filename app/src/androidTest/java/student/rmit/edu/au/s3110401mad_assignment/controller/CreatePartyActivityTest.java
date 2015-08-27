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
    public void testInstrumentsNotNull() throws Exception {
        assertNotNull("Instrument should be not null",instrumentation);
        assertNotNull("Activity should be not null", activity);
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
        assertFalse("Date Picker should be not hidden", fragDate.isHidden());
        DatePickerDialog dialogDate = (DatePickerDialog)fragDate.onCreateDialog(null);
        dialogDate.updateDate(2011, 11, 1);

        assertEquals("Year should equal 2011", 2011, activity.getCalendar().get(Calendar.YEAR));
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
        assertFalse("Time Picker should be not hidden", fragTime.isHidden());
        TimePickerDialog dialogTime = (TimePickerDialog)fragTime.onCreateDialog(null);
        dialogTime.updateTime(12, 30);

        assertEquals("Hour should equal 12", 12, activity.getCalendar().get(Calendar.HOUR_OF_DAY));
    }
}