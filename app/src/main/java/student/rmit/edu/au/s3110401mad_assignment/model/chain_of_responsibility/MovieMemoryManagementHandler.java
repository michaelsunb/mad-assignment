package student.rmit.edu.au.s3110401mad_assignment.model.chain_of_responsibility;

import android.content.Context;

import java.util.List;

import student.rmit.edu.au.s3110401mad_assignment.model.Movie;
import student.rmit.edu.au.s3110401mad_assignment.model.MovieModel;

/**
 * Created by Michaelsun Baluyos on 27/09/2015.
 *
 *
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

    public void addFilteredMovie(Movie movie) {
        if(filteredMovies.size() > 10) {
            filteredMovies.remove(0);
        }
        for (Movie movie1 : filteredMovies) {
            if(movie1.getId().equals(movie.getId())) {
                filteredMovies.remove(movie);
            }
        }
        filteredMovies.add(movie);
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