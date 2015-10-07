package student.rmit.edu.au.s3110401mad_assignment.model.chain_of_responsibility;

import android.os.AsyncTask;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import junit.framework.Assert;

import java.util.List;

import student.rmit.edu.au.s3110401mad_assignment.controller.MainActivity;
import student.rmit.edu.au.s3110401mad_assignment.model.Movie;

/**
 * Created by Michaelsun Baluyos on 3/10/2015.
 *
 *
 */
public class MovieMemoryManagementClientTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public MovieMemoryManagementClientTest() {
        super(MainActivity.class);
    }

    @SmallTest
    public void testDoInBackground() throws Throwable {

        AsyncTask<String, Void, List<Movie>> asyncTask = new MovieMemoryManagementClient(getActivity());
        asyncTask.execute("Man of Steel");
        List<Movie> asyncTaskGet = asyncTask.get();

        Assert.assertNotNull("Async should not be null", asyncTaskGet);
        Assert.assertTrue("Async should be greater the zero", asyncTaskGet.size() > 0);

        Movie movie = asyncTaskGet.get(0);
        Log.e("Movie Id is:",movie.getId());
        Assert.assertNotNull("Poster should not be null", movie.getPoster());
    }
}