package student.rmit.edu.au.s3110401mad_assignment.controller;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import student.rmit.edu.au.s3110401mad_assignment.R;
import student.rmit.edu.au.s3110401mad_assignment.model.Party;
import student.rmit.edu.au.s3110401mad_assignment.model.PartyModel;
import student.rmit.edu.au.s3110401mad_assignment.model.PartyStruct;
import student.rmit.edu.au.s3110401mad_assignment.view.DatePickerFragment;
import student.rmit.edu.au.s3110401mad_assignment.view.MovieListFragment;
import student.rmit.edu.au.s3110401mad_assignment.view.TimePickerFragment;

public class CreatePartyActivity extends AppCompatActivity {
    public static final int ZERO_NUMBER_LESS_TEN = 10;
    public static final int MIDNIGHT = 0;

    private Party party;
    private TimePickerFragment time;
    private DatePickerFragment date;
    private Calendar datetime;
    private String movieId;

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

        try {
            Bundle extras = getIntent().getExtras();
            movieId = extras.getString(getString(R.string.movie_id));
            party = new PartyStruct(PartyModel.getSingleton().getAllParties().size());
            party.setIdDB(movieId);
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

        final Context context = this;
        findViewById(R.id.create_party_invitees_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TempContactsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showMovieSelectList() {
        Bundle args = new Bundle();
        String[] movieIds = {"asfasf","asfasfasfasfasf","33521624623"};
        args.putStringArray("movie_ids", movieIds);

        MovieListFragment movieList = new MovieListFragment();
        movieList.setArguments(args);
        movieList.show(getSupportFragmentManager(), "Date Picker");
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
                ((TextView)findViewById(R.id.create_party_date_text)).setText(stringDate);
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
                ((TextView)findViewById(R.id.create_party_time_text)).setText(stringTime);
                Toast.makeText(
                        CreatePartyActivity.this, stringTime, Toast.LENGTH_SHORT
                ).show();
            }
        });
        time.show(getSupportFragmentManager(), "Time Picker");
    }
}
