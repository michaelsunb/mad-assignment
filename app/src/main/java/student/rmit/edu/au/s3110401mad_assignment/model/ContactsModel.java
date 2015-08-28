package student.rmit.edu.au.s3110401mad_assignment.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
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

    @Override
    protected Map<String,Contacts> doInBackground(Context... params) {
        cr = params[0].getContentResolver();
        cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
                null, null, null);
        Map<String,Contacts> contacts = new HashMap<>();
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
                contacts.put(cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID)),
                        contactStruct);
            }
        }
        return contacts;
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
