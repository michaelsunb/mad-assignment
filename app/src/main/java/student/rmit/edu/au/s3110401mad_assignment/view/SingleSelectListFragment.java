package student.rmit.edu.au.s3110401mad_assignment.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.ArrayAdapter;

public class SingleSelectListFragment extends DialogFragment {

    private String[] listTitle;
    private DialogInterface.OnClickListener onListenerSet;

    public void setCallBack(DialogInterface.OnClickListener onListenerSet) {
        this.onListenerSet = onListenerSet;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        listTitle = args.getStringArray("list_title");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
        builderSingle.setTitle("Select:-");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.select_dialog_singlechoice);
        arrayAdapter.addAll(listTitle);

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