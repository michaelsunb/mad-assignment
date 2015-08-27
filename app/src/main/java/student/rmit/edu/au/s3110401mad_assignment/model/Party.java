package student.rmit.edu.au.s3110401mad_assignment.model;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Michaelsun Baluyos on 27/08/2015.
 */
public interface Party {
    String getId();

    Calendar getDate();

    void setDate(Calendar date);

    String getVenue();

    void setVenue(String venue);

    String getLocation();

    void setLocation(String location);

    ArrayList<String> getEmailInvites();

    void setEmailInvites(ArrayList<String> emailInvites);
}
