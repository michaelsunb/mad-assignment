package student.rmit.edu.au.s3110401mad_assignment.model.async_task;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import student.rmit.edu.au.s3110401mad_assignment.db.PartyDatabaseManager;
import student.rmit.edu.au.s3110401mad_assignment.model.Contacts;
import student.rmit.edu.au.s3110401mad_assignment.model.ContactsModel;
import student.rmit.edu.au.s3110401mad_assignment.model.PartyInviteeModel;

/**
 * Created by Michaelsun Baluyos on 5/10/2015.
 *
 *
 */
public class PartyInviteeSaveDBTask extends AsyncTask<Void,Void,Void> {

    private Context context;
    private List<String> contactIds;
    private int partyId;

    public PartyInviteeSaveDBTask(Context context, List<String> contactIds, Integer partyId){
        this.context = context;
        this.contactIds = contactIds;
        this.partyId = partyId;
    }

    @Override
    protected Void doInBackground(Void... params) {
        PartyInviteeModel inviteeModel = PartyInviteeModel.getSingleton();
        PartyDatabaseManager partyDbm = new PartyDatabaseManager(context);
        partyDbm.open();

        List<Contacts> tempContacts =
                new ArrayList<>(ContactsModel.getSingleton().getAllContacts().values());

        inviteeModel.setContactsToParty(contactIds, partyId);

        for(Contacts contact : tempContacts) {
            if (contactIds.contains(contact.getId())) {
                partyDbm.addEditInvitees(contact.getId(), partyId);
            } else {
                partyDbm.deleteInvite(contact.getId(), partyId);
            }
        }

        partyDbm.close();

        return null;
    }
}
