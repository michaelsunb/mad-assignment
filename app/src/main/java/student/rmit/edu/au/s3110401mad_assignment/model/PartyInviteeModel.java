package student.rmit.edu.au.s3110401mad_assignment.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Michaelsun Baluyos on 25/08/2015.
 *
 *
 */
public class PartyInviteeModel {
    public static final int NO_PARTY = -1;
    public static final int NO_LINK_INDEX = -1;
    private static PartyInviteeModel singleton;

    public static PartyInviteeModel getSingleton() {
        if(singleton == null)
            singleton = new PartyInviteeModel();
        return singleton;
    }

    private List<Map<String,Integer>> inviteeMap;

    private PartyInviteeModel() {
        inviteeMap = new ArrayList<>();
    }
    public void addLink(String contactId,int partyId) {
        Map<String,Integer> map = new HashMap<>();
        map.put(contactId, partyId);

        int index = searchLinkIndex(contactId,partyId);
        if(index != NO_LINK_INDEX) {
            inviteeMap.set(index, map);
        } else {
            inviteeMap.add(map);
        }
    }
    public int searchLinkIndex(String contactId,int partyId) {
        for (int i = 0; i < inviteeMap.size() ; i++) {
            Map<String, Integer> map = inviteeMap.get(i);
            if (map.containsKey(contactId) && map.containsValue(partyId)) {
                return i;
            }
        }
        return NO_LINK_INDEX;
    }

    public void setContactsToParty(List<String> contactIds, int partyId) {
        List<Contacts> tempContacts =
                new ArrayList<>(ContactsModel.getSingleton().getAllContacts().values());

        for(Contacts contact : tempContacts) {
            if (contactIds.contains(contact.getId())) {
                addLink(contact.getId(), partyId);
            } else {
                removeLink(contact.getId(), partyId);
            }
        }
    }

    public void removeLink(String contactId, int partyId) {
        for (int i = 0; i < inviteeMap.size() ; i++) {
            Map<String, Integer> map = inviteeMap.get(i);
            if (map.containsKey(contactId) && map.containsValue(partyId)) {
                inviteeMap.remove(i);
            }
        }
    }

    public List<Map<String, Integer>> getLink() {
        return inviteeMap;
    }

    public List<Contacts> getByPartyId(int partyId) {
        List<Contacts> contacts = new ArrayList<>();
        List<Contacts> tempContacts =
                new ArrayList<>(ContactsModel.getSingleton().getAllContacts().values());

        if(tempContacts.size() == 0) return contacts;

        for (Map<String,Integer> map : inviteeMap) {
            for (Map.Entry<String,Integer> entry : map.entrySet()) {
                if(entry.getValue() == partyId) {
                    for(Contacts contact : tempContacts) {
                        if (contact.getId().equals(entry.getKey())) {
                            contacts.add(contact);
                        }
                    }
                }
            }
        }
        return contacts;
    }
}
