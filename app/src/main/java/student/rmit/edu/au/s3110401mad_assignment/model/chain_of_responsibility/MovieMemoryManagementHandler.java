package student.rmit.edu.au.s3110401mad_assignment.model.chain_of_responsibility;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;
import java.util.Map;

import student.rmit.edu.au.s3110401mad_assignment.model.Movie;
import student.rmit.edu.au.s3110401mad_assignment.model.MovieModel;

/**
 * Created by Michaelsun Baluyos on 27/09/2015.
 */
public abstract class MovieMemoryManagementHandler {
    private MovieMemoryManagementHandler next = null;
    protected static List<Movie> filteredMovies = null;
    protected MovieModel movieModel;
    protected Context context;

    public MovieMemoryManagementHandler(Context context, MovieModel movieModel) {
        this.context = context;
        this.movieModel = movieModel;
    }

    public void setNext(MovieMemoryManagementHandler handler) {
        this.next = handler;
    }

    public void handleRequest(String query) {
        if(hasEntry(query)) return;
        if(next == null) return;

        next.handleRequest(query);
    }

    protected abstract boolean hasEntry(String query);
    public List<Movie> getFilteredMovies() {
        return filteredMovies;
    }
}