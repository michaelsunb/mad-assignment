package student.rmit.edu.au.s3110401mad_assignment.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;

public class MultiSelectListDialogFragment extends DialogFragment {

    private String[] listTitle;
    private String[] selectedTitle;
    private DialogInterface.OnMultiChoiceClickListener onListenerSet;
    private DialogInterface.OnClickListener onClickListeber;

    private boolean[] checkedItems = null;

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
        Log.e("ayy lmao size of select", (selectedTitle != null ? selectedTitle.length :
                "It's a null!!!!") + "");
        getSelectedBooleans();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
        builderSingle.setTitle("Select:-");
        CharSequence[] items = listTitle;

        builderSingle.setMultiChoiceItems(items, checkedItems,onListenerSet);
        builderSingle.setPositiveButton("Set",onClickListeber);

        return builderSingle.create();
    }

    private void getSelectedBooleans() {
        checkedItems = new boolean[listTitle.length];
        if(selectedTitle != null) {
            for (int i = 0; i < listTitle.length; i++) {
                checkedItems[i] = false;
                for (String aSelectedTitle : selectedTitle) {
                    if (listTitle[i].equals(aSelectedTitle)) {
                        checkedItems[i] = true;
                        break;
                    }
                }
            }
        }
    }
}