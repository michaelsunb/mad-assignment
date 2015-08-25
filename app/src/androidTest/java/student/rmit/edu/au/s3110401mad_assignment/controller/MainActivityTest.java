package student.rmit.edu.au.s3110401mad_assignment.controller;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
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
        list = (ListView) activity.findViewById(R.id.listView);
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
    public void testListViewClick() {
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
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        getInstrumentation().waitForIdleSync();
        MovieDetailActivity childActivity = (MovieDetailActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5);
        // ChildActivity is created and gain focus on screen:
        assertNotNull(childActivity);
        childActivity.onOptionsItemSelected(null);
    }
}