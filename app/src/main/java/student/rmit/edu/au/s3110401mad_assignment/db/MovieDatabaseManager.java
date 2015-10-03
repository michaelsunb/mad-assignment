/* *
 * COSC2347 Mobile Application Development
 * Assignment 2
 *
 * */
package student.rmit.edu.au.s3110401mad_assignment.db;

import java.util.List;

import student.rmit.edu.au.s3110401mad_assignment.model.Movie;
import student.rmit.edu.au.s3110401mad_assignment.model.MovieStruct;
import student.rmit.edu.au.s3110401mad_assignment.model.MovieModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MovieDatabaseManager {
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

        Log.i("Ayy lmao", "I am adding a Movie");

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.MOVIE_ID, newMovie.getId());
        values.put(DatabaseHelper.MOVIE_TITLE, newMovie.getTitle());
        values.put(DatabaseHelper.MOVIE_YEAR, newMovie.getYear());
        values.put(DatabaseHelper.MOVIE_SHORT_PLOT, newMovie.getShortPlot());
        values.put(DatabaseHelper.MOVIE_FULL_PLOT, newMovie.getFullPlot());
        values.put(DatabaseHelper.MOVIE_IMAGE_BITMAP,
                MovieModel.bitMapToString(newMovie.getPoster()));
        values.put(DatabaseHelper.MOVIE_RATING, newMovie.getRating());

        database.insert(DatabaseHelper.MOVIE_TABLE_NAME, null, values);
    }

    public List<Movie> getAllMovies(){
        MovieModel movieModel = MovieModel.getSingleton();

        Cursor cursor = database.query(DatabaseHelper.MOVIE_TABLE_NAME,
                null, null, null, null, null, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            movieModel.addMovie(cursorToMovie(cursor));
            cursor.moveToNext();
        }

        return movieModel.getAllMovies();
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

    public void deleteMovie(String id){
        String selectionToBeDeleted = DatabaseHelper.MOVIE_ID + " = \"" + id + "\"";
        database.delete(DatabaseHelper.MOVIE_TABLE_NAME, selectionToBeDeleted, null);
        MovieModel.getSingleton().deleteMovie(id);
    }

    public void editMovie(Movie event){
        String whereClause = DatabaseHelper.MOVIE_ID + " = \"" + event.getId() + "\"" ;

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.MOVIE_ID, event.getId());
        contentValues.put(DatabaseHelper.MOVIE_TITLE, event.getTitle());
        contentValues.put(DatabaseHelper.MOVIE_YEAR, event.getYear());
        contentValues.put(DatabaseHelper.MOVIE_SHORT_PLOT, event.getShortPlot());
        contentValues.put(DatabaseHelper.MOVIE_FULL_PLOT, event.getFullPlot());
        contentValues.put(DatabaseHelper.MOVIE_IMAGE_BITMAP,
                MovieModel.bitMapToString(event.getPoster()));
        contentValues.put(DatabaseHelper.MOVIE_RATING, event.getRating());

        database.update(DatabaseHelper.MOVIE_TABLE_NAME, contentValues, whereClause, null);
    }
}
