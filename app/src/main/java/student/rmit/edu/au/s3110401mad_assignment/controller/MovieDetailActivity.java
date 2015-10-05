package student.rmit.edu.au.s3110401mad_assignment.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import student.rmit.edu.au.s3110401mad_assignment.R;
import student.rmit.edu.au.s3110401mad_assignment.model.async_task.EditMovieRatingDBTask;
import student.rmit.edu.au.s3110401mad_assignment.model.Movie;
import student.rmit.edu.au.s3110401mad_assignment.model.MovieModel;

public class MovieDetailActivity extends AppCompatActivity {

    private Movie movie;
    private String movieId;
    private String movieTitle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ImageView imageView = (ImageView) findViewById(R.id.movie_detail_poster);
        TextView titleView = (TextView) findViewById(R.id.movie_detail_title);
        TextView plotView = (TextView) findViewById(R.id.movie_detail_plot_view);
        try {
            Bundle extras = getIntent().getExtras();
            movieId = extras.getString(getString(R.string.movie_id));

            movie = getMovie(movieId);
            imageView.setImageBitmap(movie.getPoster());
            plotView.setText(movie.getShortPlot());
            titleView.setText(movie.getTitle());

            movieTitle = movie.getTitle();
        } catch (Exception e) {
            System.out.println("Oh no! Something happened: " + e.getMessage());
        }

        CharSequence titleChar = (movieTitle != null) ? movieTitle : getTitle();
        setTitle(titleChar);

        RatingBar ratingBar = (RatingBar) findViewById(R.id.move_detail_rating_bar);
        ratingBar.setRating(movie.getRating());
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                onRatingTouched(Math.round(rating));
            }
        });
        findViewById(R.id.add_movie_party).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity();
            }
        });

        if(getSupportActionBar() == null)
            return;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void nextActivity() {
        Intent intent = new Intent(this, CreatePartyActivity.class);
        intent.putExtra(getString(R.string.movie_id), movieId);
        startActivity(intent);
        //finish();
    }

    public Movie getMovie(String movieId) {
        MovieModel theModel = MovieModel.getSingleton();
        return theModel.getMovieById(movieId);
    }

    private void onRatingTouched(int rating) {
        if(movie == null)
            return;

        MovieModel movieModel = MovieModel.getSingleton();
        movieModel.editRating(movieId, rating);
        Movie movie = movieModel.getMovieById(movieId);
        Toast.makeText(
                getApplicationContext(),
                "Rated : " + movie.getRating() + " stars",
                Toast.LENGTH_SHORT
        ).show();

        new EditMovieRatingDBTask(this, movie, rating).execute();
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
