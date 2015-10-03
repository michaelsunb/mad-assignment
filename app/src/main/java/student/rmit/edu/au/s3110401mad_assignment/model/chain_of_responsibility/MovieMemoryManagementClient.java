package student.rmit.edu.au.s3110401mad_assignment.model.chain_of_responsibility;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import student.rmit.edu.au.s3110401mad_assignment.R;
import student.rmit.edu.au.s3110401mad_assignment.controller.adapter.MovieArrayAdapter;
import student.rmit.edu.au.s3110401mad_assignment.model.Movie;
import student.rmit.edu.au.s3110401mad_assignment.model.MovieModel;
import student.rmit.edu.au.s3110401mad_assignment.view.MovieListFragment;

import static java.lang.Thread.sleep;

/**
 * Created by Michaelsun Baluyos on 25/08/2015.
 * new MovieMemoryManagementClient(ProgressBar, String)
 *      .execute(MovieModel, DatabaseMovieModel, OmdbMovieModel);
 */
public class MovieMemoryManagementClient extends AsyncTask<String,Void,List<Movie>> {
    private Context context;

    public MovieMemoryManagementClient(Context context) {
        this.context = context;
    }

    private MovieMemoryManagementHandler getMemoryHandler() {
        MovieModel movieModel = MovieModel.getSingleton();

        MovieMemoryManagementHandler movieMemory = new MovieMemory(context, movieModel);
        MovieMemoryManagementHandler movieRetrieveDB = new MovieRetrieveDB(context, movieModel);
        MovieMemoryManagementHandler movieOmDB = new MovieOmDB(context, movieModel);
        movieMemory.setNext(movieRetrieveDB);
        movieRetrieveDB.setNext(movieOmDB);
        movieOmDB.setNext(null);

        return movieMemory;
    }

    @Override
    protected List<Movie> doInBackground(String... params) {
//        try { sleep(9999); } catch (InterruptedException e) { e.printStackTrace(); } // TODO: remove. Used to simulate request
        getMemoryHandler().handleRequest(params[0].toLowerCase());
        return getMemoryHandler().getFilteredMovies();
    }
}
