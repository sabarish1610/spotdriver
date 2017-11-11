package com.example.suriya.spotdrivers.support.calender;

import android.app.DatePickerDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.suriya.spotdrivers.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Suriya on 03-10-2017.
 */

public class SetData implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
     EditText editText;
     Context context;
   private java.util.Calendar myCalender;

    public SetData(EditText editText, Context context) {
        this.editText = editText;
        this.editText.setOnClickListener(this);
        this.myCalender = java.util.Calendar.getInstance();
        this.context = context;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String dateFormate = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormate, Locale.US);
        myCalender.set(java.util.Calendar.YEAR, year);
        myCalender.set(java.util.Calendar.MONTH, month);
        myCalender.set(java.util.Calendar.DAY_OF_MONTH,dayOfMonth);
        editText.setText(simpleDateFormat.format(myCalender.getTime()));
    }

    /*@Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
            new DatePickerDialog(context, this, myCalender
                    .get(java.util.Calendar.YEAR), myCalender.get(java.util.Calendar.MONTH),
                    myCalender.get(java.util.Calendar.DAY_OF_MONTH)).show();
        }
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.driver_date_of_birth:
            new DatePickerDialog(context, this, myCalender
                    .get(java.util.Calendar.YEAR), myCalender.get(java.util.Calendar.MONTH),
                    myCalender.get(java.util.Calendar.DAY_OF_MONTH)).show();
                break;
        }

    }
}
