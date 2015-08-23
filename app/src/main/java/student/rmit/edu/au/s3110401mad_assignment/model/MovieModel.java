package student.rmit.edu.au.s3110401mad_assignment.model;

import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Michaelsun Baluyos on 24/08/2015.
 */
public class MovieModel {
    private int id;

    private Date dateTime;
    private String location;

    private String title;
    private int year;

    private String shortDesc;
    private String longDesc;

    private ImageView poster;

    public List<MovieModel> viewMovies() {
        return new ArrayList<MovieModel>();
    }
    public void editMovies(int idIndex) {

    }
}
