package student.rmit.edu.au.s3110401mad_assignment.model;

import android.location.Address;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Michaelsun Baluyos on 24/08/2015.
 */
public class PartyModel {
    private Date date;
    private Address venue;
    private LatLng location;
    private ArrayList<String> emailInvites;

    public void scheduleParty(Address venue,
                              LatLng location,
                              ArrayList<String> emailInvites) {

    }
}
