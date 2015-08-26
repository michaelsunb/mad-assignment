package student.rmit.edu.au.s3110401mad_assignment.controller;


import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

/**
 * Created by Michaelsun Baluyos on 26/08/2015.
 */
public class CreatePartyActivityTest extends ActivityInstrumentationTestCase2<CreatePartyActivity> {
    private Instrumentation instrumentation;
    private Activity activity;

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
}