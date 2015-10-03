/* *
 * COSC2347 Mobile Application Development
 * Assignment 2
 *
 * */
package student.rmit.edu.au.s3110401mad_assignment.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import student.rmit.edu.au.s3110401mad_assignment.model.Contacts;
import student.rmit.edu.au.s3110401mad_assignment.model.ContactsModel;
import student.rmit.edu.au.s3110401mad_assignment.model.ContactsStruct;
import student.rmit.edu.au.s3110401mad_assignment.model.Party;
import student.rmit.edu.au.s3110401mad_assignment.model.PartyModel;
import student.rmit.edu.au.s3110401mad_assignment.model.PartyStruct;

public class PartyDatabaseManager {
    private static SQLiteDatabase database;
    private static DatabaseHelper edbHelper;

    public PartyDatabaseManager(Context context){
        edbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException{
        database = edbHelper.getWritableDatabase();
    }

    public void close(){
        database.close();
    }

    public void addParty(Party newParty){
        PartyModel.getSingleton().addParty(newParty);

        Log.i("Ayy lmao", "I am in PartyDatabaseManager");
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.PARTY_ID, newParty.getId());
        values.put(DatabaseHelper.PARTY_MOVIE_ID, newParty.getImDB());
        values.put(DatabaseHelper.PARTY_DATETIME,
                PartyModel.calendarToString(newParty.getDate()));
        values.put(DatabaseHelper.PARTY_VENUE, newParty.getVenue());

        double[] partyLocation = newParty.getLocation();
        values.put(DatabaseHelper.PARTY_LOCATION,
                partyLocation[PartyStruct.LONGITUDE] + "," + partyLocation[PartyStruct.LATITUDE]);

        database.insert(DatabaseHelper.PARTY_TABLE_NAME, null, values);
    }

    public List<Party> getAllParties(){
        PartyModel partyModel = PartyModel.getSingleton();

        Cursor partyCursor = database.query(DatabaseHelper.PARTY_TABLE_NAME,
                null, null, null, null, null, null);

        Cursor inviteeCursor;

        partyCursor.moveToFirst();
        while(!partyCursor.isAfterLast()){
            String partyId = partyCursor.getString(partyCursor.getColumnIndex(DatabaseHelper.PARTY_ID));
            inviteeCursor = database.query(
                    DatabaseHelper.PARTY_INVITEE_TABLE_NAME, null,
                    DatabaseHelper.PARTY_INVITEE_ID + " = " + partyId,
                    null, null, null, null);
            partyModel.addParty(cursorToParty(partyCursor, inviteeCursor));

            partyCursor.moveToNext();
        }

        return partyModel.getAllParties();
    }

    private Party cursorToParty(Cursor partyCursor, Cursor inviteeCursor){
        inviteeCursor.moveToFirst();
        while(!inviteeCursor.isAfterLast()){
            String partyInviteeId =
                    inviteeCursor.getString(
                            inviteeCursor.getColumnIndex(DatabaseHelper.PARTY_INVITEE_ID));
            Integer partyInviteePartyId =
                    inviteeCursor.getInt(
                            inviteeCursor.getColumnIndex(DatabaseHelper.PARTY_INVITEE_PARTY_ID));
            String partyInviteeName =
                    inviteeCursor.getString(
                            inviteeCursor.getColumnIndex(DatabaseHelper.PARTY_INVITEE_NAME));
            String partyInviteePhone =
                    inviteeCursor.getString(
                            inviteeCursor.getColumnIndex(DatabaseHelper.PARTY_INVITEE_PHONE));
            String partyInviteeEmail =
                    inviteeCursor.getString(
                            inviteeCursor.getColumnIndex(DatabaseHelper.PARTY_INVITEE_EMAIL));

            ContactsModel.getSingleton().addContact(new ContactsStruct(
                    partyInviteeId,
                    partyInviteePartyId,
                    partyInviteeName,
                    partyInviteePhone,
                    partyInviteeEmail
            ));

            inviteeCursor.moveToNext();
        }

        Integer partyId =
                partyCursor.getInt(partyCursor.getColumnIndex(DatabaseHelper.PARTY_ID));
        String partyMovieId =
                partyCursor.getString(partyCursor.getColumnIndex(DatabaseHelper.PARTY_MOVIE_ID));
        String partyDatetime =
                partyCursor.getString(partyCursor.getColumnIndex(DatabaseHelper.PARTY_DATETIME));
        String partyVenue =
                partyCursor.getString(partyCursor.getColumnIndex(DatabaseHelper.PARTY_VENUE));
        String partyLocation =
                partyCursor.getString(partyCursor.getColumnIndex(DatabaseHelper.PARTY_LOCATION));

        return new PartyStruct(partyId, partyMovieId, partyDatetime, partyVenue, partyLocation);
    }

    public void deleteParty(String id){
        String selectionToBeDeleted = DatabaseHelper.PARTY_ID + " = \"" + id + "\"";
        database.delete(DatabaseHelper.PARTY_TABLE_NAME, selectionToBeDeleted, null);
        PartyModel.getSingleton().deleteParty(id);
    }

    public void editParty(Party party, List<Contacts> contacts ){
        for(Contacts contact : contacts) {
            editInvitees(contact);
        }
        editParty(party);
    }

    public void editParty(Party party) {
        String whereClause = DatabaseHelper.PARTY_ID + " = \"" + party.getId() + "\"" ;

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.PARTY_ID, party.getId());
        contentValues.put(DatabaseHelper.PARTY_MOVIE_ID, party.getImDB());
        contentValues.put(DatabaseHelper.PARTY_DATETIME,
                PartyModel.calendarToString(party.getDate()));
        contentValues.put(DatabaseHelper.PARTY_VENUE, party.getVenue());

        double[] partyLocation = party.getLocation();
        contentValues.put(DatabaseHelper.PARTY_LOCATION,
                partyLocation[PartyStruct.LONGITUDE] + "," + partyLocation[PartyStruct.LATITUDE]);

        database.update(DatabaseHelper.PARTY_TABLE_NAME, contentValues, whereClause, null);
    }

    public void editInvitees(Contacts contact ){
        String whereClause = DatabaseHelper.PARTY_INVITEE_ID + " = \"" + contact.getId() + "\"" ;

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.PARTY_INVITEE_ID, contact.getId());
        contentValues.put(DatabaseHelper.PARTY_INVITEE_PARTY_ID, contact.getPartyId());
        contentValues.put(DatabaseHelper.PARTY_INVITEE_NAME, contact.getName());
        contentValues.put(DatabaseHelper.PARTY_INVITEE_PHONE, contact.getPhone());
        contentValues.put(DatabaseHelper.PARTY_INVITEE_EMAIL, contact.getEmail());

        database.update(DatabaseHelper.PARTY_INVITEE_TABLE_NAME, contentValues, whereClause, null);
        ContactsModel.getSingleton().addContact(contact);
    }
}
