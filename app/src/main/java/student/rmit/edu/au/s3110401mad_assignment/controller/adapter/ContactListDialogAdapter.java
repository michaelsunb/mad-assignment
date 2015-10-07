package student.rmit.edu.au.s3110401mad_assignment.controller.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import student.rmit.edu.au.s3110401mad_assignment.R;
import student.rmit.edu.au.s3110401mad_assignment.model.Contacts;

/**
 * Created by Michaelsun Baluyos on 25/08/2015.
 *
 *
 */
public class ContactListDialogAdapter extends ArrayAdapter<Contacts> {
    // Reference Controller
    private Activity activity;

    public ContactListDialogAdapter(Activity activity, List<Contacts> allContacts) {
        super(activity, R.layout.activity_main, allContacts);

        this.activity = activity;
    }

    @Override
    public View getView(int position, View cachedView, ViewGroup parent)
    {
        View movieItemView;

        if (cachedView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            movieItemView = inflater.inflate(R.layout.contact_list_dialog_row, parent, false);
        } else {
            movieItemView = cachedView;
        }

        // Get The model
        Contacts contact = getItem(position);

        // Get our views
        TextView nameView = (TextView) movieItemView.findViewById(R.id.custom_list_name);
        TextView phoneView = (TextView) movieItemView.findViewById(R.id.custom_list_phone);
        TextView emailView = (TextView) movieItemView.findViewById(R.id.custom_list_email);

        // Fill in the Views with content
        nameView.setText(contact.getName());
        String phone = contact.getPhone();
        phoneView.setText((phone != null) ? phone : "");
        String email = contact.getEmail();
        emailView.setText((email != null) ? phone : "");

        return movieItemView;
    }
}
