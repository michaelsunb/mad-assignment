package student.rmit.edu.au.s3110401mad_assignment.model.async_task;

import android.database.MatrixCursor;
import android.os.AsyncTask;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import student.rmit.edu.au.s3110401mad_assignment.db.DatabaseHelper;
import student.rmit.edu.au.s3110401mad_assignment.model.Movie;
import student.rmit.edu.au.s3110401mad_assignment.model.MovieModel;

/**
 * Created by Michaelsun Baluyos on 5/10/2015.
 *
 *
 */
public class ListToMatrixCursorTask extends AsyncTask<Void,Void,MatrixCursor> {

    private List<Movie> allMovies;

    public ListToMatrixCursorTask(List<Movie> allMovies) {
        this.allMovies = allMovies;
    }

    @Override
    protected MatrixCursor doInBackground(Void... params) {
        if(allMovies == null) return null;
        MatrixCursor matrixCursor = new MatrixCursor(DatabaseHelper.MOVIE_SUMMARY_PROJECTION);

        long i = 0;
        for(Movie movie : allMovies) {
            if(i++ > 10) break;
            Matcher movieIdNo = Pattern.compile("\\d+$").matcher(movie.getId());
            matrixCursor.addRow(new String[]{
                    // Some reason Cursor looks for double, so i regex "\d+$" to get that double
                    ((movieIdNo.find()) ? movieIdNo.group(0) : i + ""),
                    movie.getId(),
                    movie.getTitle(),
                    movie.getYear(),
                    movie.getShortPlot(),
                    MovieModel.bitMapToString(movie.getPoster()),
                    movie.getRating() + ""
            });
        }
        return matrixCursor;
    }
}