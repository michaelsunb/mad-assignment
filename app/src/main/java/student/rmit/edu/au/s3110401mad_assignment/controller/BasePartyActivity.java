package student.rmit.edu.au.s3110401mad_assignment.controller;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import student.rmit.edu.au.s3110401mad_assignment.R;
import student.rmit.edu.au.s3110401mad_assignment.model.Contacts;
import student.rmit.edu.au.s3110401mad_assignment.model.ContactsModel;
import student.rmit.edu.au.s3110401mad_assignment.model.Movie;
import student.rmit.edu.au.s3110401mad_assignment.model.MovieModel;
import student.rmit.edu.au.s3110401mad_assignment.model.PartyModel;
import student.rmit.edu.au.s3110401mad_assignment.model.PartyStruct;
import student.rmit.edu.au.s3110401mad_assignment.view.DatePickerFragment;
import student.rmit.edu.au.s3110401mad_assignment.view.MultiSelectListFragment;
import student.rmit.edu.au.s3110401mad_assignment.view.TimePickerFragment;

/**
 * Created by Michaelsun Baluyos on 30/08/2015.
 */
public class BasePartyActivity extends AppCompatActivity {
    final static private String LOG_ERROR = "ERROR @ ";

    public static final int ZERO_NUMBER_LESS_TEN = 10;
    public static final int MIDNIGHT = 0;

    protected int partyId;
    protected String[] movieIds;
    protected String[] contactIds;
    protected Calendar datetime;
    protected AsyncTask<Context, Void, Map<String, Contacts>> asyncContactsTask;
    protected List<String> whichMovie = new ArrayList<>();
    protected List<String> whichContacts = new ArrayList<>();
    private Map<String, Contacts> contactsMap;

    private DatePickerFragment date = new DatePickerFragment();
    private TimePickerFragment time = new TimePickerFragment();

    private MultiSelectListFragment movieList = new MultiSelectListFragment();
    private MultiSelectListFragment contactList = new MultiSelectListFragment();

    public TimePickerFragment getTime() {
        return time;
    }
    public DatePickerFragment getDate() {
        return date;
    }

    public void submitAndCreateParty(int party_latitude_edit_text, int party_venue_edit_text, int party_longitude_edit_text) {
        Double longitude;
        Double latitude;
        String venueTitle;
        try {
            longitude =
                    Double.parseDouble(((EditText) findViewById(party_longitude_edit_text)).getText().toString());
            latitude =
                    Double.parseDouble(((EditText) findViewById(party_latitude_edit_text)).getText().toString());
            venueTitle = ((EditText)findViewById(party_venue_edit_text)).getText().toString();
        } catch (Exception e) {
            Toast.makeText(this, "Please enter a location number",
                    Toast.LENGTH_LONG).show();
            return;
        }

        double[] location = {longitude,latitude};


        PartyStruct partyStruct = new PartyStruct(
                partyId,
                whichMovie,
                datetime.getTime(),
                venueTitle,
                location,
                whichContacts
        );

        PartyModel.getSingleton().addParty(partyStruct);

        if(whichContacts.size() > 0) {
            ContactsModel contactsModel = ContactsModel.getSingleton();
            contactsModel.setContactsMap(contactsMap);
            for(String contactId : whichContacts) {
                Contacts contact = contactsModel.getById(contactId);
                for(String forContact : contact.getPhone()) {

                    JSONObject movieObj = new JSONObject();
                    JSONArray movieObjArray = new JSONArray();
                    try {
                        for (String movieId2 : whichMovie) {
                            movieObj.put(movieId2, MovieModel.getSingleton().getMovieById(movieId2).getTitle());
                        }
                        movieObjArray.put(movieObj);
                    } catch(Exception ex) {
                        Log.i(LOG_ERROR, ex.getMessage());
                    }

                    JSONObject contactObj = new JSONObject();
                    JSONArray contactObjArray = new JSONArray();
                    try {
                        for (String contactId2 : whichContacts) {
                            contactObj.put(contactId2,contactsModel.getById(contactId2).getName());
                        }
                        contactObjArray.put(contactObj);
                    } catch(Exception ex) {
                        ex.printStackTrace();
                    }

                    JSONObject locationObj = new JSONObject();
                    JSONArray locationObjArray = new JSONArray();
                    try {
                        locationObj.put("longitude",location[PartyStruct.LONGITUDE]);
                        locationObj.put("latitude",location[PartyStruct.LATITUDE]);
                        locationObjArray.put(locationObj);
                    } catch(Exception ex) {
                        ex.printStackTrace();
                    }

                    JSONObject message = new JSONObject();
                    try {
                        int year = datetime.get(Calendar.YEAR);
                        int monthOfYear = datetime.get(Calendar.MONTH);
                        int dayOfMonth = datetime.get(Calendar.DAY_OF_MONTH);

                        message.put("attendees", contactObjArray);
                        message.put("datetime", year + "-" + monthOfYear + "-" + dayOfMonth);
                        message.put("venue", venueTitle);
                        message.put("location", locationObjArray);
                        message.put("movies", movieObjArray);

                        Toast.makeText(this, "Message sent to " + whichContacts.size() + " contacts."
                                , Toast.LENGTH_SHORT).show();
                        SmsManager sms = SmsManager.getDefault();
                        sms.sendTextMessage(forContact, null, message.toString(), null, null);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

        Intent intent = new Intent(this, MovieListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void showMovieSelectList(final Integer party_movie_text) {
        MovieModel movieModel = MovieModel.getSingleton();
        final Bundle args = new Bundle();
        final String[] movieTitles = new String[movieModel.getAllMovies().size()];
        int index = 0;
        for (Movie movie : movieModel.getAllMovies()) {
            movieTitles[index] = movie.getTitle();
            index++;
        }
        args.putStringArray("list_title", movieTitles);
        if (movieIds != null &&
                movieIds.length > 0) {
            String[] movieTitle = new String[movieIds.length];
            int i = 0;
            for(String movieId : movieIds) {
                if(movieModel.getMovieById(movieId) == null)
                    break;

                movieTitle[i] = movieModel.getMovieById(movieId).getTitle();
                i++;
            }
            args.putStringArray("selected_title", movieTitle);
        }

        movieList.setCallBack(
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        String movieId =
                                MovieModel.getSingleton().getByName(movieTitles[which]).getId();
                        if (isChecked) {
                            if(!whichMovie.contains(movieId))
                                whichMovie.add(movieId);
                        } else {
                            if(whichMovie.contains(movieId))
                                whichMovie.remove(movieId);
                        }

                        movieList.setCheckedItems(which,isChecked);
                    }
                },
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((TextView) findViewById(party_movie_text)).setText(
                                whichMovie.size() + " " + getText(R.string.party_movie_text));
                    }
                });
        movieList.setArguments(args);
        movieList.show(getSupportFragmentManager(), "Movie Select");
    }

    public void showContactsSelectList(final int party_invitees_text) {
        Bundle args = new Bundle();
        final String[] contactNames;
        try {
            contactsMap = asyncContactsTask.get();
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
        if(contactIds != null)
            args.putStringArray("selected_title", contactIds);

        contactList.setArguments(args);
        contactList.setCallBack(
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        String contactId =
                                ContactsModel.getSingleton().getByName(contactNames[which]).getId();
                        if (isChecked)
                            whichContacts.add(contactId);
                        else if (whichContacts.contains(contactId))
                            whichContacts.remove(contactId);
                    }
                },
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((TextView) findViewById(party_invitees_text)).setText(
                                whichContacts.size() + " " + getText(R.string.party_invitees_text));
                    }
                });
        contactList.show(getSupportFragmentManager(), "Contact Select Picker");
    }

    public void showDatePicker(final int party_date_text) {
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

    public void showTimePicker(final int party_time_text) {
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
