package student.rmit.edu.au.s3110401mad_assignment.view;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

public class TimePickerDialogFragment extends DialogFragment {
    OnTimeSetListener onTimeSet;

    public TimePickerDialogFragment() {
    }

    public void setCallBack(OnTimeSetListener ondate) {
        onTimeSet = ondate;
    }

    private int hourOfDay, minute;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        hourOfDay = args.getInt("hourOfDay");
        minute = args.getInt("minute");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new TimePickerDialog(getActivity(), onTimeSet, hourOfDay, minute, false);
    }
}