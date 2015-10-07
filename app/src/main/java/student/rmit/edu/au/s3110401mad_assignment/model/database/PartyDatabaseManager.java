package student.rmit.edu.au.s3110401mad_assignment.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import student.rmit.edu.au.s3110401mad_assignment.model.PartyInviteeModel;
import student.rmit.edu.au.s3110401mad_assignment.model.Contacts;
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

    public void addParty(Party newParty, List<String> contactIds){

        if(database.rawQuery("SELECT * FROM " + DatabaseHelper.PARTY_TABLE_NAME
                + " WHERE " + DatabaseHelper.PARTY_ID + " = \""
                + newParty.getId() + "\"",null).getCount() != 0) return;

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.PARTY_ID, newParty.getId());
        values.put(DatabaseHelper.PARTY_MOVIE_ID, newParty.getImDB());
        values.put(DatabaseHelper.PARTY_MOVIE_TITLE, newParty.getMovieTitle());
        values.put(DatabaseHelper.PARTY_DATETIME,
                PartyModel.calendarToString(newParty.getDate())
        );
        values.put(DatabaseHelper.PARTY_VENUE, newParty.getVenue());

        double[] partyLocation = newParty.getLocation();
        values.put(DatabaseHelper.PARTY_LOCATION,
                partyLocation[PartyStruct.LONGITUDE] + "," + partyLocation[PartyStruct.LATITUDE]
        );

        database.insert(DatabaseHelper.PARTY_TABLE_NAME, null, values);
    }

    public List<Party> getAllParties(){
        List<Party> parties = new ArrayList<>();

        Cursor partyCursor = database.query(DatabaseHelper.PARTY_TABLE_NAME,
                null, null, null, null, null, null);

        partyCursor.moveToFirst();
        while(!partyCursor.isAfterLast()){
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
            String movieTitle =
                    partyCursor.getString(partyCursor.getColumnIndex(DatabaseHelper.PARTY_MOVIE_TITLE));

            parties.add(new PartyStruct(partyId, partyMovieId, movieTitle, partyDatetime, partyVenue,
                    partyLocation));

            partyCursor.moveToNext();
        }

        return parties;
    }

    public void deleteParty(int id){
        String selectionToBeDeleted = DatabaseHelper.PARTY_ID + " = \"" + id + "\"";
        database.delete(DatabaseHelper.PARTY_TABLE_NAME, selectionToBeDeleted, null);

        for(Contacts contact : PartyInviteeModel.getSingleton().getByPartyId(id)) {
            addEditInvitees(contact.getId(),PartyInviteeModel.NO_PARTY);
        }
    }

    public void editParty(Party party, List<String> contacts ){
        for(String contactId : contacts) {
            addEditInvitees(contactId, party.getId());
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


    /** Invitee stuff **/
    public void editInvitees(String contactId,int partyId){
        String whereClause =
                DatabaseHelper.PARTY_INVITEE_CONTACTS_ID + " = \"" + contactId + "\" AND " +
                DatabaseHelper.PARTY_INVITEE_PARTY_ID + " = \"" + partyId + "\"";

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.PARTY_INVITEE_CONTACTS_ID, contactId);
        contentValues.put(DatabaseHelper.PARTY_INVITEE_PARTY_ID, partyId);

        database.update(DatabaseHelper.PARTY_INVITEE_TABLE_NAME, contentValues, whereClause, null);
        PartyInviteeModel.getSingleton().addLink(contactId, partyId);
    }

    public void addInvitees(String contactId,int partyId){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.PARTY_INVITEE_CONTACTS_ID, contactId);
        contentValues.put(DatabaseHelper.PARTY_INVITEE_PARTY_ID, partyId);

        database.insert(DatabaseHelper.PARTY_INVITEE_TABLE_NAME, null, contentValues);
        PartyInviteeModel.getSingleton().addLink(contactId, partyId);
    }

    public void addEditInvitees(String contactId, int partyId) {
        String sql = "SELECT * FROM " + DatabaseHelper.PARTY_INVITEE_TABLE_NAME
                + " WHERE " + DatabaseHelper.PARTY_INVITEE_CONTACTS_ID + " = \""
                + contactId + "\" AND " + DatabaseHelper.PARTY_INVITEE_PARTY_ID
                + " = \"" + partyId + "\"";
        addEditInvitees(contactId, partyId,
                (database.rawQuery(sql, null).getCount() != 0));
    }

    public void addEditInvitees(String contactId, int partyId, boolean edit) {
        if(partyId == PartyInviteeModel.NO_PARTY && edit) {
            deleteInvite(contactId,partyId);
            return;
        }

        if(edit)
            editInvitees(contactId,partyId);
        else
            addInvitees(contactId, partyId);
    }

    public void deleteInvite(String contactId, int partyId){
        String selectionToBeDeleted =
                DatabaseHelper.PARTY_INVITEE_CONTACTS_ID + " = \"" + contactId + "\" AND " +
                DatabaseHelper.PARTY_INVITEE_PARTY_ID + " = \"" + partyId + "\"";
        database.delete(DatabaseHelper.PARTY_INVITEE_TABLE_NAME, selectionToBeDeleted, null);
    }

    public List<Map<String,Integer>> getContactPartyLink(){
        PartyInviteeModel partyInviteeModel = PartyInviteeModel.getSingleton();

        Cursor partyCursor = database.query(DatabaseHelper.PARTY_INVITEE_TABLE_NAME,
                null, null, null, null, null, null);

        partyCursor.moveToFirst();
        while(!partyCursor.isAfterLast()){
            String contactId =
                    partyCursor.getString(
                            partyCursor.getColumnIndex(DatabaseHelper.PARTY_INVITEE_CONTACTS_ID));
            Integer partyId =
                    partyCursor.getInt(
                            partyCursor.getColumnIndex(DatabaseHelper.PARTY_INVITEE_PARTY_ID));

            partyInviteeModel.addLink(contactId, partyId);
            partyCursor.moveToNext();
        }

        return partyInviteeModel.getLink();
    }
}
