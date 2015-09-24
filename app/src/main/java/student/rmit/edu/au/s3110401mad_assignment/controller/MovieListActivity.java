package student.rmit.edu.au.s3110401mad_assignment.controller;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import student.rmit.edu.au.s3110401mad_assignment.R;
import student.rmit.edu.au.s3110401mad_assignment.controller.adapter.MovieArrayAdapter;
import student.rmit.edu.au.s3110401mad_assignment.model.Movie;
import student.rmit.edu.au.s3110401mad_assignment.model.MovieModel;
import student.rmit.edu.au.s3110401mad_assignment.model.MovieStruct;
import student.rmit.edu.au.s3110401mad_assignment.model.PartyModel;

public class MovieListActivity extends AppCompatActivity {
    public static final String DRAWABLE = "drawable";
    public static final int IMDB_ID = 0;
    public static final int IMDB_TITLE = 1;
    public static final int IMDB_YEAR = 2;
    public static final int IMDB_SHORT_PLOT = 3;
    public static final int IMDB_FULL_PLOT = 4;

    private static MovieModel theModel = MovieModel.getSingleton();
    private ListView movieListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context context = this;
        findViewById(R.id.main_create_party).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CreatePartyActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.see_invites).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToEventListActivity();
            }
        });

        movieListView = (ListView) findViewById(R.id.movie_list);
        this.retrieveMovies(); // load from movies_sample.xml

        MovieArrayAdapter movieArrayAdapter = new MovieArrayAdapter(this, theModel.getAllMovies());
        movieListView.setAdapter(movieArrayAdapter);

        AdapterView.OnItemClickListener listener = new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movieSelected = (Movie) movieListView.getItemAtPosition(position);
                goToMovieDetailActivity(movieSelected);
            }
        };
        movieListView.setOnItemClickListener(listener);
    }

    public MovieModel getTheModel() {
        return theModel;
    }

    @Override
    public void onResume() {
        super.onResume();
        movieListView.setAdapter(new MovieArrayAdapter(this, theModel.getAllMovies()));
    }

    private void goToMovieDetailActivity(Movie movieSelected) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(getString(R.string.movie_id), movieSelected.getId());
        startActivityForResult(intent, 1);
    }

    private void goToEventListActivity() {
        if(PartyModel.getSingleton().getAllParties().size() <= 0) {
            Toast.makeText(
                    this,"No events happening!",
                    Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(this, PartyListActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Get hard coded values from /res/values/movies_sample.xml
     */
    private void retrieveMovies() {
        if(theModel.getAllMovies().size() > 0)
            return;
        TypedArray movieArray  = getResources().obtainTypedArray(R.array.movie_array);
        int movieArrayLength = movieArray.length();
        for (int i = 0; i < movieArrayLength; ++i) {
            int id = movieArray.getResourceId(i, 0);
            if (id > 0) {
                String[] newArray = getResources().getStringArray(id);
                int imageResourceId = getResources().getIdentifier(
                        newArray[IMDB_ID],
                        DRAWABLE,
                        getPackageName());

                theModel.addMovie(new MovieStruct(
                        newArray[IMDB_ID],
                        newArray[IMDB_TITLE],
                        newArray[IMDB_YEAR],
                        newArray[IMDB_SHORT_PLOT],
                        newArray[IMDB_FULL_PLOT],
                        imageResourceId));
            }
        }
        movieArray.recycle(); // Important!
    }
}
