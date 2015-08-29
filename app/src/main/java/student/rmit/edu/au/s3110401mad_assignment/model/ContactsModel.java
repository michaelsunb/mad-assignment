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
    private Cursor cur;
    private ContentResolver cr;

    private static ContactsModel singleton;
    private Map<String, Contacts> contactsMap;

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
        cr = params[0].getContentResolver();
        cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
                null, null, null);
        contactsMap = new HashMap<>();
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {

                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                List<String> phone = queryPhoneNumbers(id);

                Contacts contactStruct = new ContactsStruct(
                        id,
                        cur.getString(cur
                                .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),
                        phone
                );
                contactsMap.put(cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID)),
                        contactStruct);
            }
        }
        return contactsMap;
    }

    private List<String> queryPhoneNumbers(String id) {
        List<String> phone = new ArrayList<>();
        if (Integer
                .parseInt(cur.getString(cur
                        .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
            Cursor pCur = cr.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                            + " = ?", new String[] { id }, null);
            while (pCur.moveToNext()) {
                phone.add(pCur
                        .getString(pCur
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
            }
            pCur.close();
        }
        return phone;
    }
}
