package student.rmit.edu.au.s3110401mad_assignment.model;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Michaelsun Baluyos on 24/08/2015.
 */
public class PartyStruct implements Party {
    private int id;
    private String idDB;
    private Calendar date;
    private String venue;
    private String location;
    private ArrayList<String> emailInvites;

    public PartyStruct(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getIdDB() {
        return idDB;
    }

    @Override
    public void setIdDB(String idDB) {
        this.idDB = idDB;
    }

    @Override
    public Calendar getDate() {
        return date;
    }

    @Override
    public void setDate(Calendar date) {
        this.date = date;
    }

    @Override
    public String getVenue() {
        return venue;
    }

    @Override
    public void setVenue(String venue) {
        this.venue = venue;
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public ArrayList<String> getEmailInvites() {
        return emailInvites;
    }

    @Override
    public void setEmailInvites(ArrayList<String> emailInvites) {
        this.emailInvites = emailInvites;
    }
}
