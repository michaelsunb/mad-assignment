package student.rmit.edu.au.s3110401mad_assignment.controller;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.ImageView;
import android.widget.RatingBar;

import student.rmit.edu.au.s3110401mad_assignment.R;

/**
 * Created by Michaelsun Baluyos on 26/08/2015.
 */
public class MovieDetailActivityTest extends ActivityInstrumentationTestCase2<MovieDetailActivity> {
    private static final String MOVIE_ID = "tt0119925";

    private Instrumentation instrumentation;
    private MovieDetailActivity activity;
    private ImageView imageView;

    public MovieDetailActivityTest() {
        super(MovieDetailActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Intent intent = new Intent();
        intent.putExtra("movie_id", MOVIE_ID);
        setActivityIntent(intent);
        instrumentation = getInstrumentation();
        activity = getActivity();
    }
}