package student.rmit.edu.au.s3110401mad_assignment.controller;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

import student.rmit.edu.au.s3110401mad_assignment.R;
import student.rmit.edu.au.s3110401mad_assignment.model.ContactsModel;
import student.rmit.edu.au.s3110401mad_assignment.model.Party;
import student.rmit.edu.au.s3110401mad_assignment.model.PartyModel;

public class EditPartyActivity extends BasePartyActivity {

    private String date;
    private String time;
    private Calendar datetime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_party);

        asyncContactsTask = new ContactsModel().execute(this);

        try {
            Bundle extras = getIntent().getExtras();
            Party party =
                    PartyModel.getSingleton().getPartyById(extras.getInt(getString(R.string.party_id)));
            String[] tempMovieIds = new String[party.getIdDB().size()];
            int i = 0;
            for(String tempMovieId : tempMovieIds) {
                movieIds[0] = tempMovieId;
            }
//            movieIds = (String[])party.getIdDB().toArray();
            datetime = party.getDate();
            time = datetime.get(Calendar.HOUR_OF_DAY) + ":" + datetime.get(Calendar.MINUTE);
        } catch (Exception e) {
            System.out.println("Oh no! Something happened: " + e.getMessage());
        }

        if(whichContacts != null) {
            ((TextView) findViewById(R.id.edit_party_invitees_text)).setText(
                    whichContacts.size() + " " + getText(R.string.event_invitees_text)
            );
        }

        TextView viewById = (TextView) findViewById(R.id.edit_party_movie_text);
        if(movieIds != null) {
            viewById.setText(
                    movieIds.length + " " + getText(R.string.event_movie_text)
            );
        } else {
            viewById.setText(
                    "0 " + getText(R.string.event_movie_text)
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
                showMovieSelectList();
            }
        });

        findViewById(R.id.edit_party_invitees_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContactsSelectList();
            }
        });

        findViewById(R.id.edit_party_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAndCreateParty();
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
