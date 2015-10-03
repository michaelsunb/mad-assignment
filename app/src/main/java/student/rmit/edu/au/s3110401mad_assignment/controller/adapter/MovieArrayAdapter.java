package student.rmit.edu.au.s3110401mad_assignment.controller.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import student.rmit.edu.au.s3110401mad_assignment.R;
import student.rmit.edu.au.s3110401mad_assignment.db.DatabaseHelper;
import student.rmit.edu.au.s3110401mad_assignment.model.Movie;
import student.rmit.edu.au.s3110401mad_assignment.model.MovieModel;
import student.rmit.edu.au.s3110401mad_assignment.model.PartyModel;

/**
 * Created by Michaelsun Baluyos on 25/08/2015.
 */
public class MovieArrayAdapter extends CursorAdapter {
    public static final String MAIN_ROW_SPACE = " ";
    public static final String MAIN_ROW_RATING_OUT_OF_FIVE = "/5";

    // Reference Controller
    private Context context;

    public MovieArrayAdapter(Context context, Cursor cursor) {
        super(context, cursor, false);
        this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.movie_row, parent, false);
    }

//    @Override
//    public View getView(int position, View cachedView, ViewGroup parent)
//    {
//        return cachedView;
//    }

//    @Override
//    public void bindView(View view, Context context, Cursor cursor) {
//        Movie movie = MovieModel.getSingleton().getMovieById(
//                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.MOVIE_ID))
//        );
//        TextView titleView = (TextView) view.findViewById(R.id.title);
//        TextView yearView = (TextView) view.findViewById(R.id.year);
//        TextView plotView = (TextView) view.findViewById(R.id.plot);
//        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
//        TextView numPartyView = (TextView) view.findViewById(R.id.num_of_invitees);
//        TextView ratingBar = (TextView) view.findViewById(R.id.rating_bar);
//
//        // Fill in the Views with content
//        titleView.setText(movie.getTitle());
//        yearView.setText(movie.getYear());
//        plotView.setText(movie.getShortPlot());
//        imageView.setImageBitmap(movie.getPoster());
//
//        StringBuilder sbRating = new StringBuilder();
//        sbRating.append(context.getString(R.string.main_rating));
//        sbRating.append(MAIN_ROW_SPACE);
//        sbRating.append(movie.getRating());
//        sbRating.append(MAIN_ROW_RATING_OUT_OF_FIVE);
//        ratingBar.setText(sbRating.toString());
//
//        StringBuilder sbParties = new StringBuilder();
//        sbParties.append(context.getString(R.string.main_num_parties));
//        sbParties.append(MAIN_ROW_SPACE);
//        sbParties.append(PartyModel.getSingleton().getPartiesByMovieId(movie.getId()).size());
//        numPartyView.setText(sbParties.toString());
//    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView titleView = (TextView) view.findViewById(R.id.title);
        TextView yearView = (TextView) view.findViewById(R.id.year);
        TextView plotView = (TextView) view.findViewById(R.id.plot);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        TextView numPartyView = (TextView) view.findViewById(R.id.num_of_invitees);
        TextView ratingBar = (TextView) view.findViewById(R.id.rating_bar);

        // Fill in the Views with content
        titleView.setText(
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.MOVIE_TITLE)));
        yearView.setText(
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.MOVIE_YEAR)));
        plotView.setText(
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.MOVIE_SHORT_PLOT)));
        String moviePoster =
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.MOVIE_IMAGE_BITMAP));
        if(moviePoster != null) {
            imageView.setImageBitmap(MovieModel.decodeBase64(moviePoster));
        }

        StringBuilder sbRating = new StringBuilder();
        sbRating.append(context.getString(R.string.main_rating));
        sbRating.append(MAIN_ROW_SPACE);
        sbRating.append(
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.MOVIE_RATING))
        );
        sbRating.append(MAIN_ROW_RATING_OUT_OF_FIVE);
        ratingBar.setText(sbRating.toString());

        StringBuilder sbParties = new StringBuilder();
        sbParties.append(context.getString(R.string.main_num_parties));
        sbParties.append(MAIN_ROW_SPACE);
        sbParties.append(
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.MOVIE_ID))
        );
        numPartyView.setText(sbParties.toString());
    }
}
