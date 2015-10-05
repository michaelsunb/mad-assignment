package student.rmit.edu.au.s3110401mad_assignment.model.async_task;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;
import java.util.Map;

import student.rmit.edu.au.s3110401mad_assignment.db.PartyDatabaseManager;
import student.rmit.edu.au.s3110401mad_assignment.model.Contacts;
import student.rmit.edu.au.s3110401mad_assignment.model.ContactsModel;
import student.rmit.edu.au.s3110401mad_assignment.model.Party;

/**
 * Created by Michaelsun Baluyos on 5/10/2015.
 *
 *
 */
public class ContactQueryDBTask extends AsyncTask<Context, Void, Map<String, Contacts>> {

    @Override
    protected Map<String, Contacts> doInBackground(Context... params) {
        PartyDatabaseManager partyDbm = new PartyDatabaseManager(params[0]);
        partyDbm.open();
        for (Contacts contact : partyDbm.getAllContact()) {
            ContactsModel.getSingleton().addContact(contact);
        }
        partyDbm.close();
        return ContactsModel.getSingleton().getAllContacts();
    }
}
