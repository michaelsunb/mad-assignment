package student.rmit.edu.au.s3110401mad_assignment.model.chain_of_responsibility;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import student.rmit.edu.au.s3110401mad_assignment.model.Party;
import student.rmit.edu.au.s3110401mad_assignment.model.PartyModel;

/**
 * Created by Michaelsun Baluyos on 25/08/2015.
 *
 *
 */
public class PartyMemoryManagementClient extends AsyncTask<Void,Void,List<Party>> {
    private Context context;
    private static PartyMemoryManagementHandler partyMemory;

    public PartyMemoryManagementClient(Context context) {
        this.context = context;
    }

    private PartyMemoryManagementHandler getMemoryHandler() {
        if(partyMemory != null) return partyMemory;

        partyMemory = new PartyMemory(context, PartyModel.getSingleton());
        partyMemory.setNext(new PartyRetrieveDB(context, PartyModel.getSingleton()));

        return partyMemory;
    }

    @Override
    protected List<Party> doInBackground(Void... params) {
        getMemoryHandler().handleRequest();
        return getMemoryHandler().getFilteredParties();
    }
}
