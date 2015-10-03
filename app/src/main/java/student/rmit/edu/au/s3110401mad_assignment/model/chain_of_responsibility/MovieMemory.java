package student.rmit.edu.au.s3110401mad_assignment.model.chain_of_responsibility;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import student.rmit.edu.au.s3110401mad_assignment.db.MovieDatabaseManager;
import student.rmit.edu.au.s3110401mad_assignment.model.Movie;
import student.rmit.edu.au.s3110401mad_assignment.model.MovieModel;

/**
 * Created by Michaelsun Baluyos on 25/08/2015.
 */
public class MovieMemory extends MovieMemoryManagementHandler
{
    public MovieMemory(Context context, MovieModel movieModel) {
        super(context, movieModel);
    }

    @Override
    protected boolean hasEntry(String query) {
        Log.e("Ayy lmao Class is", this.getClass().getSimpleName());

        if(movieModel.getAllMovies().size()==0) return false;

        filteredMovies = new ArrayList<>();
        for(Movie movie : movieModel.getAllMovies()) {
            if(Pattern.compile(query).matcher(movie.getTitle()).find()) {
                filteredMovies.add(movie);
            }
        }

        return (filteredMovies.size() > 0);
    }
}