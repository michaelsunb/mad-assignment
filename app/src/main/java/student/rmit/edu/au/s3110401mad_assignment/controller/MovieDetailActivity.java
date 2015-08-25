package student.rmit.edu.au.s3110401mad_assignment.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import student.rmit.edu.au.s3110401mad_assignment.R;
import student.rmit.edu.au.s3110401mad_assignment.model.Movie;
import student.rmit.edu.au.s3110401mad_assignment.model.MovieModel;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Bundle extras = getIntent().getExtras();
        String movieId = extras.getString(getString(R.string.movie_id));

        MovieModel theModel = MovieModel.getSingleton();
        Movie movie = theModel.getMovieById(movieId);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }
}
