package student.rmit.edu.au.s3110401mad_assignment.model;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

/**
 * Created by Michaelsun Baluyos on 24/08/2015.
 *
 *
 */
public class MovieStruct implements Movie {
    private String id;
    private String title;
    private String year;
    private String shortPlot;
    private String fullPlot;
    private Bitmap imageResource;

    private int rating;

    // Expect to be parsed properly from controller
    public MovieStruct(String imdbID, String title, String year, String shortPlot, String fullPlot,
                       Bitmap imageResource, int rating) {
        this.id = imdbID;
        this.title = title;
        this.year = year;
        this.shortPlot = shortPlot;
        this.fullPlot = fullPlot;
        this.imageResource = imageResource;
        this.rating = rating;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getYear() {
        return year;
    }

    @Override
    public String getShortPlot() {
        return shortPlot;
    }

    @Override
    public String getFullPlot() {
        return fullPlot;
    }

    @Override
    public Bitmap getPoster() {
        return imageResource;
    }

    @Override
    public int getRating() {
        return rating;
    }

    @Override
    public void setRating(int rating) {
        this.rating = rating;
    }

    // TODO: Possibly delete
    public void setYear(String year) {
        this.year = year;
    }

    public void setShortPlot(String shortPlot) {
        this.shortPlot = shortPlot;
    }

    public void setFullPlot(String fullPlot) {
        this.fullPlot = fullPlot;
    }

    public void setPoster(Bitmap poster) {
        this.imageResource = imageResource;
    }
}
