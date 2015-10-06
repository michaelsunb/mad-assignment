package student.rmit.edu.au.s3110401mad_assignment.controller;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import student.rmit.edu.au.s3110401mad_assignment.model.async_task.ContactQueryDBTask;
import student.rmit.edu.au.s3110401mad_assignment.view.DatePickerDialogFragment;
import student.rmit.edu.au.s3110401mad_assignment.view.MultiSelectListDialogFragment;
import student.rmit.edu.au.s3110401mad_assignment.view.SingleSelectListFragment;
import student.rmit.edu.au.s3110401mad_assignment.view.TimePickerDialogFragment;

/**
 * Created by Michaelsun Baluyos on 30/08/2015.
 *
 *
 */
public class BasePartyActivity extends AppCompatActivity {
    public static final int ZERO_NUMBER_LESS_TEN = 10;
    public static final int MIDNIGHT = 0;

    protected int partyId;
    protected String whichMovie;
    protected String movieId;
    protected String[] checkedContactNames;
    protected Calendar datetime;
    protected AsyncTask<Context, Void, Map<String, Contacts>> asyncContactsTask;

    protected List<String> whichContacts = new ArrayList<>();

    private DatePickerDialogFragment date = new DatePickerDialogFragment();
    private TimePickerDialogFragment time = new TimePickerDialogFragment();

    private SingleSelectListFragment movieList = new SingleSelectListFragment();
    private MultiSelectListDialogFragment contactList = new MultiSelectListDialogFragment();

    public TimePickerDialogFragment getTime() {
        return time;
    }
    public DatePickerDialogFragment getDate() {
        return date;
    }

    protected Party submitAndCreateParty(int party_latitude_edit_text,
                                         int party_venue_edit_text,
                                         int party_longitude_edit_text) {
        Double longitude;
        Double latitude;
        String venueTitle;
        try {
            longitude =
                    Double.parseDouble(
                            ((EditText) findViewById(party_longitude_edit_text))
                                    .getText().toString());
            latitude =
                    Double.parseDouble(
                            ((EditText) findViewById(party_latitude_edit_text))
                                    .getText().toString());

            venueTitle = ((EditText)findViewById(party_venue_edit_text)).getText().toString();

            if(whichContacts.size() == 0) {
                Toast.makeText(this, "Please choose a contact",
                        Toast.LENGTH_LONG).show();
                return null;
            }

        } catch (Exception e) {
            Toast.makeText(this, "Please enter a location number",
                    Toast.LENGTH_LONG).show();
            return null;
        }

        double[] location = {longitude,latitude};


        String movieTitle = MovieModel.getSingleton().getMovieById(whichMovie).getTitle();
        Log.e("ayy lmao " + whichMovie, movieTitle);
        PartyStruct partyStruct = new PartyStruct(
                partyId,
                whichMovie,
                movieTitle,
                datetime,
                venueTitle,
                location);

        PartyModel.getSingleton().addParty(partyStruct);
        ContactsModel.getSingleton().setContactsToParty(whichContacts, partyId);

        return partyStruct;
    }

    protected void finishActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    protected void showMovieSelectList(final Integer party_movie_text) {
        MovieModel movieModel = MovieModel.getSingleton();
        final Bundle args = new Bundle();
        final String[] movieTitles = new String[movieModel.getAllMovies().size()];
        int index = 0;
        for (Movie movie : movieModel.getAllMovies()) {
            movieTitles[index] = movie.getTitle();
            index++;
        }
        args.putStringArray("list_title", movieTitles);

        movieList.setCallBack(
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        whichMovie = MovieModel.getSingleton().getByName(movieTitles[id]).getId();

                        ((TextView) findViewById(party_movie_text)).setText(
                                movieTitles[id] + " " + getText(R.string.party_movie_text));
                        dialog.dismiss();
                    }
                });
        movieList.setArguments(args);
        movieList.show(getSupportFragmentManager(), "Movie Select");
    }

    protected void showContactsSelectList(final int party_invitees_text) {
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
        if(checkedContactNames != null)
            args.putStringArray("selected_title", checkedContactNames);

        final List<String> tempWhichContacts = whichContacts;

        contactList.setArguments(args);
        contactList.setCallBack(
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        ContactsModel contactsModel = ContactsModel.getSingleton();
                        String contactId =
                                contactsModel.getByName(contactNames[which]).getId();

                        if (isChecked) {
                            if(!tempWhichContacts.contains(contactId))
                                tempWhichContacts.add(contactId);
                        } else if (tempWhichContacts.contains(contactId)) {
                            tempWhichContacts.remove(contactId);
                        }
                    }
                },
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((TextView) findViewById(party_invitees_text)).setText(
                                whichContacts.size() + " " + getText(R.string.party_invitees_text));

                        if(whichContacts.size() == 0) {
                            checkedContactNames = null;
                            return;
                        }

                        whichContacts = tempWhichContacts;
                        checkedContactNames = new String[whichContacts.size()];
                        int i = 0;
                        for (String contactId : whichContacts) {
                            checkedContactNames[i++] =
                                    ContactsModel.getSingleton().getById(contactId).getName();
                        }
                    }
                });
        contactList.show(getSupportFragmentManager(), "Contact Select Picker");
    }

    protected void showDatePicker(final int party_date_text) {
        Bundle args = new Bundle();
        args.putInt("year", datetime.get(Calendar.YEAR));
        args.putInt("month", datetime.get(Calendar.MONTH));
        args.putInt("day", datetime.get(Calendar.DAY_OF_MONTH));

        date.setArguments(args);
        date.setCallBack(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                datetime.set(Calendar.YEAR, year);
                datetime.set(Calendar.MONTH, monthOfYear);
                datetime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String stringDate = String.valueOf(year) + "-" + String.valueOf(monthOfYear)
                        + "-" + String.valueOf(dayOfMonth);
                ((TextView) findViewById(party_date_text)).setText(
                        getString(R.string.party_date_text) + stringDate);
                Toast.makeText(
                        BasePartyActivity.this,
                        String.valueOf(year) + "-" + String.valueOf(monthOfYear)
                                + "-" + String.valueOf(dayOfMonth),
                        Toast.LENGTH_LONG).show();
            }
        });
        date.show(getSupportFragmentManager(), "Date Picker");
    }

    protected void showTimePicker(final int party_time_text) {
        Bundle args = new Bundle();
        args.putInt("hourOfDay", datetime.get(Calendar.HOUR_OF_DAY));
        args.putInt("minute", datetime.get(Calendar.MINUTE));

        time.setArguments(args);
        time.setCallBack(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                datetime.set(Calendar.MINUTE, minute);

                String am_pm = (datetime.get(Calendar.AM_PM) == Calendar.AM) ? "AM" : "PM";
                String strHrsToShow = (datetime.get(Calendar.HOUR) == MIDNIGHT) ? "12" : datetime.get(Calendar.HOUR) + "";
                String strMinToShow = (datetime.get(Calendar.MINUTE) < ZERO_NUMBER_LESS_TEN) ? "0" + datetime.get(Calendar.MINUTE) :
                        "" + datetime.get(Calendar.MINUTE);
                String stringTime = strHrsToShow + ":" + strMinToShow + " " + am_pm;
                ((TextView) findViewById(party_time_text)).setText(
                        getString(R.string.party_time_text) + stringTime);
                Toast.makeText(
                        BasePartyActivity.this, stringTime, Toast.LENGTH_SHORT
                ).show();
            }
        });
        time.show(getSupportFragmentManager(), "Time Picker");
    }
}
