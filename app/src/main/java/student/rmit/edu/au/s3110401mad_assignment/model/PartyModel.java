package student.rmit.edu.au.s3110401mad_assignment.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Michaelsun Baluyos on 27/08/2015.
 *
 *
 */
public class PartyModel {
    private static PartyModel singleton;

    public static PartyModel getSingleton() {
        if(singleton == null)
            singleton = new PartyModel();
        return singleton;
    }

    private Map<Integer, Party> partyMap;

    private PartyModel() {
        this.partyMap = new HashMap<>();
    }

    public void addParty(Party party) {
        partyMap.put(party.getId(), party);
    }
    public Party getPartyById(Integer partyId) {
        return partyMap.get(partyId);
    }
    public List<Party> getPartiesByMovieId(String imdbId) {
        List<Party> partiesByMovie = new ArrayList<>();
        for(Map.Entry<Integer,Party> entry : partyMap.entrySet())
            if(entry.getValue().getImDB().contains(imdbId))
                partiesByMovie.add(entry.getValue());
        return partiesByMovie;
    }

    public List<Party> getAllParties() {
        return new ArrayList<Party>(partyMap.values());
    }

    public void deleteEvent(int id) {
        partyMap.remove(id);
    }

    public static Calendar stringToCalendar(String calenar) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Timestamp.valueOf(calenar).getTime());
        return calendar;
    }

    public static String calendarToString(Calendar calendar) {
        return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(calendar.getTime());
    }

    public void deleteParty(String id) {
        // TODO: add functionality
    }
}