package student.rmit.edu.au.s3110401mad_assignment.model;

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
    private List<String> idDB;
    private Date date;
    private String venue;
    private double[] location = new double[LONGITUDE_LATITUDE];
    private List<String> inviteeIDs;
    private boolean myParty;

    public PartyStruct(int id, List<String> idDB, Date date, String venue, double[] location, List<String> inviteeIDs) {
        this.id = id;
        this.idDB = idDB;
        this.date = date;
        this.venue = venue;
        this.location = location;
        this.inviteeIDs = inviteeIDs;
        this.myParty = true;
    }

    public PartyStruct(int id, List<String> idDB, Date date, String venue, double[] location, List<String> inviteeIDs, boolean myParty) {
        this.id = id;
        this.idDB = idDB;
        this.date = date;
        this.venue = venue;
        this.location = location;
        this.inviteeIDs = inviteeIDs;
        this.myParty = myParty;
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
    public Date getDate() {
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
    public List<String> getInviteeIDs() {
        return inviteeIDs;
    }

    @Override
    public boolean myParty() {
        return myParty;
    }
}
