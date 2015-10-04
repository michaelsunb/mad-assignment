package student.rmit.edu.au.s3110401mad_assignment.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
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

    private Map<String,Movie> movieListMap;

    private MovieModel() {
        this.movieListMap = new HashMap<String, Movie>();
    }

    public Movie getByName(String title) {
        for (Map.Entry<String, Movie> entry : movieListMap.entrySet()) {
            if(entry.getValue().getTitle().equals(title))
                return entry.getValue();
        }
        return null;
    }

    private String query = "";
    public void addMovie(Movie movie,String query) {
        if(!this.query.equals(query)) {
            movieListMap = new HashMap<>();
            this.query = query;
        }
        if(movieListMap.size() > 10) {
            movieListMap.remove((String) movieListMap.keySet().toArray()[0]);
        }
        movieListMap.put(movie.getId(), movie);
    }
    public void editRating(String imdbId,int rating) {
        movieListMap.get(imdbId).setRating(rating);
    }
    public Movie getMovieById(String imdbId) {
        return movieListMap.get(imdbId);
    }
    public Map<String,Movie> getMovieListMap() {
        return movieListMap;
    }
    public List<Movie> getAllMovies() {
        return new ArrayList<Movie>(movieListMap.values());
    }

    public static String bitMapToString(Bitmap bitmap){
        if(bitmap == null) return null;

        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        byte [] b=baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input) {
        if(input == null || input.equals("")) return null;
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public void deleteMovie(String id) {
        // TODO: add functionality
    }
}
