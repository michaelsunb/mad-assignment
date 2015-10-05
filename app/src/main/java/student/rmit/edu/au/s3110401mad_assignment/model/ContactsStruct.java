package student.rmit.edu.au.s3110401mad_assignment.model;

/**
 * Created by Michaelsun Baluyos on 29/08/2015.
 *
 *
 */
public class ContactsStruct implements Contacts {
    private String id;
    private int partyId;
    private String name;
    private String phone;;
    private String email;

    public ContactsStruct(String id, int partyId, String name, String phone, String email) {
        this.id = id;
        this.partyId = partyId;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void setPartyId(int partyId) {
        this.partyId = partyId;
    }

    @Override
    public int getPartyId() {
        return partyId;
    }
}
