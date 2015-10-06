package student.rmit.edu.au.s3110401mad_assignment.controller;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import student.rmit.edu.au.s3110401mad_assignment.R;
import student.rmit.edu.au.s3110401mad_assignment.model.PartyInviteeModel;
import student.rmit.edu.au.s3110401mad_assignment.model.Contacts;
import student.rmit.edu.au.s3110401mad_assignment.model.ContactsModel;
import student.rmit.edu.au.s3110401mad_assignment.model.Movie;
import student.rmit.edu.au.s3110401mad_assignment.model.Party;
import student.rmit.edu.au.s3110401mad_assignment.model.PartyModel;
import student.rmit.edu.au.s3110401mad_assignment.model.PartyStruct;
import student.rmit.edu.au.s3110401mad_assignment.model.async_task.PartyEditDBTask;
import student.rmit.edu.au.s3110401mad_assignment.model.chain_of_responsibility.MovieMemoryManagementClient;

public class PartyEditActivity extends BasePartyActivity {

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
            whichMovie = movieId = party.getImDB();

            if(party.getDate() != null)
                datetime.setTime(party.getDate().getTime());

            String movieTitle = "0";
            List<Movie> movies;
            if (party.getMovieTitle() != null) {
                movies = new MovieMemoryManagementClient(this)
                        .execute(party.getMovieTitle()).get();

                movieTitle = party.getMovieTitle();
                for (Movie movie : movies) {
                    if(movie.getId().equals(movieId)) {
                        movieTitle = movie.getTitle();
                        break;
                    }
                }
            }

            viewById.setText(movieTitle + " " + getText(R.string.party_movie_text));

            List<Contacts> contacts = PartyInviteeModel.getSingleton().getByPartyId(partyId);
            checkedContactNames = new String[contacts.size()];
            for(int i=0; i < contacts.size(); i++) {
                checkedContactNames[i] = contacts.get(i).getName();
                whichContacts.add(contacts.get(i).getId());
            }

            ((TextView) findViewById(R.id.edit_party_invitees_text)).setText(
                    checkedContactNames.length + " " + getText(R.string.party_invitees_text));
            ((EditText)findViewById(R.id.edit_party_venue_edit_text)).setText(party.getVenue());
            ((EditText)findViewById(R.id.edit_party_longitude_edit_text)).setText(
                    "" + party.getLocation()[PartyStruct.LONGITUDE]);
            ((EditText)findViewById(R.id.edit_party_latitude_edit_text)).setText(
                    "" + party.getLocation()[PartyStruct.LATITUDE]);
        } catch (Exception e) {
            Log.e("ayy lmao wrong", e.getMessage());
            viewById.setText(
                    "0 " + getText(R.string.party_movie_text)
            );
        }

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

        final Context context = this;
        findViewById(R.id.edit_party_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Party party = submitAndCreateParty(R.id.edit_party_latitude_edit_text,
                        R.id.edit_party_venue_edit_text,
                        R.id.edit_party_latitude_edit_text);

                if(party != null) {
                    new PartyEditDBTask(context,party,whichContacts).execute();
                    finishActivity();
                }
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
