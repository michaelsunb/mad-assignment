package student.rmit.edu.au.s3110401mad_assignment.model;

import android.content.Context;

/**
 * Created by Michaelsun Baluyos on 29/08/2015.
 *
 *
 */
public class ContactsStruct implements Contacts {
    private String id;
    private String name;
    private String phone;
    private String email;

    public ContactsStruct(String id, String name, String phone, String email) {
        this.id = id;
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

}
