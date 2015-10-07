package student.rmit.edu.au.s3110401mad_assignment.model.async_task;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import student.rmit.edu.au.s3110401mad_assignment.model.db.PartyDatabaseManager;
import student.rmit.edu.au.s3110401mad_assignment.model.Contacts;
import student.rmit.edu.au.s3110401mad_assignment.model.ContactsModel;
import student.rmit.edu.au.s3110401mad_assignment.model.PartyInviteeModel;

/**
 * Created by Michaelsun Baluyos on 5/10/2015.
 *
 *
 */
public class PartyInviteeQueryDBTask extends AsyncTask<Void,Void,List<Contacts>> {

    private Context context;
    private int partyId;

    public PartyInviteeQueryDBTask(Context context, int partyId){
        this.context = context;
        this.partyId = partyId;
    }

    @Override
    protected List<Contacts> doInBackground(Void... params) {
        PartyInviteeModel inviteeModel = PartyInviteeModel.getSingleton();
        if(inviteeModel.getLink().size() > 0)
            return inviteeModel.getByPartyId(partyId);

        PartyDatabaseManager partyDbm = new PartyDatabaseManager(context);
        partyDbm.open();
        ContactsModel contactsModel = ContactsModel.getSingleton();
        if(contactsModel.getAllContacts().size() == 0)
            contactsModel.setFromContactsDb(context);
        partyDbm.getContactPartyLink();
        partyDbm.close();

        return inviteeModel.getByPartyId(partyId);
    }
}
