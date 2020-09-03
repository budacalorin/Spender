package com.example.dayplanner;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment {

    DatePickerDialog.OnDateSetListener listener;
    Date customDate;

    public DatePickerFragment(DatePickerDialog.OnDateSetListener listener){
        this.listener = listener;
    }

    public void setDate(Date date){
        customDate = date;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = customDate==null ? c.get(Calendar.YEAR) : customDate.getYear();
        int month = customDate==null ? c.get(Calendar.MONTH) : customDate.getMonth();
        int day = customDate==null ? c.get(Calendar.DAY_OF_MONTH) : customDate.getDate();

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), listener, year, month, day);
    }
}