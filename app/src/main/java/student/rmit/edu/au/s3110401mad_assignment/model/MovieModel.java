package student.rmit.edu.au.s3110401mad_assignment.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Michaelsun Baluyos on 25/08/2015.
 */
public class MovieModel {
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


    public Movie getByName(String title) {
        for (Map.Entry<String, Movie> entry : movieMap.entrySet()) {
            if(entry.getValue().getTitle().equals(title))
                return entry.getValue();
        }
        return null;
    }
    public void addMovie(Movie movie) {
        movieMap.put(movie.getId(), movie);
    }
    public void editRating(String imdbId,float rating) {
        movieMap.get(imdbId).setRating(rating);
    }
    public Movie getMovieById(String imdbId) {
        return movieMap.get(imdbId);
    }
    public List<Movie> getAllMovies() {
        return new ArrayList<Movie>(movieMap.values());
    }
}
