package student.rmit.edu.au.s3110401mad_assignment.model.async_task;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import student.rmit.edu.au.s3110401mad_assignment.db.PartyDatabaseManager;
import student.rmit.edu.au.s3110401mad_assignment.model.Party;

/**
 * Created by Michaelsun Baluyos on 5/10/2015.
 *
 *
 */
public class PartyEditDBTask extends AsyncTask<Void, Void, Void> {

    private Party party;
    private Context context;
    private List<String> contactIds;

    public PartyEditDBTask(Context context, Party party, List<String> contactIds){
        this.context = context;
        this.party = party;
        this.contactIds = contactIds;
    }

    @Override
    protected Void doInBackground(Void... params) {
        PartyDatabaseManager partyDbm = new PartyDatabaseManager(context);
        partyDbm.open();
        partyDbm.editParty(party, contactIds);
        partyDbm.close();
        return null;
    }
}
