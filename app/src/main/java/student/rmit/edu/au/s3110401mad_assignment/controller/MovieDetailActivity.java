package student.rmit.edu.au.s3110401mad_assignment.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import student.rmit.edu.au.s3110401mad_assignment.R;
import student.rmit.edu.au.s3110401mad_assignment.model.Movie;
import student.rmit.edu.au.s3110401mad_assignment.model.MovieModel;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ImageView imageView = (ImageView) findViewById(R.id.movie_detail_poster);
        TextView titleView = (TextView) findViewById(R.id.movie_detail_title);
        TextView plotView = (TextView) findViewById(R.id.movie_detail_plot_view);
        try {
            Bundle extras = getIntent().getExtras();
            String movieId = extras.getString(getString(R.string.movie_id));

            Movie movie = getMovie(movieId);
            imageView.setImageResource(movie.getPoster());
            plotView.setText(movie.getShortPlot());
            titleView.setText(movie.getTitle());
        } catch (Exception e) {
            System.out.println("Oh no! Something happened: " + e.getMessage());
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    public Movie getMovie(String movieId) {
        MovieModel theModel = MovieModel.getSingleton();
        return theModel.getMovieById(movieId);
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
