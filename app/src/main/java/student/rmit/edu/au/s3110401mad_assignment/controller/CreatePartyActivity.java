package student.rmit.edu.au.s3110401mad_assignment.controller;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

import student.rmit.edu.au.s3110401mad_assignment.R;
import student.rmit.edu.au.s3110401mad_assignment.model.ContactsModel;
import student.rmit.edu.au.s3110401mad_assignment.model.MovieModel;
import student.rmit.edu.au.s3110401mad_assignment.model.PartyModel;

public class CreatePartyActivity extends BasePartyActivity {

    public static final int MOVIE_ID = 0;
    public static final int NUMBER_OF_MOVIE_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_party);

        asyncContactsTask = new ContactsModel().execute(this);
        datetime = Calendar.getInstance();
        partyId = (PartyModel.getSingleton().getAllParties().size() + 1);

        try {
            Bundle extras = getIntent().getExtras();
            whichMovie = movieId = extras.getString(getString(R.string.movie_id));
            ((TextView) findViewById(R.id.create_party_movie_text)).setText(
                    MovieModel.getSingleton().getMovieById(movieId).getTitle()
                            + " " + getText(R.string.party_movie_text)
            );
        } catch (Exception e) {
            ((TextView) findViewById(R.id.create_party_movie_text)).setText(
                    "0 " + getText(R.string.party_movie_text)
            );
        }

        ((TextView) findViewById(R.id.create_party_invitees_text)).setText(
                whichContacts.size() + " " + getText(R.string.party_invitees_text)
        );

        findViewById(R.id.create_party_date_picker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(R.id.create_party_date_text);
            }
        });

        findViewById(R.id.create_party_time_picker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(R.id.create_party_time_text);
            }
        });

        findViewById(R.id.create_party_movie_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMovieSelectList(R.id.create_party_movie_text);
            }
        });

        findViewById(R.id.create_party_invitees_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContactsSelectList(R.id.create_party_invitees_text);
            }
        });

        findViewById(R.id.create_party_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAndCreateParty(R.id.create_party_latitude_edit_text, R.id.create_party_venue_edit_text, R.id.create_party_longitude_edit_text);
            }
        });

        if (getSupportActionBar() == null)
            return;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }

}
