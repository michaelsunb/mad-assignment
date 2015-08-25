package student.rmit.edu.au.s3110401mad_assignment.model;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import student.rmit.edu.au.s3110401mad_assignment.R;

/**
 * Created by Michaelsun Baluyos on 25/08/2015.
 */
public class MovieModel {
    private static final int MAXIMUM_RATING = 5;
    private static final int MINIMUM_RATING = 0;

    private static MovieModel singleton;

    public static MovieModel getSingleton() {
        if(singleton == null)
            singleton = new MovieModel();
        return singleton;
    }

    private Map<String,Movie> movieMap;

    private MovieModel() {
        this.movieMap = new HashMap<String, Movie>();
    }

    public void addMovie(Movie movie) {
        movieMap.put(movie.getId(), movie);
    }
    public Movie getMovieById(String imdbId)
    {
        return movieMap.get(imdbId);
    }
    public List<Movie> getAllMovies() {
        return new ArrayList(movieMap.values());
    }
}
