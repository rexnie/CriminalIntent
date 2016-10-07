package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by niedaocai on 10/6/16.
 */

public class TimePickerFragment extends DialogFragment {
    private static final String ARG_TIME = "arg_time";
    public static final String EXTRA_TIME = "com.bignerdranch.android.criminalintent.time";
    private TimePicker mTimePicker;

    public static TimePickerFragment newInstance(Date time) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_TIME, time);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date time = (Date) getArguments().getSerializable(ARG_TIME);
        Calendar c = Calendar.getInstance();
        c.setTime(time);

        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH);
        final int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        final int second = c.get(Calendar.SECOND);


        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.dialog_time, null);

        mTimePicker = (TimePicker) v.findViewById(R.id.dialog_time_time_picker);
        mTimePicker.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
        mTimePicker.setCurrentMinute(c.get(Calendar.MINUTE));
        mTimePicker.setOnTimeChangedListener(null);


        return new AlertDialog.Builder(getActivity())
                .setTitle("set time of crime:")
                .setView(v)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Date date = new GregorianCalendar(year,
                                month,
                                dayOfMonth,
                                mTimePicker.getCurrentHour(),
                                mTimePicker.getCurrentMinute(),
                                second).getTime();
                        sendResult(Activity.RESULT_OK, date);
                    }
                })
                .create();
    }

    private void sendResult(int resultCode, Date date) {
        Fragment targetFragment = getTargetFragment();
        if (targetFragment == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, date);
        targetFragment.onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
