package com.example.proyecto_dam_gian_monacelli;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendarView = findViewById(R.id.calendarView);
        dateTimeDisplay = (TextView) findViewById(R.id.text_date_display);


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

        getDate();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

            }
        });

    }

    public void setDate(int day, int month, int year) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        long milli = calendar.getTimeInMillis();
        calendarView.setDate(milli);

    }

    public void getDate() {

        long date = calendarView.getDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
        calendar.setTimeInMillis(date);
        String selected_date = simpleDateFormat.format(calendar.getTime());
        Toast.makeText(this, selected_date, Toast.LENGTH_SHORT).show();
    }


    public void onTiqBreakClick(View view) {
        // Get the current timestamp
        long currentTime = Long.parseLong(getCurrentTimestamp());

        // Save the current timestamp in the SQLite database using the DataBaseHelper class
        saveBreakActivationTime(this, currentTime);
    }


    // Method to save the break activation time
    public static void saveBreakActivationTime(Context context, long activationTime) {
        // Get instance of the database helper
        DataBaseHelper dbHelper = new DataBaseHelper(context);

        // Get writable database
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create ContentValues to insert data
        ContentValues values = new ContentValues();

        // Add data to ContentValues
        values.put("time_out", activationTime);

        // Insert row in timestamps table
        db.insert(DataBaseHelper.TIME_STAMPS_TABLE_NAME, null, values);
    }

    // Method to get the current timestamp
    public String getCurrentTimestamp() {
        return String.valueOf(System.currentTimeMillis());
    }


    // Method to save the counter activation time and date
    public static void saveCounterActivationTime(Context context, Date selectedDate, long activationTime) {

        // Get instance of the database helper
        DataBaseHelper dbHelper = new DataBaseHelper(context);

        // Get writable database
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create ContentValues to insert data
        ContentValues values = new ContentValues();

        // Add data to ContentValues
        values.put("date", selectedDate.toString());
        values.put("time", String.valueOf(activationTime));

        // Insert row in timestamps table
        db.insert(DataBaseHelper.TIME_STAMPS_TABLE_NAME, null, values);

    }


}



