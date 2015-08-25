package student.rmit.edu.au.s3110401mad_assignment.model;

import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Michaelsun Baluyos on 24/08/2015.
 */
public class MovieStruct {
    private String title;
    private String year;
    private String shortPlot;
    private String fullPlot;
    private ImageView poster;
    private String id;
    private int rating = 0;

    // Expect to be parsed properly from sample data
    public MovieStruct(String imdbID, String title, String year, String shortPlot, String fullPlot) {
        this.id = imdbID;
        this.title = title;
    }

    // Expect to be parsed properly from controller
    public MovieStruct(String imdbID, String title, String year, String shortPlot, String fullPlot, ImageView poster) {
        this.id = imdbID;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getShortPlot() {
        return shortPlot;
    }

    public String getFullPlot() {
        return fullPlot;
    }

    public ImageView getPoster() {
        return poster;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setShortPlot(String shortPlot) {
        this.shortPlot = shortPlot;
    }

    public void setFullPlot(String fullPlot) {
        this.fullPlot = fullPlot;
    }

    public void setPoster(ImageView poster) {
        this.poster = poster;
    }
}
