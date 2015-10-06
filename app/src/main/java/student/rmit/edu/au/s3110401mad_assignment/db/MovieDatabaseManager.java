package student.rmit.edu.au.s3110401mad_assignment.db;

import java.util.ArrayList;
import java.util.List;

import student.rmit.edu.au.s3110401mad_assignment.model.Movie;
import student.rmit.edu.au.s3110401mad_assignment.model.MovieStruct;
import student.rmit.edu.au.s3110401mad_assignment.model.MovieModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class MovieDatabaseManager {
    // 89 characters seems to be the max length before 10x of these short plots bugs out
    // 70 should not be more than 2 lines
    public static final int SHORT_PLOT_MAX_LENGTH = 70;

    private static SQLiteDatabase database;
    private static DatabaseHelper edbHelper;

    public MovieDatabaseManager(Context context){
        edbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException{
        database = edbHelper.getWritableDatabase();
    }

    public void close(){
        database.close();
    }

    public void addMovie(Movie newMovie){

        if(database.rawQuery("SELECT * FROM " + DatabaseHelper.MOVIE_TABLE_NAME
                + " WHERE " + DatabaseHelper.MOVIE_ID + " = \""
                + newMovie.getId() + "\"",null).getCount() != 0) return;

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.MOVIE_ID, newMovie.getId());
        values.put(DatabaseHelper.MOVIE_TITLE, newMovie.getTitle());
        values.put(DatabaseHelper.MOVIE_YEAR, newMovie.getYear());

        values.put(DatabaseHelper.MOVIE_SHORT_PLOT,
                (newMovie.getShortPlot().length()> SHORT_PLOT_MAX_LENGTH) ?
                        newMovie.getShortPlot().substring(0, SHORT_PLOT_MAX_LENGTH) + "..." :
                        newMovie.getShortPlot());

        values.put(DatabaseHelper.MOVIE_FULL_PLOT, newMovie.getFullPlot());
        values.put(DatabaseHelper.MOVIE_IMAGE_BITMAP,
                MovieModel.bitMapToString(newMovie.getPoster()));
        values.put(DatabaseHelper.MOVIE_RATING, newMovie.getRating());

        database.insert(DatabaseHelper.MOVIE_TABLE_NAME, null, values);
    }

    public List<Movie> getAllMovies(){
        Cursor cursor = database.query(DatabaseHelper.MOVIE_TABLE_NAME,
                null, null, null, null, null, null);

        cursor.moveToFirst();
        List<Movie> movies = new ArrayList<>();
        while(!cursor.isAfterLast()){
            movies.add(cursorToMovie(cursor));
            cursor.moveToNext();
        }

        return movies;
    }

    private Movie cursorToMovie(Cursor movieCursor){

        String movieId = movieCursor.getString(movieCursor.getColumnIndex(DatabaseHelper.MOVIE_ID));
        String movieTitle = movieCursor.getString(
                movieCursor.getColumnIndex(DatabaseHelper.MOVIE_TITLE));
        String movieYear = movieCursor.getString(
                movieCursor.getColumnIndex(DatabaseHelper.MOVIE_YEAR));
        String shortPlot = movieCursor.getString(
                movieCursor.getColumnIndex(DatabaseHelper.MOVIE_SHORT_PLOT));
        String fullPlot = movieCursor.getString(
                movieCursor.getColumnIndex(DatabaseHelper.MOVIE_FULL_PLOT));
        String bitmapImage = movieCursor.getString(
                movieCursor.getColumnIndex(DatabaseHelper.MOVIE_IMAGE_BITMAP));
        Integer rating = movieCursor.getInt(
                movieCursor.getColumnIndex(DatabaseHelper.MOVIE_RATING));

        return new MovieStruct(movieId, movieTitle, movieYear, shortPlot, fullPlot,
                MovieModel.decodeBase64(bitmapImage), rating
        );
    }

    public void editMovie(Movie movie){
        String whereClause = DatabaseHelper.MOVIE_ID + " = \"" + movie.getId() + "\"" ;

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.MOVIE_ID, movie.getId());
        contentValues.put(DatabaseHelper.MOVIE_TITLE, movie.getTitle());
        contentValues.put(DatabaseHelper.MOVIE_YEAR, movie.getYear());
        contentValues.put(DatabaseHelper.MOVIE_SHORT_PLOT, movie.getShortPlot());
        contentValues.put(DatabaseHelper.MOVIE_FULL_PLOT, movie.getFullPlot());
        contentValues.put(DatabaseHelper.MOVIE_IMAGE_BITMAP,
                MovieModel.bitMapToString(movie.getPoster()));
        contentValues.put(DatabaseHelper.MOVIE_RATING, movie.getRating());

        database.update(DatabaseHelper.MOVIE_TABLE_NAME, contentValues, whereClause, null);
    }
}
