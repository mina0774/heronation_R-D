package com.example.heronation.login_register;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;

public class DatePickerFragment extends DialogFragment {
    DatePickerDialog.OnDateSetListener ondateSet;
    /* 년도, 월, 일 변수*/
    private int year, month, day;
    public DatePickerFragment() { }

    public void setCallBack(DatePickerDialog.OnDateSetListener ondate) {
        ondateSet = ondate;
    }
    @SuppressLint("NewApi")
    @Override
    /*입력 받기*/
    public void setArguments(Bundle args) {
        super.setArguments(args);
        year = args.getInt("year");
        month = args.getInt("month");
        day = args.getInt("day");
    }
    /*년도, 월, 일 받아와 출력*/
    public Dialog onCreateDialog(Bundle savedInstanceState){
        return new DatePickerDialog(getActivity(), ondateSet,year, month, day);
    }
}
