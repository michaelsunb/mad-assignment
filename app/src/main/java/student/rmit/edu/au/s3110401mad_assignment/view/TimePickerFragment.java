package student.rmit.edu.au.s3110401mad_assignment.view;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class TimePickerFragment extends DialogFragment {
    OnTimeSetListener onTimeSet;

    public TimePickerFragment() {
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

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new TimePickerDialog(getActivity(), onTimeSet, hourOfDay, minute, false);
    }
}