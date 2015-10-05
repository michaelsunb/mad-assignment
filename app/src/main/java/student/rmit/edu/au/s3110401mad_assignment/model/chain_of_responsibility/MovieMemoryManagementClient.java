package student.rmit.edu.au.s3110401mad_assignment.model.chain_of_responsibility;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import student.rmit.edu.au.s3110401mad_assignment.model.Movie;
import student.rmit.edu.au.s3110401mad_assignment.model.MovieModel;

/**
 * Created by Michaelsun Baluyos on 25/08/2015.
 * new MovieMemoryManagementClient(ProgressBar, String)
 *      .execute(MovieModel, DatabaseMovieModel, OmdbMovieModel);
 */
public class MovieMemoryManagementClient extends AsyncTask<String,Void,List<Movie>> {
    private Context context;
    private static MovieMemoryManagementHandler movieMemory;

    public MovieMemoryManagementClient(Context context) {
        this.context = context;
    }

    private MovieMemoryManagementHandler getMemoryHandler() {
        if(movieMemory != null) return movieMemory;

        MovieModel movieModel = MovieModel.getSingleton();

        movieMemory = new MovieMemory(context, movieModel);
        MovieMemoryManagementHandler movieRetrieveDB = new MovieRetrieveDB(context, movieModel);
        MovieMemoryManagementHandler movieOmDB = new MovieOmdbRest(context, movieModel);
        movieMemory.setNext(movieRetrieveDB);
        movieRetrieveDB.setNext(movieOmDB);
        movieOmDB.setNext(null);

        return movieMemory;
    }

    @Override
    protected List<Movie> doInBackground(final String... params) {
        getMemoryHandler().handleRequest(params[0].toLowerCase());
        return getMemoryHandler().getFilteredMovies();
    }
}
