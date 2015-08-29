package student.rmit.edu.au.s3110401mad_assignment.controller;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import student.rmit.edu.au.s3110401mad_assignment.R;
import student.rmit.edu.au.s3110401mad_assignment.model.Movie;
import student.rmit.edu.au.s3110401mad_assignment.model.Party;
import student.rmit.edu.au.s3110401mad_assignment.model.PartyModel;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class EventListAdapter extends BaseAdapter {

    private List<Party> eventList = new ArrayList<Party>();
    private LayoutInflater inflater;
    private Context context;

    public EventListAdapter(Context context, int resource,
                            List<Party> eventList) {
        this.eventList = eventList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Party getItem(int position) {
        return eventList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        private TextView eventTitle;
        private Button deleteButton;
        private TextView eventDate;
        private TextView eventAttendeeCount;
        private Button editButton;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        final Party event = getItem(position);
        final int posToDelete = position;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_events_row,
                    parent, false);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.eventTitle = (TextView) convertView
                .findViewById(R.id.row_event_title_text);
        viewHolder.eventTitle.setText(event.getVenue());

        viewHolder.eventDate = (TextView) convertView
                .findViewById(R.id.row_eventDateTextView);

        String title = (event.getVenue() != null) ? event.getVenue() : "Event " + position;
        viewHolder.eventDate.setText(title);

        viewHolder.eventAttendeeCount = (TextView) convertView
                .findViewById(R.id.row_eventAttendeeCount);
        viewHolder.eventAttendeeCount.setText(convertView.getResources()
                .getString(R.string.row_eventAttendeeCountText)
                + (new StringBuilder().append(event.getInviteeIDs().size()).toString()));

        viewHolder.deleteButton = (Button) convertView
                .findViewById(R.id.row_deleteButton);
        viewHolder.deleteButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                PartyModel.getSingleton().deleteEvent(event.getId());
                eventList.remove(posToDelete);
                notifyDataSetChanged();
            }
        });

        viewHolder.editButton = (Button) convertView
                .findViewById(R.id.row_editButton);
        viewHolder.editButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, EditEventActivity.class);
//                intent.putExtra("event_id", event.getId());
//                context.startActivity(intent);

            }
        });

        return convertView;
    }

    // update the entire list
    public void updateEventAdapterList(ArrayList<Party> newEventList) {
        eventList.clear();
        eventList.addAll(newEventList);
        this.notifyDataSetChanged();
    }
}