package student.rmit.edu.au.s3110401mad_assignment.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class MultiSelectListFragment extends DialogFragment {

    private String[] listTitle;


    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        listTitle = args.getStringArray("list_title");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
        builderSingle.setTitle("Select:-");
        builderSingle.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        CharSequence[] items = listTitle;
        boolean[] checkedItems = new boolean[listTitle.length];
        for(int i=0; i < listTitle.length; i++) {
            checkedItems[i] = false;
        }

        builderSingle.setMultiChoiceItems(items, checkedItems,
                new DialogInterface.OnMultiChoiceClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(
                                getActivity());
                        builderInner.setMessage("which: " + which);
                        builderInner.setTitle("Your Selected Item is");
                        builderInner.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        dialog.dismiss();
                                    }
                                });
                        builderInner.show();
                    }
                }
        );
        builderSingle.setPositiveButton("Set",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return builderSingle.create();
    }
}