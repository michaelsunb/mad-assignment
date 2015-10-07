package student.rmit.edu.au.s3110401mad_assignment.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import student.rmit.edu.au.s3110401mad_assignment.controller.adapter.ContactListDialogAdapter;
import student.rmit.edu.au.s3110401mad_assignment.model.PartyInviteeModel;

public class ContactListDialogFragment extends DialogFragment {

    private int partyId;
    private DialogInterface.OnClickListener onListenerSet;

    public void setCallBack(DialogInterface.OnClickListener onListenerSet) {
        this.onListenerSet = onListenerSet;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        partyId = args.getInt("party_id");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
        builderSingle.setTitle("Select:-");

        final ContactListDialogAdapter arrayAdapter = new ContactListDialogAdapter(
                getActivity(), PartyInviteeModel.getSingleton().getByPartyId(partyId));

        builderSingle.setAdapter(arrayAdapter, onListenerSet);
        builderSingle.setNegativeButton(
                "cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return builderSingle.create();
    }
}