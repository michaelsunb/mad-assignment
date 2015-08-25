package student.rmit.edu.au.s3110401mad_assignment.controller;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.ImageView;

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

    @SmallTest
    public void testInstrumentsNotNull() throws Exception {
        assertNotNull("Instrument should be not null", instrumentation);
        assertNotNull("Activity should be not null", activity);

        ImageView imageView = new ImageView(activity);
        imageView.setImageResource(activity.getMovie(MOVIE_ID).getPoster());
        assertEquals("Should be the same pictures",
                ContextCompat.getDrawable(activity, R.drawable.tt0119925).getConstantState(),
                imageView.getDrawable().getConstantState());
    }

    @SmallTest
    public void testImage() {
        imageView = (ImageView) activity.findViewById(R.id.movie_detail_poster);
        assertNotNull(imageView);
        assertNotSame("Should not be the placeholder",
                ContextCompat.getDrawable(activity, R.drawable.placeholder).getConstantState(),
                imageView.getDrawable().getConstantState());
    }
}