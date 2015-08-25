package student.rmit.edu.au.s3110401mad_assignment.model;

import android.content.Context;
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

    private static Context context;
    private static MovieModel singleton = new MovieModel(retrieveMovies());

    private static List<MovieStruct> movieList = null;

    public static MovieModel getSingleton(Context c) {
        context = c;
        return singleton;
    }

    private Map<String,MovieStruct> movieMap;

    // Construction
    public MovieModel(List<MovieStruct> movieList) {
        this.movieMap = new HashMap<String, MovieStruct>();

        for (MovieStruct movie : movieList) {
            this.movieMap.put(movie.getId(), movie);
        }
    }

    private static List<MovieStruct> retrieveMovies() {
        if(movieList != null)
            return movieList;

        TypedArray movieArray  = context.getResources().obtainTypedArray(R.array.movie_array);

        int movieArrayLength = movieArray.length();

        List<MovieStruct> movieList = new ArrayList<>();
        for (int i = 0; i < movieArrayLength; ++i) {
            int id = movieArray.getResourceId(i, 0);
            if (id > 0) {
                String[] newArray = context.getResources().getStringArray(id);
                movieList.add(
                        new MovieStruct(newArray[0],
                                newArray[1],
                                newArray[2],
                                newArray[3],
                                newArray[4])
                );
            }
        }
        movieArray.recycle(); // Important!
        return movieList;
    }

    // Model Access
    public MovieStruct getMovieById(String imdbId)
    {
        return movieMap.get(imdbId);
    }
    public List<MovieStruct> getAllMovies()
    {
        return retrieveMovies();
    }
}
