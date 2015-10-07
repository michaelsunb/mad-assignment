package student.rmit.edu.au.s3110401mad_assignment.model.async_task;

import student.rmit.edu.au.s3110401mad_assignment.model.database.MovieDatabaseManager;
import student.rmit.edu.au.s3110401mad_assignment.model.Movie;
import student.rmit.edu.au.s3110401mad_assignment.model.MovieModel;

import android.content.Context;
import android.os.AsyncTask;

public class EditMovieRatingDBTask extends AsyncTask<Void, Void, Void> {
    private Movie movie;
    private Context context;
    private int rating;

    public EditMovieRatingDBTask(Context context, Movie movie, int rating) {
        this.movie = movie;
        this.context = context;
        this.rating = rating;
    }

    @Override
    protected Void doInBackground(Void... params) {
        MovieDatabaseManager edbm = new MovieDatabaseManager(context);
        edbm.open();
        edbm.editMovie(movie);
        MovieModel.getSingleton().editRating(movie.getId(), rating);
        edbm.close();
        return null;
    }
}
