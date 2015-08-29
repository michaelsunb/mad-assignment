package student.rmit.edu.au.s3110401mad_assignment.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import student.rmit.edu.au.s3110401mad_assignment.R;
import student.rmit.edu.au.s3110401mad_assignment.model.Party;
import student.rmit.edu.au.s3110401mad_assignment.model.PartyModel;

public class EventListActivity extends AppCompatActivity {

    private EventListAdapter listAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        listView = (ListView) findViewById(R.id.eventListListView);
        listAdapter = new EventListAdapter(this, 0, PartyModel.getSingleton().getAllParties());
        listView.setAdapter(listAdapter);

        if (getSupportActionBar() == null)
            return;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        listAdapter.notifyDataSetChanged();
    }
}
