package student.rmit.edu.au.s3110401mad_assignment.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Michaelsun Baluyos on 29/08/2015.
 */
public class ContactsModel extends AsyncTask<Context, Void, Map<String,Contacts>> {
    public static final int NO_PARTY = -1;

    private Cursor cur;
    private ContentResolver cr;

    private static ContactsModel singleton;
    private Map<String, Contacts> contactsMap;
    private Integer partyId = NO_PARTY;

    public static ContactsModel getSingleton() {
        if(singleton == null)
            singleton = new ContactsModel();
        return singleton;
    }

    public ContactsModel() {}

    public Map<String,Contacts> getContacts() {
        return contactsMap;
    }
    public Contacts getById(String id) {
        return contactsMap.get(id);
    }

    public List<Contacts> getByPartyId(int partyId) {
        List<Contacts> contacts = new ArrayList<>();
        for(Map.Entry<String,Contacts> contactEntry : contactsMap.entrySet()) {
            Contacts contact = contactEntry.getValue();
            if(contact.getPartyId() == partyId) {
                contacts.add(contact);
            }
        }
        return contacts;
    }

    public Contacts getByName(String name) {
        for (Map.Entry<String, Contacts> entry : contactsMap.entrySet()) {
            if(entry.getValue().getName().equals(name))
                return entry.getValue();
        }
        return null;
    }

    public void setContactsMap(Map<String, Contacts> contactsMap) {
        this.contactsMap = contactsMap;
    }

    public void setContactsToParty(List<String> contactIds, Integer partyId) {
        for(String contactId : contactIds) {
            for(Map.Entry<String,Contacts> entry : contactsMap.entrySet()) {
                Contacts contacts = entry.getValue();
                if (contacts.getId().equals(contactId)) {
                    ((ContactsStruct)contacts).setPartyId(partyId);
                    contactsMap.put(contacts.getId(),contacts);
                }
            }
        }
    }

    public void addContact(Context context, Integer partyId) {
        this.partyId = partyId;
        this.execute(context);
    }

    public void addContact(Contacts contact) {
        this.contactsMap.put(contact.getId(),contact);
    }

    @Override
    protected Map<String,Contacts> doInBackground(Context... params) {
        cr = params[0].getContentResolver();
        cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
                null, null, null);
        contactsMap = new HashMap<>();
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {

                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String phone = queryPhoneNumber(id);
                String email = queryEmail(id);

                Contacts contactStruct = new ContactsStruct(
                        id,
                        partyId,
                        cur.getString(cur
                                .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),
                        phone,
                        email);
                contactsMap.put(cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID)),
                        contactStruct);
            }
        }
        return contactsMap;
    }

    private String queryPhoneNumber(String id) {
        String phone = null;
        if (Integer
                .parseInt(cur.getString(cur
                        .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
            Cursor pCur = cr.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                    new String[]{id},
                    null);
            // Just get the first in the list
            pCur.moveToNext();
            phone = pCur
                    .getString(pCur
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            pCur.close();
        }
        return phone;
    }

    private String queryEmail(String id) {
        Cursor pCur = cr.query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                new String[]{id},
                null);
        // Just get the first in the list
        if(pCur.getCount() == 0) return "";

        pCur.moveToNext();
        String email = pCur
                .getString(pCur
                        .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
        pCur.close();
        return email;
    }
}
