package student.rmit.edu.au.s3110401mad_assignment.controller.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import student.rmit.edu.au.s3110401mad_assignment.R;
import student.rmit.edu.au.s3110401mad_assignment.model.Movie;
import student.rmit.edu.au.s3110401mad_assignment.model.PartyModel;

/**
 * Created by Michaelsun Baluyos on 25/08/2015.
 */
public class MovieArrayAdapter extends ArrayAdapter<Movie> {
    public static final String MAIN_ROW_SPACE = " ";
    public static final String MAIN_ROW_RATING_OUT_OF_FIVE = "/5";

    // Reference Controller
    private Activity activity;

    public MovieArrayAdapter(Activity activity, List<Movie> allMovies) {
        super(activity, R.layout.activity_movie_list, allMovies);

        this.activity = activity;
    }

    @Override
    public View getView(int position, View cachedView, ViewGroup parent)
    {
        View movieItemView;

        if (cachedView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            movieItemView = inflater.inflate(R.layout.movie_row, parent, false);
        } else {
            movieItemView = cachedView;
        }

        // Get The model
        Movie movie = getItem(position);

        // Get our views
        TextView titleView = (TextView) movieItemView.findViewById(R.id.title);
        TextView yearView = (TextView) movieItemView.findViewById(R.id.year);
        TextView plotView = (TextView) movieItemView.findViewById(R.id.plot);
        ImageView imageView = (ImageView) movieItemView.findViewById(R.id.imageView);
        TextView numPartyView = (TextView) movieItemView.findViewById(R.id.num_of_invitees);
        TextView ratingBar = (TextView) movieItemView.findViewById(R.id.rating_bar);

        // Fill in the Views with content
        titleView.setText(movie.getTitle());
        yearView.setText(movie.getYear());
        plotView.setText(movie.getShortPlot());
        imageView.setImageResource(movie.getPoster());

        StringBuilder sbRating = new StringBuilder();
        sbRating.append(activity.getString(R.string.main_rating));
        sbRating.append(MAIN_ROW_SPACE);
        sbRating.append(movie.getRating());
        sbRating.append(MAIN_ROW_RATING_OUT_OF_FIVE);
        ratingBar.setText(sbRating.toString());

        StringBuilder sbParties = new StringBuilder();
        sbParties.append(activity.getString(R.string.main_num_parties));
        sbParties.append(MAIN_ROW_SPACE);
        sbParties.append(PartyModel.getSingleton().getPartiesByMovieId(movie.getId()).size());
        numPartyView.setText(sbParties.toString());

        return movieItemView;
    }
}
