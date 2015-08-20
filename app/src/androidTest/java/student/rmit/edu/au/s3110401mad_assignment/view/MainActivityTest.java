package student.rmit.edu.au.s3110401mad_assignment.view;

import android.app.Instrumentation;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.View;
import android.widget.ListView;

import student.rmit.edu.au.s3110401mad_assignment.R;

/**
 * Created by Michaelsun Baluyos on 10/08/2015.
 * instructions: https://youtu.be/ar28jvdoTnY?t=761
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private static String TAG = MainActivity.class.getSimpleName();
    private Instrumentation instrumentation;
    private MainActivity activity;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        instrumentation = getInstrumentation();
        activity = getActivity();
    }

    @SmallTest
    public void testSSwipeNavDrawer() throws Exception {
        swipeNavDrawhichSide(GravityCompat.START);
        ListView drawerList = (ListView) activity.findViewById(R.id.left_drawer);
        TouchUtils.clickView(this, drawerList.getChildAt(0));

        assertEquals("Activity should be Main activity",activity,getActivity());
    }

    private void swipeNavDrawhichSide(int whichSide) {
        int[] xy = new int[2];
        View v = activity.getCurrentFocus();
        v.getLocationOnScreen(xy);
        final int viewWidth = v.getWidth();
        final int viewHeight = v.getHeight();
        final float x = xy[0] + (viewWidth / 2.0f);
        float fromY = xy[1] + (viewHeight / 2.0f);
        int screenWidth = activity.getWindowManager().getDefaultDisplay().getWidth();
        //Drag from centre of screen to Leftmost edge of
        if(whichSide == GravityCompat.END)
            TouchUtils.drag(this, (screenWidth - 1), x, fromY, fromY, 5);
        else if(whichSide == GravityCompat.START)
            TouchUtils.drag(this, 0, x, fromY, fromY, 5);
    }
}