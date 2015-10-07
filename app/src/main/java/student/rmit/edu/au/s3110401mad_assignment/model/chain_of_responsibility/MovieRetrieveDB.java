package student.rmit.edu.au.s3110401mad_assignment.model.chain_of_responsibility;

import student.rmit.edu.au.s3110401mad_assignment.model.database.MovieDatabaseManager;
import student.rmit.edu.au.s3110401mad_assignment.model.Movie;
import student.rmit.edu.au.s3110401mad_assignment.model.MovieModel;

import android.content.Context;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class MovieRetrieveDB extends MovieMemoryManagementHandler {

    public MovieRetrieveDB(Context context, MovieModel movieModel){
        super(context, movieModel);
    }

    @Override
    protected boolean hasEntry(String query) {
        MovieDatabaseManager edm =
                new MovieDatabaseManager(context.getApplicationContext());
        edm.open();

        if(edm.getAllMovies().size()==0) {
            edm.close();
            return false;
        }

        filteredMovies = new ArrayList<>();
        int i = 0;
        for(Movie movie : edm.getAllMovies()) {
            if(Pattern.compile(query).matcher(movie.getTitle().toLowerCase()).find()
                    && i++ < 10) {
                movieModel.addMovie(movie,query);
                addFilteredMovie(movie);
            } else if(i >= 10) {
                break;
            }
        }

        edm.close();

        return (filteredMovies.size()>0);
    }
}
