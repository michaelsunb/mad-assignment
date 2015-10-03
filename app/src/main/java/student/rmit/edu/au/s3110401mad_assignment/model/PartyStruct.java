package student.rmit.edu.au.s3110401mad_assignment.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Michaelsun Baluyos on 24/08/2015.
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
    private boolean myParty;

    public PartyStruct(int id, String idDB, String date, String venue, String location) {
        this.id = id;
        this.idDB = idDB;
        this.venue = venue;

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

    public PartyStruct(int id, String idDB, Calendar date, String venue, double[] location) {
        this.id = id;
        this.idDB = idDB;
        this.date = date;
        this.venue = venue;
        this.location = location;
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
}
