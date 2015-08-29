package student.rmit.edu.au.s3110401mad_assignment.controller;

import android.app.AlertDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import student.rmit.edu.au.s3110401mad_assignment.R;
import student.rmit.edu.au.s3110401mad_assignment.model.Contacts;
import student.rmit.edu.au.s3110401mad_assignment.model.ContactsModel;
import student.rmit.edu.au.s3110401mad_assignment.model.Movie;
import student.rmit.edu.au.s3110401mad_assignment.model.MovieModel;
import student.rmit.edu.au.s3110401mad_assignment.model.Party;
import student.rmit.edu.au.s3110401mad_assignment.model.PartyModel;
import student.rmit.edu.au.s3110401mad_assignment.model.PartyStruct;
import student.rmit.edu.au.s3110401mad_assignment.view.DatePickerFragment;
import student.rmit.edu.au.s3110401mad_assignment.view.MultiSelectListFragment;
import student.rmit.edu.au.s3110401mad_assignment.view.TimePickerFragment;

public class CreatePartyActivity extends AppCompatActivity {
    public static final int ZERO_NUMBER_LESS_TEN = 10;
    public static final int MIDNIGHT = 0;

    private TimePickerFragment time;
    private DatePickerFragment date;
    private Calendar datetime;
    private String movieId;
    private AsyncTask<Context, Void, Map<String, Contacts>> asyncContactsTask;

    private List<String> whichMovie = new ArrayList<>();
    private List<String> whichContacts = new ArrayList<>();

    public TimePickerFragment getTime() {
        return time;
    }
    public DatePickerFragment getDate() {
        return date;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_party);

        asyncContactsTask = new ContactsModel().execute(this);

        try {
            Bundle extras = getIntent().getExtras();
            movieId = extras.getString(getString(R.string.movie_id));
        } catch (Exception e) {
            System.out.println("Oh no! Something happened: " + e.getMessage());
        }
        datetime = Calendar.getInstance();

        findViewById(R.id.create_party_date_picker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        findViewById(R.id.create_party_time_picker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        findViewById(R.id.create_party_movie_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMovieSelectList();
            }
        });

        findViewById(R.id.create_party_invitees_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContactsSelectList();
            }
        });

        findViewById(R.id.create_party_submit).setOnClickListener(new View.OnClickListener() {
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

    private void submitAndCreateParty() {
        Double longitude;
        Double latitude;
        String venueTitle;
        try {
            longitude =
                    Double.parseDouble(((EditText) findViewById(R.id.create_party_longitude_edit_text)).getText().toString());
            latitude =
                    Double.parseDouble(((EditText) findViewById(R.id.create_party_latitude_edit_text)).getText().toString());
            venueTitle = ((EditText)findViewById(R.id.create_party_venue_edit_text)).getText().toString();
        } catch (Exception e) {
            Toast.makeText(this,"Please enter a location number",
                    Toast.LENGTH_LONG).show();
            return;
        }

        double[] location = {longitude,latitude};
        PartyStruct partyStruct = new PartyStruct(
                PartyModel.getSingleton().getAllParties().size(),
                whichMovie,
                datetime,
                venueTitle,
                location,
                whichContacts
        );

        PartyModel.getSingleton().addParty(partyStruct);

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void showMovieSelectList() {
        Bundle args = new Bundle();
        MovieModel movieModel = MovieModel.getSingleton();
        final String[] movieTitles = new String[movieModel.getAllMovies().size()];
        int index = 0;
        for (Movie movie : movieModel.getAllMovies()) {
            movieTitles[index] = movie.getTitle();
            index++;
        }
        args.putStringArray("list_title", movieTitles);
        if (movieId != null)
            args.putString("selected_title", movieModel.getMovieById(movieId).getTitle());

        final Context context = this;
        MultiSelectListFragment movieList = new MultiSelectListFragment();
        movieList.setCallBack(
                new DialogInterface.OnMultiChoiceClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        String movieId =
                                MovieModel.getSingleton().getByName(movieTitles[which]).getId();
                        if(isChecked)
                            whichMovie.add(movieId);
                        else if(whichMovie.contains(movieId))
                            whichMovie.remove(movieId);
                    }
                });
        movieList.setArguments(args);
        movieList.show(getSupportFragmentManager(), "Movie Select");
    }

    private void showContactsSelectList() {
        Bundle args = new Bundle();
        final String[] contactNames;
        try {
            Map<String, Contacts> contactsMap = asyncContactsTask.get();
            ContactsModel.getSingleton().setContactsMap(contactsMap);

            contactNames = new String[contactsMap.size()];
            int i = 0;
            for (Map.Entry<String, Contacts> entry : contactsMap.entrySet()) {
                contactNames[i] = entry.getValue().getName();
                i++;
            }
        } catch (Exception e) {
            return;
        }
        args.putStringArray("list_title", contactNames);

        MultiSelectListFragment movieList = new MultiSelectListFragment();
        movieList.setArguments(args);
        movieList.setCallBack(
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        String contactId =
                                ContactsModel.getSingleton().getByName(contactNames[which]).getId();
                        if(isChecked)
                            whichContacts.add(contactId);
                        else if(whichContacts.contains(contactId))
                            whichContacts.remove(contactId);
                    }
                });
        movieList.show(getSupportFragmentManager(), "Contact Select Picker");
    }

    private void showDatePicker() {
        Bundle args = new Bundle();
        args.putInt("year", datetime.get(Calendar.YEAR));
        args.putInt("month", datetime.get(Calendar.MONTH));
        args.putInt("day", datetime.get(Calendar.DAY_OF_MONTH));

        date = new DatePickerFragment();
        date.setArguments(args);
        date.setCallBack(new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                datetime.set(Calendar.YEAR, year);
                datetime.set(Calendar.MONTH, monthOfYear);
                datetime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String stringDate = String.valueOf(year) + "-" + String.valueOf(monthOfYear)
                        + "-" + String.valueOf(dayOfMonth);
                ((TextView) findViewById(R.id.create_party_date_text)).setText(stringDate);
                Toast.makeText(
                        CreatePartyActivity.this,
                        String.valueOf(year) + "-" + String.valueOf(monthOfYear)
                                + "-" + String.valueOf(dayOfMonth),
                        Toast.LENGTH_LONG).show();
            }
        });
        date.show(getSupportFragmentManager(), "Date Picker");
    }

    private void showTimePicker() {
        Bundle args = new Bundle();
        args.putInt("hourOfDay", datetime.get(Calendar.HOUR_OF_DAY));
        args.putInt("minute", datetime.get(Calendar.MINUTE));

        time = new TimePickerFragment();
        time.setArguments(args);
        time.setCallBack(new OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                datetime.set(Calendar.MINUTE, minute);

                String am_pm = (datetime.get(Calendar.AM_PM) == Calendar.AM) ? "AM" : "PM";
                String strHrsToShow = (datetime.get(Calendar.HOUR) == MIDNIGHT) ? "12" : datetime.get(Calendar.HOUR) + "";
                String strMinToShow = (datetime.get(Calendar.MINUTE) < ZERO_NUMBER_LESS_TEN) ? "0" + datetime.get(Calendar.MINUTE) :
                        "" + datetime.get(Calendar.MINUTE);
                String stringTime = strHrsToShow + ":" + strMinToShow + " " + am_pm;
                ((TextView) findViewById(R.id.create_party_time_text)).setText(stringTime);
                Toast.makeText(
                        CreatePartyActivity.this, stringTime, Toast.LENGTH_SHORT
                ).show();
            }
        });
        time.show(getSupportFragmentManager(), "Time Picker");
    }
}
