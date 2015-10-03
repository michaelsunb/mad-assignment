package student.rmit.edu.au.s3110401mad_assignment.model;

import android.graphics.Bitmap;

/**
 * Created by Michaelsun Baluyos on 25/08/2015.
 */
public interface Movie {
    String getId();

    String getTitle();

    String getYear();

    String getShortPlot();

    String getFullPlot();

    Bitmap getPoster();

    int getRating();

    void setRating(int rating);
}
