package student.rmit.edu.au.s3110401mad_assignment.model;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Michaelsun Baluyos on 24/08/2015.
 */
public class PartyStruct implements Party {
    public static final int LONGITUDE_LATITUDE = 2;
    public static final int LONGITUDE = 0;
    public static final int LATITUDE = 1;
    private int id;
    private List<String> idDB;
    private Calendar date;
    private String venue;
    private double[] location = new double[LONGITUDE_LATITUDE];
    private List<String> inviteeIDs;

    public PartyStruct(int id, List<String> idDB, Calendar date, String venue, double[] location, List<String> inviteeIDs) {
        this.id = id;
        this.idDB = idDB;
        this.date = date;
        this.venue = venue;
        this.location = location;
        this.inviteeIDs = inviteeIDs;
    }

    public PartyStruct(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public List<String> getIdDB() {
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

//    @Override
//    public void setLocation(double longitude, double latitude) {
//        this.location[LONGITUDE] = longitude;
//        this.location[LATITUDE] = latitude;
//    }

    @Override
    public List<String> getInviteeIDs() {
        return inviteeIDs;
    }
}
