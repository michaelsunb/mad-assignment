package student.rmit.edu.au.s3110401mad_assignment.controller;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by Michaelsun Baluyos on 26/08/2015.
 */
public class MovieDetailActivityTest extends ActivityInstrumentationTestCase2<MovieDetailActivity> {
    private Instrumentation instrumentation;
    private MovieDetailActivity activity;

    public MovieDetailActivityTest() {
        super(MovieDetailActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(false);
        instrumentation = getInstrumentation();
        activity = getActivity();
    }
}