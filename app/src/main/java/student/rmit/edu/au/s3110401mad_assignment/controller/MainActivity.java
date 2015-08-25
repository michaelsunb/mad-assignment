package student.rmit.edu.au.s3110401mad_assignment.controller;

import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;

import java.util.ArrayList;
import java.util.List;

import student.rmit.edu.au.s3110401mad_assignment.R;
import student.rmit.edu.au.s3110401mad_assignment.model.Movie;
import student.rmit.edu.au.s3110401mad_assignment.model.MovieModel;
import student.rmit.edu.au.s3110401mad_assignment.model.MovieStruct;

public class MainActivity extends AppCompatActivity {
    public static final String DRAWABLE = "drawable";
    public static final int IMDB_ID = 0;
    public static final int IMDB_TITLE = 1;
    public static final int IMDB_YEAR = 2;
    public static final int IMDB_SHORT_PLOT = 3;
    public static final int IMDB_FULL_PLOT = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView movieListView = (ListView) findViewById(R.id.listView);

        MovieModel theModel = MovieModel.getSingleton();
        for (Movie movie : retrieveMovies()) {
            theModel.addMovie(movie);
        }
        MovieArrayAdapter movieArrayAdapter = new MovieArrayAdapter(this, theModel.getAllMovies());
        movieListView.setAdapter(movieArrayAdapter);

        AdapterView.OnItemClickListener listener = new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movieSelected = (Movie) movieListView.getItemAtPosition(position);
                nextActivity(movieSelected);
            }
        };
        movieListView.setOnItemClickListener(listener);

    }

    private void nextActivity(Movie movieSelected) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(getString(R.string.movie_id), movieSelected.getId());
        startActivity(intent);
        //finish();
    }

    /**
     * Get from movies_sample.xml
     * @return List<Movie>  List of movies from the movies_sample.xml
     */
    private List<Movie> retrieveMovies() {
        TypedArray movieArray  = getResources().obtainTypedArray(R.array.movie_array);

        int movieArrayLength = movieArray.length();

        List<Movie> movieList = new ArrayList<>();
        for (int i = 0; i < movieArrayLength; ++i) {
            int id = movieArray.getResourceId(i, 0);
            if (id > 0) {
                String[] newArray = getResources().getStringArray(id);
                int imageResourceId = getResources().getIdentifier(newArray[IMDB_ID],
                        DRAWABLE,
                        getPackageName());
                movieList.add(
                        new MovieStruct(newArray[IMDB_ID],
                                newArray[IMDB_TITLE],
                                newArray[IMDB_YEAR],
                                newArray[IMDB_SHORT_PLOT],
                                newArray[IMDB_FULL_PLOT],
                                imageResourceId)
                );
            }
        }
        movieArray.recycle(); // Important!
        return movieList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
