package student.rmit.edu.au.s3110401mad_assignment.model.async_task;

import android.content.Context;
import android.os.AsyncTask;

import student.rmit.edu.au.s3110401mad_assignment.model.db.PartyDatabaseManager;

/**
 * Created by Michaelsun Baluyos on 5/10/2015.
 *
 *
 */
public class PartyDeleteDBTask extends AsyncTask<Void, Void, Void> {

    private int partyId;
    private Context context;

    public PartyDeleteDBTask(Context context, int partyId){
        this.context = context;
        this.partyId = partyId;
    }

    @Override
    protected Void doInBackground(Void... params) {
        PartyDatabaseManager partyDbm = new PartyDatabaseManager(context);
        partyDbm.open();
        partyDbm.deleteParty(partyId);
        partyDbm.close();
        return null;
    }
}