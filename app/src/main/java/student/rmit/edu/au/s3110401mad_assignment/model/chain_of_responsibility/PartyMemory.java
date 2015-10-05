package student.rmit.edu.au.s3110401mad_assignment.model.chain_of_responsibility;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.regex.Pattern;

import student.rmit.edu.au.s3110401mad_assignment.model.Movie;
import student.rmit.edu.au.s3110401mad_assignment.model.MovieModel;
import student.rmit.edu.au.s3110401mad_assignment.model.Party;
import student.rmit.edu.au.s3110401mad_assignment.model.PartyModel;

/**
 * Created by Michaelsun Baluyos on 25/08/2015.
 *
 *
 */
public class PartyMemory extends PartyMemoryManagementHandler
{
    public PartyMemory(Context context, PartyModel partyModel) {
        super(context, partyModel);
    }

    @Override
    protected boolean hasEntry() {
        Log.e("Ayy lmao Class is", this.getClass().getSimpleName());

        if(partyModel.getAllParties().size()==0) return false;

        filteredParty = new ArrayList<>();
        int i = 0;
        for(Party party : partyModel.getAllParties()) {
            filteredParty.add(party);
        }

        return (filteredParty.size() > 0);
    }
}