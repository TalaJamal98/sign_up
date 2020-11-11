package com.example.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

public class Calender extends AppCompatActivity {

    CalendarView calenderview;
    Button save;
    TextView mytext;
    int myyear, mymonth, myday;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        calenderview=findViewById(R.id.cal);
        save=findViewById(R.id.addDate);
        mytext=findViewById(R.id.text);


        calenderview.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                 date =dayOfMonth + "/" + month + "/" + year;
                myyear=year;
                mymonth=month;
                myday=dayOfMonth;

                mytext.setText(date);

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            /*    calenderview.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        String date =dayOfMonth + "/" + month + "/" + year;
                        myyear=year;
                        mymonth=month;
                        myday=dayOfMonth;

                        mytext.setText(year);

                    }
                }); */

                Intent i = new Intent(Calender.this, createEvent.class);
                i.putExtra("year", myyear);
                i.putExtra("month", mymonth);
                i.putExtra("day", myday);
                i.putExtra("full",date);
                startActivity(i);

            }
        });
    }
}