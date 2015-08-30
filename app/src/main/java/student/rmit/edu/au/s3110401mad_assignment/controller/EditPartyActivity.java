package student.rmit.edu.au.s3110401mad_assignment.controller;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

import student.rmit.edu.au.s3110401mad_assignment.R;
import student.rmit.edu.au.s3110401mad_assignment.model.ContactsModel;
import student.rmit.edu.au.s3110401mad_assignment.model.Party;
import student.rmit.edu.au.s3110401mad_assignment.model.PartyModel;
import student.rmit.edu.au.s3110401mad_assignment.model.PartyStruct;

public class EditPartyActivity extends BasePartyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_party);

        datetime = Calendar.getInstance();
        asyncContactsTask = new ContactsModel().execute(this);
        partyId = (PartyModel.getSingleton().getAllParties().size() + 1);

        TextView viewById = (TextView) findViewById(R.id.edit_party_movie_text);
        try {
            Bundle extras = getIntent().getExtras();
            partyId = extras.getInt("party_id");
            Party party =
                    PartyModel.getSingleton().getPartyById(partyId);
            movieIds = party.getIdDB().toArray(new String[party.getIdDB().size()]);

            if(party.getDate() != null)
                datetime.setTime(party.getDate());
            viewById.setText(
                    movieIds.length + " " + getText(R.string.event_movie_text)
            );

            ((EditText)findViewById(R.id.edit_party_venue_edit_text)).setText(party.getVenue());
            ((EditText)findViewById(R.id.edit_party_longitude_edit_text)).setText(
                    "" + party.getLocation()[PartyStruct.LONGITUDE]);
            ((EditText)findViewById(R.id.edit_party_latitude_edit_text)).setText(
                    "" + party.getLocation()[PartyStruct.LATITUDE]);
        } catch (Exception e) {
            System.out.println("Oh no! Something happened: " + e.getMessage());
            viewById.setText(
                    "0 " + getText(R.string.event_movie_text)
            );
        }

        ((TextView) findViewById(R.id.edit_party_invitees_text)).setText(
                whichContacts.size() + " " + getText(R.string.event_invitees_text)
        );

        findViewById(R.id.edit_party_date_picker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(R.id.edit_party_date_text);
            }
        });

        findViewById(R.id.edit_party_time_picker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(R.id.edit_party_time_text);
            }
        });

        findViewById(R.id.edit_party_movie_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMovieSelectList(R.id.edit_party_movie_text);
            }
        });

        findViewById(R.id.edit_party_invitees_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContactsSelectList(R.id.edit_party_invitees_text);
            }
        });

        findViewById(R.id.edit_party_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAndCreateParty(R.id.create_party_latitude_edit_text, R.id.create_party_venue_edit_text, R.id.create_party_longitude_edit_text);
            }
        });

        findViewById(R.id.edit_party_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAndCreateParty(R.id.edit_party_latitude_edit_text, R.id.edit_party_venue_edit_text, R.id.edit_party_longitude_edit_text);
            }
        });

        if (getSupportActionBar() == null)
            return;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_party, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }
}
