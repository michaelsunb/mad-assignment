package student.rmit.edu.au.s3110401mad_assignment.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Michaelsun Baluyos on 27/08/2015.
 */
public class PartyModel {
    private static PartyModel singleton;

    public static PartyModel getSingleton() {
        if(singleton == null)
            singleton = new PartyModel();
        return singleton;
    }

    private Map<String,Party> partyMap;

    private PartyModel() {
        this.partyMap = new HashMap<String, Party>();
    }

    public void addParty(Party party) {
        partyMap.put(party.getId(), party);
    }
    public Party getPartyById(String imdbId) {
        return partyMap.get(imdbId);
    }
    public List<Party> getAllParties() {
        return new ArrayList<Party>(partyMap.values());
    }
}