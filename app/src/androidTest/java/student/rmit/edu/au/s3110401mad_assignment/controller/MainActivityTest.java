package student.rmit.edu.au.s3110401mad_assignment.controller;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import student.rmit.edu.au.s3110401mad_assignment.R;

/**
 * Created by Michaelsun Baluyos on 10/08/2015.
 * instructions: https://youtu.be/ar28jvdoTnY?t=761
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private Instrumentation instrumentation;
    private MainActivity activity;
    private ListView list;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        instrumentation = getInstrumentation();
        activity = getActivity();
        list = (ListView) activity.findViewById(R.id.movie_list);
    }

    @SmallTest
    public void testListViewRow() {
        assertNotNull(list);
        for (int i = 0; i <= list.getLastVisiblePosition() - list.getFirstVisiblePosition(); i++) {
            assertTrue("Is type of LinearLayout", (list.getChildAt(i) instanceof LinearLayout));
            assertNotSame("Should not equal placeholder",
                    ((TextView)list.getChildAt(i).findViewById(R.id.title)).getText(),
                    R.string.placeholder);
        }
    }

    @SmallTest
    public void testListViewRowRating() {
        MovieDetailActivity childActivity = getMovieDetailActivity();
        // ChildActivity is created and gain focus on screen:
        assertNotNull(childActivity);
        RatingBar ratingBar = (RatingBar)childActivity.findViewById(R.id.move_detail_rating_bar);
        ratingBar.setRating(4f);
        childActivity.onOptionsItemSelected(null);
        assertEquals("Rating should equal 4f", 4f,
                activity.getTheModel().getMovieById(childActivity.getMovieId()).getRating());
    }

    private MovieDetailActivity getMovieDetailActivity() {
        Instrumentation.ActivityMonitor activityMonitor =
                instrumentation.addMonitor(MovieDetailActivity.class.getName(), null, false);
        assertNotNull(list.getChildAt(0));
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                list.performItemClick(list.getAdapter().getView(0, null, null),
                        0, list.getAdapter().getItemId(0));
            }
        });
        getInstrumentation().waitForIdleSync();
        return (MovieDetailActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5);
    }
}