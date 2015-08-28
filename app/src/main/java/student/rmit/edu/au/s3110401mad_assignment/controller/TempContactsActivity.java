package student.rmit.edu.au.s3110401mad_assignment.controller;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import student.rmit.edu.au.s3110401mad_assignment.R;
// TODO
public class TempContactsActivity extends AppCompatActivity {
    TextView textDetail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_contacts);
        textDetail = (TextView) findViewById(R.id.textView1);
        readContacts();

        if(getSupportActionBar() == null)
            return;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }

    public void readContacts() {
        StringBuffer sb = new StringBuffer();
        sb.append("......Contact Details.....");
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
                null, null, null);
        String phone = null;
        String emailContact = null;
        String emailType = null;
        String image_uri = "";
        Bitmap bitmap = null;
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur
                        .getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur
                        .getString(cur
                                .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                image_uri = cur
                        .getString(cur
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                if (Integer
                        .parseInt(cur.getString(cur
                                .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    System.out.println("name : " + name + ", ID : " + id);
                    sb.append("\n Contact Name:" + name);
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                    + " = ?", new String[] { id }, null);
                    while (pCur.moveToNext()) {
                        phone = pCur
                                .getString(pCur
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        sb.append("\n Phone number:" + phone);
                        System.out.println("phone" + phone);
                    }
                    pCur.close();

                    Cursor emailCur = cr.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID
                                    + " = ?", new String[] { id }, null);
                    while (emailCur.moveToNext()) {
                        emailContact = emailCur
                                .getString(emailCur
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        emailType = emailCur
                                .getString(emailCur
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
                        sb.append("\nEmail:" + emailContact + "Email type:" + emailType);
                        System.out.println("Email " + emailContact
                                + " Email Type : " + emailType);

                    }

                    emailCur.close();
                }

                if (image_uri != null) {
                    System.out.println(Uri.parse(image_uri));
                    try {
                        bitmap = MediaStore.Images.Media
                                .getBitmap(this.getContentResolver(),
                                        Uri.parse(image_uri));
                        sb.append("\n Image in Bitmap:" + bitmap);
                        System.out.println(bitmap);

                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }


                sb.append("\n........................................");
            }

            textDetail.setText(sb);
        }
    }

}