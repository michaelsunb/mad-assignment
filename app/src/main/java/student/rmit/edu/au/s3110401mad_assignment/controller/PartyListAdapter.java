package student.rmit.edu.au.s3110401mad_assignment.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import student.rmit.edu.au.s3110401mad_assignment.R;
import student.rmit.edu.au.s3110401mad_assignment.model.Party;
import student.rmit.edu.au.s3110401mad_assignment.model.PartyModel;

import android.content.Context;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;

public class PartyListAdapter extends BaseAdapter {

    private List<Party> partyList = new ArrayList<Party>();
    private LayoutInflater inflater;
    private Context context;

    public PartyListAdapter(Context context, int resource,
                            List<Party> eventList) {
        this.partyList = eventList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return partyList.size();
    }

    @Override
    public Party getItem(int position) {
        return partyList.get(position);
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
            convertView = inflater.inflate(R.layout.activity_party_row,
                    parent, false);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.eventTitle = (TextView) convertView
                .findViewById(R.id.party_list_title_text);
        viewHolder.eventTitle.setText(event.getVenue());

        viewHolder.eventDate = (TextView) convertView
                .findViewById(R.id.party_list_date_text);

        String title = (event.getVenue() != null) ? event.getVenue() : "Event " + position;
        viewHolder.eventDate.setText(title);

        viewHolder.eventAttendeeCount = (TextView) convertView
                .findViewById(R.id.party_list_attendee_count);
        viewHolder.eventAttendeeCount.setText(convertView.getResources()
                .getString(R.string.party_list_attendee_text)
                + (new StringBuilder().append(event.getInviteeIDs().size()).toString()));

        viewHolder.deleteButton = (Button) convertView
                .findViewById(R.id.party_list_delete_button);
        viewHolder.deleteButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                PartyModel.getSingleton().deleteEvent(event.getId());
                partyList.remove(posToDelete);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}