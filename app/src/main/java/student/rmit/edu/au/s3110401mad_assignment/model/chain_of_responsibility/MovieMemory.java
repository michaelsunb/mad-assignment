package student.rmit.edu.au.s3110401mad_assignment.model.chain_of_responsibility;

import android.content.Context;

import java.util.ArrayList;
import java.util.regex.Pattern;

import student.rmit.edu.au.s3110401mad_assignment.model.Movie;
import student.rmit.edu.au.s3110401mad_assignment.model.MovieModel;

/**
 * Created by Michaelsun Baluyos on 25/08/2015.
 *
 *
 */
public class MovieMemory extends MovieMemoryManagementHandler
{
    public MovieMemory(Context context, MovieModel movieModel) {
        super(context, movieModel);
    }

    @Override
    protected boolean hasEntry(String query) {
        if(movieModel.getAllMovies().size()==0) return false;

        filteredMovies = new ArrayList<>();
        int i = 0;
        for(Movie movie : movieModel.getAllMovies()) {
            if(Pattern.compile(query).matcher(movie.getTitle().toLowerCase()).find()
                    && i++ < 10) {
                addFilteredMovie(movie);
            } else if(i >= 10) {
                break;
            }
        }

        return (filteredMovies.size() > 0);
    }
}