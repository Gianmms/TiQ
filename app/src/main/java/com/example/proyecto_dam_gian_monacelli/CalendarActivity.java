package com.example.proyecto_dam_gian_monacelli;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class CalendarActivity extends AppCompatActivity {
    CalendarView calendarView;
    Calendar calendar;
    private TextView dateTimeDisplay;
    private DataBaseHelper dbHelper;  // Database helper instance


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendarView = findViewById(R.id.calendarView);
        dateTimeDisplay = (TextView) findViewById(R.id.timestamp_display);
        dbHelper = new DataBaseHelper(this);

        ImageButton homeButton = findViewById(R.id.homeButton);
        ImageButton profileButton = findViewById(R.id.profileButton);


        //Abre MainActivity
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crea un Intent para abrir CalendarActivity
                setContentView(R.layout.activity_main);
                Intent intent = new Intent(CalendarActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

        //Abre ProfileActivity
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crea un Intent para abrir ProfileActivity
                setContentView(R.layout.activity_profile);
                Intent intent = new Intent(CalendarActivity.this, ProfileActivity.class);
                startActivity(intent);

            }
        });


        calendar = Calendar.getInstance();
        setDate(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));


        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
            String timestamp = dbHelper.getTimestampForDate(selectedDate);
            //dateTimeDisplay.setText(timestamp);
            dateTimeDisplay.setText(timestamp);
        });




    }


    //Asigna la fecha al CalendarView
    public void setDate(int day, int month, int year) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        long milli = calendar.getTimeInMillis();
        calendarView.setDate(milli);
    }


}



