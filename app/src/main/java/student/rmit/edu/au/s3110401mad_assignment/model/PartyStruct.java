package student.rmit.edu.au.s3110401mad_assignment.model;

import java.util.Calendar;

/**
 * Created by Michaelsun Baluyos on 24/08/2015.
 *
 *
 */
public class PartyStruct implements Party {
    public static final int LONGITUDE_LATITUDE = 2;
    public static final int LONGITUDE = 0;
    public static final int LATITUDE = 1;
    private int id;
    private String idDB;
    private Calendar date;
    private String venue;
    private double[] location = new double[LONGITUDE_LATITUDE];
    private String movieTitle;

    public PartyStruct(int id, String idDB, String movieTitle, String date, String venue, String location) {
        this.id = id;
        this.idDB = idDB;
        this.venue = venue;
        this.movieTitle = movieTitle;

        try {
            this.date = PartyModel.stringToCalendar(date);

            String[] separated = location.split(",");
            for(int i=0 ; i < LONGITUDE_LATITUDE ; i++) {
                this.location[i] = Double.parseDouble(separated[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PartyStruct(int id, String idDB, String movieTitle, Calendar date, String venue, double[] location) {
        this.id = id;
        this.idDB = idDB;
        this.date = date;
        this.venue = venue;
        this.location = location;
        this.movieTitle = movieTitle;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getImDB() {
        return idDB;
    }

    @Override
    public Calendar getDate() {
        return date;
    }

    @Override
    public String getVenue() {
        return venue;
    }

    @Override
    public double[] getLocation() {
        return location;
    }

    @Override
    public String getMovieTitle() {
        return movieTitle;
    }
}
