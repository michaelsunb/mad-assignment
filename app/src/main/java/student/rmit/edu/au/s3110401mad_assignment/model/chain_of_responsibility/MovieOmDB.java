package student.rmit.edu.au.s3110401mad_assignment.model.chain_of_responsibility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import student.rmit.edu.au.s3110401mad_assignment.db.MovieDatabaseManager;
import student.rmit.edu.au.s3110401mad_assignment.model.Movie;
import student.rmit.edu.au.s3110401mad_assignment.model.MovieModel;
import student.rmit.edu.au.s3110401mad_assignment.model.MovieStruct;

/**
 * Created by Michaelsun Baluyos on 1/10/2015.
 * https://www.youtube.com/watch?v=mIOxaSKiq0c
 */
public class MovieOmDB extends MovieMemoryManagementHandler  {
    public static final String OMDB_URL_SEARCH = "http://www.omdbapi.com/?r=json&s=";
    public static final String OMDB_URL_SHORT_BY_ID = "http://www.omdbapi.com/?r=json&i=";
    public static final String OMDB_URL_FULL_BY_ID = "http://www.omdbapi.com/?r=json&plot=full&i=";

    public MovieOmDB(Context context, MovieModel movieModel) {
        super(context, movieModel);
    }
    public MovieOmDB() {
        super(null, MovieModel.getSingleton());
    }

    @Override
    protected boolean hasEntry(String query) {
        Log.e("Ayy lmao Class is", this.getClass().getSimpleName());

        String json = getJsonFromHttp(OMDB_URL_SEARCH,query);

        MovieDatabaseManager db = null;
        try {
            JSONObject obj = (JSONObject) new JSONTokener(json).nextValue();
            JSONArray objSearch = obj.getJSONArray("Search");

            filteredMovies = new ArrayList<>();
            db = new MovieDatabaseManager(context);
            db.open();

            for(int i = 0; i < objSearch.length(); i++) {
                JSONObject movieJsonObject = objSearch.getJSONObject(i);
                String imdbID = movieJsonObject.getString("imdbID");
                String shortJson = getJsonFromHttp(OMDB_URL_SHORT_BY_ID, imdbID);
                String fullJson = getJsonFromHttp(OMDB_URL_FULL_BY_ID, imdbID);

                JSONObject objShort = (JSONObject) new JSONTokener(shortJson).nextValue();
                JSONObject objFull = (JSONObject) new JSONTokener(fullJson).nextValue();

                Movie movie = new MovieStruct(
                        imdbID,
                        movieJsonObject.getString("Title"),
                        movieJsonObject.getString("Year"),
                        objShort.getString("Plot"),
                        objFull.getString("Plot"),
                        getBitmapFromURL(objFull.getString("Poster")),
                        0
                );
                movieModel.addMovie(movie);
                if(Pattern.compile(query).matcher(movie.getTitle().toLowerCase()).find()) {
                    db.addMovie(movie);
                    filteredMovies.add(movie);
                }
            }
            return true;
        } catch (Throwable t) {
//            Log.e("My App", "Could not parse malformed JSON: \"" + json + "\"");
            t.printStackTrace();
        } finally {
            if(db!=null) db.close();
        }
        return false;
    }

    public Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("Bitmap Url is",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
//            e.printStackTrace();
            Log.e("No Bitmap. Url is",src);
            return null;
        }
    }

    /**
     * @param omdbUrl
     * @param query
     * @return String
     */
    public String getJsonFromHttp(String omdbUrl, String query) {
        BufferedReader reader = null;

        try {
            URL url = new URL(omdbUrl + query.replace(" ", "%20"));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }

            return sb.toString();
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
}
