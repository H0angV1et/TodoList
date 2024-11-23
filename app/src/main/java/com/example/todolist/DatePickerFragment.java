package com.example.todolist;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DatePickerFragment extends DialogFragment implements
        DatePickerDialog.OnDateSetListener {

    public static final String TAG = "datePicker";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LocalDate d = LocalDate.now();
        int year = d.getYear();
        int month = d.getMonthValue();
        int day = d.getDayOfMonth();
        return new DatePickerDialog(getActivity(), this, year, --month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        LocalDate selectedDate = LocalDate.of(year, ++month, day);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, d MMMM yyyy");
        ((TaskDetailActivity) getActivity()).updateDueDate(selectedDate.format(formatter));
    }
}
