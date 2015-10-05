package student.rmit.edu.au.s3110401mad_assignment.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import student.rmit.edu.au.s3110401mad_assignment.R;
import student.rmit.edu.au.s3110401mad_assignment.controller.adapter.PartyListAdapter;
import student.rmit.edu.au.s3110401mad_assignment.model.Party;
import student.rmit.edu.au.s3110401mad_assignment.model.PartyModel;
import student.rmit.edu.au.s3110401mad_assignment.model.async_task.EditMovieRatingDBTask;
import student.rmit.edu.au.s3110401mad_assignment.model.Movie;
import student.rmit.edu.au.s3110401mad_assignment.model.MovieModel;

public class MovieDetailActivity extends AppCompatActivity {

    private Movie movie;
    private String movieId;
    private BaseAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        try {
            Bundle extras = getIntent().getExtras();
            movieId = extras.getString(getString(R.string.movie_id));

            movie = getMovie(movieId);


            View header = getLayoutInflater().inflate(R.layout.activity_movie_detail_head, null);

            ImageView imageView = (ImageView) header.findViewById(R.id.movie_detail_poster);
            TextView titleView = (TextView) header.findViewById(R.id.movie_detail_title);
            TextView yearView = (TextView) header.findViewById(R.id.movie_detail_year);
            TextView plotView = (TextView) header.findViewById(R.id.movie_detail_plot_view);

            imageView.setImageBitmap(movie.getPoster());
            plotView.setText(movie.getFullPlot());
            titleView.setText(movie.getTitle());
            yearView.setText(movie.getYear());

            ListView drawerList = (ListView) findViewById(R.id.movie_detail_party_list);
            drawerList.addHeaderView(header);

            String movieTitle = movie.getTitle();

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

            List<Party> parties = PartyModel.getSingleton().getPartiesByMovieId(movieId);
            listAdapter = new PartyListAdapter(this, parties);
            drawerList.setAdapter(listAdapter);
        } catch (Exception e) {
            System.out.println("Oh no! Something happened: " + e.getMessage());
        }

        if(getSupportActionBar() == null)
            return;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void nextActivity() {
        Intent intent = new Intent(this, PartyCreateActivity.class);
        intent.putExtra(getString(R.string.movie_id), movieId);
        startActivity(intent);
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

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        listAdapter.notifyDataSetChanged();
    }
}
