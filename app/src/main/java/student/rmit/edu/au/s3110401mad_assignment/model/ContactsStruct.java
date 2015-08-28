package student.rmit.edu.au.s3110401mad_assignment.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michaelsun Baluyos on 29/08/2015.
 */
public class ContactsStruct implements Contacts {
    private String id;
    private String name;
    private List<String> phone;;

    public ContactsStruct(String id, String name, List<String> phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
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
    public List<String> getPhone() {
        return phone;
    }
}
