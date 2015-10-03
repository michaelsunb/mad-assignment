/* *
 * COSC2347 Mobile Application Development
 * Assignment 2
 * 
 * */
package student.rmit.edu.au.s3110401mad_assignment.model.chain_of_responsibility;

import student.rmit.edu.au.s3110401mad_assignment.db.MovieDatabaseManager;
import student.rmit.edu.au.s3110401mad_assignment.model.Movie;
import student.rmit.edu.au.s3110401mad_assignment.model.MovieModel;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class MovieRetrieveDB extends MovieMemoryManagementHandler {

    public MovieRetrieveDB(Context context, MovieModel movieModel){
        super(context, movieModel);
    }

    @Override
    protected boolean hasEntry(String query) {
        Log.e("Ayy lmao Class is", this.getClass().getSimpleName());

        MovieDatabaseManager edm =
                new MovieDatabaseManager(context.getApplicationContext());
        edm.open();

        if(edm.getAllMovies().size()==0) {
            edm.close();
            return false;
        }

        filteredMovies = new ArrayList<>();
        for(Movie movie : edm.getAllMovies()) {
            movieModel.addMovie(movie);
            if(Pattern.compile(query).matcher(movie.getTitle().toLowerCase()).find()) {
                filteredMovies.add(movie);
            }
        }

        edm.close();

        return (filteredMovies.size()>0);
    }
}
