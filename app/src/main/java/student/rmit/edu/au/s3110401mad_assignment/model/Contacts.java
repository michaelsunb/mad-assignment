package student.rmit.edu.au.s3110401mad_assignment.model;

/**
 * Created by Michaelsun Baluyos on 29/08/2015.
 *
 *
 */
public interface Contacts {
    String getId();

    int getPartyId();

    void setPartyId(int partyId);

    String getName();

    String getPhone();

    String getEmail();
}
