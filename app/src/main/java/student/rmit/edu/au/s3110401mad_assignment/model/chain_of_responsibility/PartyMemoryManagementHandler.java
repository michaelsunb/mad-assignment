package student.rmit.edu.au.s3110401mad_assignment.model.chain_of_responsibility;

import android.content.Context;

import java.util.List;

import student.rmit.edu.au.s3110401mad_assignment.model.Movie;
import student.rmit.edu.au.s3110401mad_assignment.model.Party;
import student.rmit.edu.au.s3110401mad_assignment.model.PartyModel;

/**
 * Created by Michaelsun Baluyos on 27/09/2015.
 *
 *
 */
public abstract class PartyMemoryManagementHandler {
    private PartyMemoryManagementHandler next = null;
    protected static List<Party> filteredParty = null;
    protected PartyModel partyModel;
    protected Context context;

    public PartyMemoryManagementHandler(Context context, PartyModel partyModel) {
        this.context = context;
        this.partyModel = partyModel;
    }

    public void setNext(PartyMemoryManagementHandler handler) {
        this.next = handler;
    }

    public void handleRequest() {
        if(hasEntry()) return;
        if(next == null) return;

        next.handleRequest();
    }

    protected abstract boolean hasEntry();
    public List<Party> getFilteredParties() {
        return filteredParty;
    }
}