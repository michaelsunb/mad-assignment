package student.rmit.edu.au.s3110401mad_assignment.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class MultiSelectListFragment extends DialogFragment {

    private String[] listTitle;
    private String[] selectedTitle;
    private DialogInterface.OnMultiChoiceClickListener onListenerSet;
    private DialogInterface.OnClickListener onClickListeber;


    public void setCallBack(DialogInterface.OnMultiChoiceClickListener onListenerSet,
                            DialogInterface.OnClickListener onClickListener) {
        this.onListenerSet = onListenerSet;
        this.onClickListeber = onClickListener;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        listTitle = args.getStringArray("list_title");
        selectedTitle = args.getStringArray("selected_title");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
        builderSingle.setTitle("Select:-");
        CharSequence[] items = listTitle;

        boolean[] checkedItems = new boolean[listTitle.length];
        if(selectedTitle != null) {
            for (int i = 0; i < listTitle.length; i++) {
                checkedItems[i] = false;
                for (int j = 0; j < selectedTitle.length; j++) {
                    if (listTitle[i].equals(selectedTitle[j])) {
                        checkedItems[i] = true;
                        break;
                    }
                }
            }
        }

        builderSingle.setMultiChoiceItems(items, checkedItems,onListenerSet);
        builderSingle.setPositiveButton("Set",onClickListeber);

        return builderSingle.create();
    }
}