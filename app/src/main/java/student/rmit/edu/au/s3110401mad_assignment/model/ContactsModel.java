package student.rmit.edu.au.s3110401mad_assignment.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Michaelsun Baluyos on 29/08/2015.
 *
 *
 */
public class ContactsModel extends AsyncTask<Context, Void, Map<String,Contacts>> {

    private Cursor cur;
    private ContentResolver cr;

    private static ContactsModel singleton;
    private Map<String, Contacts> contactsMap;

    public static ContactsModel getSingleton() {
        if(singleton == null)
            singleton = new ContactsModel();
        return singleton;
    }

    public ContactsModel() { this.contactsMap = new HashMap<>(); }

    public Contacts getById(String id) {
        return contactsMap.get(id);
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

    @Override
    protected Map<String,Contacts> doInBackground(Context... params) {
        Context context = params[0];
        setFromContactsDb(context);
        return contactsMap;
    }

    public void setFromContactsDb(Context context) {
        cr = context.getContentResolver();
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
                        cur.getString(cur
                                .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),
                        phone,
                        email);
                contactsMap.put(cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID)),
                        contactStruct);
            }
        }
        cur.close();
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

        if(pCur.getCount() == 0) {
            pCur.close();
            return "";
        }
        // Just get the first in the list
        pCur.moveToNext();
        String email = pCur
                .getString(pCur
                        .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
        pCur.close();
        return email;
    }

    public Map<String,Contacts> getAllContacts() {
        return contactsMap;
    }
}
